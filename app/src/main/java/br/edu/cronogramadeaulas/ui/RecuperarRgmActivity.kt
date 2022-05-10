package br.edu.cronogramadeaulas.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import br.edu.cronogramadeaulas.R
import br.edu.cronogramadeaulas.dao.AlunoDAO

class RecuperarRgmActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperar_rgm)
        title = "Recuperar RGM"

        // Declarando variaveis
        val (db, et_emailRecupera: EditText) = declararVariaveis()

        // Botão que recupera RGM apartir
        recuperarRgm(db, et_emailRecupera)

    }

    private fun declararVariaveis(): Pair<AlunoDAO, EditText> {
        val db = AlunoDAO(applicationContext)
        val et_emailRecupera: EditText = findViewById(R.id.et_emailRecupera)
        return Pair(db, et_emailRecupera)
    }

    private fun recuperarRgm(
        db: AlunoDAO,
        et_emailRecupera: EditText
    ) {
        val btn_recuperar: Button = findViewById(R.id.btn_recuperarRgm)
        btn_recuperar.setOnClickListener() {
            val recuperaRgm = db.recuperarRgm(et_emailRecupera.text.toString())

            if (recuperaRgm != null) {

                AlertDialog.Builder(this)
                    .setTitle("Recuperado")
                    .setMessage("Seu RGM: $recuperaRgm")
                    .setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, i ->
                        finish()
                    })
                    .show()

            } else {

                AlertDialog.Builder(this)
                    .setTitle("Erro")
                    .setMessage("E-mail não encontrado")
                    .setPositiveButton("Ok", null)
                    .show()
            }

        }
    }
}