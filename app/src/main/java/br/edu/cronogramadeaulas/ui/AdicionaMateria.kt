package br.edu.cronogramadeaulas.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import br.edu.cronogramadeaulas.R
import br.edu.cronogramadeaulas.dao.MateriaDAO

class AdicionaMateria : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adiciona_materia)

        // Recebendo extras
        val (rgm, nomeDoUsuario) = receberExtras()

        // Criando cursos
        criarDropDownDeCursos(nomeDoUsuario, rgm)

    }

    private fun receberExtras(): Pair<Any?, Any?> {
        val extras: Bundle? = intent.extras
        val rgm = extras?.get("rgm")
        val nomeDoUsuario = extras?.get("userName")
        return Pair(rgm, nomeDoUsuario)
    }

    private fun criarDropDownDeCursos(nomeDoUsuario: Any?, rgm: Any?) {
        val autoCompleteTextView: AutoCompleteTextView = findViewById(R.id.autoCompleteTextView)
        val cursos = resources.getStringArray(R.array.Curso)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, cursos)
        autoCompleteTextView.setAdapter(arrayAdapter)

        autoCompleteTextView.setOnItemClickListener { adapterView, view, position, id ->
            val item = adapterView.getItemAtPosition(position)
            // Inscreve no curso ciências da computação
            inscreverCienciasDaComputacao(item, cursos, nomeDoUsuario, rgm)
        }
    }

    private fun inscreverCienciasDaComputacao(
        item: Any?,
        cursos: Array<String>,
        nomeDoUsuario: Any?,
        rgm: Any?
    ) {
        val btn_inscrever_se: Button = findViewById(R.id.btn_inscrever_se)
        btn_inscrever_se.setOnClickListener() {
            // Verifica se é Ciências da Computação e Adiciona as materias no perfil
            if (item == cursos[0]) {
                val intentDisciplinaActivity = Intent(this, DisciplinaActivity::class.java)
                intentDisciplinaActivity.putExtra("userName", nomeDoUsuario.toString())
                intentDisciplinaActivity.putExtra("rgm", rgm.toString())
                val dbMateria = MateriaDAO(this)
                dbMateria.adicionarMateriasCC(rgm.toString())

                finish()
                startActivity(intentDisciplinaActivity)

            }
        }
    }
}