package br.edu.cronogramadeaulas.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import br.edu.cronogramadeaulas.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class NoDisciplinaActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_disciplina)
        title = "Disciplina do Dia"

        // Recebendo extras
        val (nomeDoUsuario, rgm) = receberExtras()

        // Informa que não possui disciplinas cadastradas
        informarStatus()

        // Informa o nome do aluno
        informarNomeDoAluno(nomeDoUsuario)

        // Botão para adicionar cursos
        adicionarCursos(rgm, nomeDoUsuario)

        // Solicita permissão de localização para o usuário
        if (hasPermission()) return

    }

    private fun hasPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                1
            )
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_NETWORK_STATE),
                1
            )
            return true
        }
        return false
    }

    private fun receberExtras(): Pair<Any?, Any?> {
        val extras: Bundle? = intent.extras
        val nomeDoUsuario = extras?.get("userName")
        val rgm = extras?.get("rgm")
        return Pair(nomeDoUsuario, rgm)
    }

    private fun informarStatus() {
        AlertDialog.Builder(this)
            .setTitle("Aviso!")
            .setMessage("Você não possui disciplinas cadastradas")
            .setPositiveButton("Ok", null)
            .show()
    }

    private fun informarNomeDoAluno(nomeDoUsuario: Any?) {
        val nomeDoAluno: TextView = findViewById(R.id.tv_nomeAlunoNoDisciplina)
        nomeDoAluno.text = "Bem vindo, " + nomeDoUsuario.toString()
    }

    private fun adicionarCursos(rgm: Any?, nomeDoUsuario: Any?) {
        val fab_adicionaMateria: FloatingActionButton = findViewById(R.id.fab_adicionaMateria)
        fab_adicionaMateria.setOnClickListener() {
            val intentAdicionarMateria = Intent(this, AdicionaMateria::class.java)
            intentAdicionarMateria.putExtra("rgm", rgm.toString())
            intentAdicionarMateria.putExtra("userName", nomeDoUsuario.toString())
            finish()
            startActivity(intentAdicionarMateria)

        }
    }



}