package br.edu.cronogramadeaulas.ui

import android.annotation.SuppressLint
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import br.edu.cronogramadeaulas.R
import br.edu.cronogramadeaulas.dao.MateriaDAO
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import java.text.SimpleDateFormat
import java.util.*

class DisciplinaActivity : AppCompatActivity() {

    val dbMateria = MateriaDAO(this)

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_disciplina)
        title = "Disciplina do Dia"

        // Inicializando variáveis
        val (buttonConfirm: Button, nomeDaDisciplina: TextView, dataEHora: TextView) = inicializarVariavel()

        // Botão para finalizar o app
        finalizarApp()

        // Recebendo Extras
        val (nomeDoUsuario, rgm) = receberExtras()

        // Alterando nome do aluno
        alterarNomeDoAluno(nomeDoUsuario)

        // Verificando se o aluno possui aula hoje
        verificarSePossuiAula(rgm, nomeDaDisciplina, dataEHora, buttonConfirm)

        // Envia localizacao para a classe "LocalizacaoAtual.kt"
        enviarLocalizacaoAtualParaClasse()

        // Atualiza a localização atual
        atualizarLocalizacao()

        // Registra presença
        registrarPresenca(buttonConfirm, nomeDaDisciplina, rgm)

    }

    private fun finalizarApp() {
        val btn_sair: Button = findViewById(R.id.btn_sair)
        btn_sair.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        atualizarLocalizacao()
    }

    private fun inicializarVariavel(): Triple<Button, TextView, TextView> {
        val buttonConfirm: Button = findViewById(R.id.btn_registrar_presenca)
        val nomeDaDisciplina: TextView = findViewById(R.id.tv_nomeDaDisciplina)
        val dataEHora: TextView = findViewById(R.id.tv_dataHora)
        return Triple(buttonConfirm, nomeDaDisciplina, dataEHora)
    }

    private fun registrarPresenca(buttonConfirm: Button, nomeDaDisciplina: TextView, rgm: Any?) {
        buttonConfirm.setOnClickListener {
            atualizarLocalizacao()
            val date = Calendar.getInstance().time
            var dateTimeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            var dateFormat = SimpleDateFormat("EEEE", Locale.getDefault())
            val horaAtual = dateTimeFormat.format(date).toString().replace(":",".").toDouble()
            if (dateFormat.format(date) == "segunda-feira" || dateFormat.format(date) == "Monday"){
                if (horaAtual >= 19.10 && horaAtual <= 20.25){
                    verificarLocalizacaoAtual(nomeDaDisciplina, buttonConfirm)
                }else
                {
                    verificarHorario(rgm, nomeDaDisciplina)
                }
            }
            else if (horaAtual >= 10.10 && horaAtual <= 21.50){
                verificarLocalizacaoAtual(nomeDaDisciplina, buttonConfirm)
            }else
            {
                verificarHorario(rgm, nomeDaDisciplina)
            }

        }
    }

    private fun verificarHorario(rgm: Any?, nomeDaDisciplina: TextView) {
        val verificaMateria = dbMateria.buscarMateria(rgm.toString())
        verificaMateria!!.moveToFirst()
        if (nomeDaDisciplina.text == verificaMateria.getString(0)) {
            AlertDialog.Builder(this)
                .setTitle("Aviso!")
                .setMessage("A presença só pode ser registrada entre 19h10 e 20h25.")
                .setPositiveButton("Ok", null)
                .show()
        } else {
            AlertDialog.Builder(this)
                .setTitle("Aviso!")
                .setMessage("A presença só pode ser registrada entre 19h10 e 21h50.")
                .setPositiveButton("Ok", null)
                .show()
        }
    }

    private fun verificarLocalizacaoAtual(nomeDaDisciplina: TextView, buttonConfirm: Button) {
        val date = Calendar.getInstance().time
        var dateTimeFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        if (atualizarLocalizacao() < 104) {
            buttonConfirm.visibility = View.GONE
            AlertDialog.Builder(this)
                .setTitle("Aviso!")
                .setMessage( "${nomeDaDisciplina.text}\n\nLatitude:${LocalizacaoAtual.latitude} \nLongitude:${LocalizacaoAtual.longitude} \n\nPresença registrada em ${dateTimeFormat.format(date)}")
                .setPositiveButton("Ok", null)
                .show()
        } else {
            AlertDialog.Builder(this)
                .setTitle("Aviso!")
                .setMessage("Você precisa estar na faculdade para registrar a presença.")
                .setPositiveButton("Ok", null)
                .show()
        }
    }

    private fun atualizarLocalizacao(): Double {
        val localizacaoAtual = LatLng(LocalizacaoAtual.latitude, LocalizacaoAtual.longitude)
        val localizacaoUnicid = LatLng(-23.536404076471314, -46.560885725859094)
        val distancia : Double = SphericalUtil.computeDistanceBetween(localizacaoAtual, localizacaoUnicid)

        val tv_latitude : TextView = findViewById(R.id.tv_latitude)
        tv_latitude.text = "Latitude: " + LocalizacaoAtual.latitude

        val tv_longitude : TextView = findViewById(R.id.tv_longitude)
        tv_longitude.text = "Longitude: " + LocalizacaoAtual.longitude

        val tv_distancia : TextView = findViewById(R.id.tv_distancia)
        if (distancia.toInt()-103 <= 0){
            tv_distancia.text = "Você chegou na Unicid!"
        }
        else if (distancia.toInt()-103 >= 1000){
            tv_distancia.text = "Você está a " + (distancia.toInt()-103)/1000 + " km da Unicid"
        }
        else{
            tv_distancia.text = "Você está a " + (distancia.toInt()-103) + " metros da Unicid"
        }

        refresh(1000)

        return distancia

    }

    private fun refresh(milliseconds: Int) {
        val handler = Handler(Looper.getMainLooper())
        val runnable = Runnable {
            atualizarLocalizacao()
        }
        handler.postDelayed(runnable, milliseconds.toLong())
    }

    private fun enviarLocalizacaoAtualParaClasse() {
        val mLocManager: LocationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        val mLocListener = LocalizacaoAtual()
        mLocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1L, 1F, mLocListener)
    }

    private fun verificarSePossuiAula(rgm: Any?, nomeDaDisciplina: TextView, dataEHora: TextView, buttonConfirm: Button) {
        // Verifica o ID das matérias. Se =1, pertence ao ID das matérias de Ciências da Computação
        if (dbMateria.verificarIdMaterias(rgm.toString()).toString() == "1") {
            var naoTemAula = true
            val buscarMateria = dbMateria.buscarMateria(rgm.toString())
            buscarMateria!!.moveToFirst()

            // Verifica o dia da semana(hoje)
            val date = Calendar.getInstance().time
            val dateTimeFormat = SimpleDateFormat("EEEE", Locale.getDefault())

            // Altera nome da Disciplina do dia
            if (dateTimeFormat.format(date) == "segunda-feira" || dateTimeFormat.format(date) == "Monday") {
                nomeDaDisciplina.text = buscarMateria.getString(0); naoTemAula = false
                dataEHora.text = "Segunda-feira entre 19h10 e 20h25"
            }

            if (dateTimeFormat.format(date) == "quarta-feira" || dateTimeFormat.format(date) == "Wednesday") {
                nomeDaDisciplina.text = buscarMateria.getString(1); naoTemAula = false
                dataEHora.text = "Quarta-feira entre 19h10 e 21h50"
            }

            if (dateTimeFormat.format(date) == "quinta-feira" || dateTimeFormat.format(date) == "Thursday") {
                nomeDaDisciplina.text = buscarMateria.getString(2); naoTemAula = false
                dataEHora.text = "Quinta-feira entre 19h10 e 21h50"
            }

            if (dateTimeFormat.format(date) == "sexta-feira" || dateTimeFormat.format(date) == "Friday") {
                nomeDaDisciplina.text = buscarMateria.getString(3)
                dataEHora.text = "Sexta-feira entre 19h10 e 21h50"
            }

            else if (naoTemAula) {
                val cardView : CardView = findViewById(R.id.cardView)
                cardView.visibility = View.INVISIBLE
                dataEHora.text = "Hoje você não possui aula"
                dataEHora.textSize = 20F
                nomeDaDisciplina.visibility = View.INVISIBLE
                buttonConfirm.visibility = View.INVISIBLE
            }
        }
    }

    private fun receberExtras(): Pair<Any?, Any?> {
        val extras: Bundle? = intent.extras
        val nomeDoUsuario = extras?.get("userName")
        val rgm = extras?.get("rgm")
        return Pair(nomeDoUsuario, rgm)
    }

    private fun alterarNomeDoAluno(nomeDoUsuario: Any?) {
        val nomeDoAluno: TextView = findViewById(R.id.tv_nomeAluno)
        nomeDoAluno.text = "Bem vindo, " + nomeDoUsuario.toString()
    }

}