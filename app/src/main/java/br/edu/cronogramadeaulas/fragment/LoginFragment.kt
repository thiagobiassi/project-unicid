package br.edu.cronogramadeaulas.fragment

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import br.edu.cronogramadeaulas.R
import br.edu.cronogramadeaulas.dao.AlunoDAO
import br.edu.cronogramadeaulas.dao.MateriaDAO
import br.edu.cronogramadeaulas.ui.DisciplinaActivity
import br.edu.cronogramadeaulas.ui.NoDisciplinaActivity
import br.edu.cronogramadeaulas.ui.RecuperarRgmActivity
import java.text.SimpleDateFormat
import java.util.*

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        // Inicializando variáveis
        val (rgm: EditText, senha: EditText) = declararVariaveis(view)

        // Envia para a activity "RecuperarRgmActivity.kt"
        recuperarRgm(view)

        // Verifica se o aluno está registrado. Se registrado, verifica se possui disciplinas cadastradas
        verificarDadosAluno(view, rgm, senha)

        return view
    }

    private fun recuperarRgm(view: View) {
        val lembrarRgm: TextView = view.findViewById(R.id.btn_lembrarRgm)
        lembrarRgm.setOnClickListener() {
            val intentDisciplina = Intent(activity, RecuperarRgmActivity::class.java)
            startActivity(intentDisciplina)
        }
    }

    private fun verificarDadosAluno(
        view: View,
        rgm: EditText,
        senha: EditText
    ) {
        val db = AlunoDAO(view.context)
        val dbMateria = MateriaDAO(view.context)
        val btn_entrar: Button = view.findViewById(R.id.btn_entrar)
        btn_entrar.setOnClickListener() {
            val verifcaRgmSenha = db.verificarRegistroDoAluno(rgm.text.toString(), senha.text.toString())
            val intentDisciplina = Intent(activity, DisciplinaActivity::class.java)
            val intentNoDisciplina = Intent(activity, NoDisciplinaActivity::class.java)

            enviarParaActivityDisciplina(verifcaRgmSenha, db, rgm, intentDisciplina, dbMateria, intentNoDisciplina)

        }
    }

    private fun enviarParaActivityDisciplina(
        verifcaRgmSenha: Cursor,
        db: AlunoDAO,
        rgm: EditText,
        intentDisciplina: Intent,
        dbMateria: MateriaDAO,
        intentNoDisciplina: Intent
    ) {
        if (verifcaRgmSenha.moveToFirst()) {
            val nomeDoUsuario = db.buscarNomeDoAluno(rgm.text.toString())
            intentDisciplina.putExtra("userName", nomeDoUsuario.toString())
            intentDisciplina.putExtra("rgm", rgm.text.toString())
            intentNoDisciplina.putExtra("userName", nomeDoUsuario.toString())
            intentNoDisciplina.putExtra("rgm", rgm.text.toString())

            if (dbMateria.verificarDisciplinasCadastradas(rgm.text.toString()) != null){
                activity?.finish()
                startActivity(intentDisciplina)
            }else{
                activity?.finish()
                startActivity(intentNoDisciplina)
            }

        } else {
            AlertDialog.Builder(activity)
                .setTitle("Erro")
                .setMessage("Usuário ou senha inválidos")
                .setPositiveButton("Ok", null)
                .show()
        }
    }

    private fun declararVariaveis(view: View): Pair<EditText, EditText> {
        val rgm: EditText = view.findViewById(R.id.et_login_rgm)
        val senha: EditText = view.findViewById(R.id.et_login_password)
        return Pair(rgm, senha)
    }



}