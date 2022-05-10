package br.edu.cronogramadeaulas.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import br.edu.cronogramadeaulas.R
import br.edu.cronogramadeaulas.dao.AlunoDAO
import br.edu.cronogramadeaulas.ui.MainActivity
import java.util.*

class RegisterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        // Declarando variáveis do Layout
        val et_name: EditText = view.findViewById(R.id.et_name)
        val et_email: EditText = view.findViewById(R.id.et_email)
        val et_password: EditText = view.findViewById(R.id.et_password)
        val et_repassword: EditText = view.findViewById(R.id.et_repassword)

        // Registrando Aluno
        registrarAluno(view, et_name, et_email, et_password, et_repassword)

        return view

    }

    private fun registrarAluno(
        view: View,
        et_name: EditText,
        et_email: EditText,
        et_password: EditText,
        et_repassword: EditText

    ) {
        val btn_register: Button = view.findViewById(R.id.btn_register)
        btn_register.setOnClickListener() {

            var dadosValidos = true

            val db = AlunoDAO(view.context)
            if (db.verificarEmailExistente(et_email.text.toString()) != null){
                et_email.setError("E-mail já cadastrado"); et_email.requestFocus(); dadosValidos = false
            }
            // Se estvier vazio retorna erro ou se não estiver igual à primeira senha digitada
            if (TextUtils.isEmpty(et_repassword.text.toString())){
                et_repassword.setError("Confirme a senha"); et_repassword.requestFocus(); dadosValidos = false
            }

            // Se as senhas não são iguais
            if (!et_repassword.text.toString().equals(et_password.text.toString())){
                et_repassword.setError("As senhas não conferem"); et_repassword.requestFocus(); dadosValidos = false
            }

            // Se estvier vazio retorna erro
            if (TextUtils.isEmpty(et_password.text.toString())){
                et_password.setError("Digite uma senha"); et_password.requestFocus(); dadosValidos = false
            }

            // Se estvier vazio retorna erro ou se não constar "@" retorna
            if (TextUtils.isEmpty(et_email.text.toString()) || !et_email.text.toString().contains("@")){
                et_email.setError("Digite um e-mail válido"); et_email.requestFocus(); dadosValidos = false
            }

            // Se estvier vazio retorna erro
            if (TextUtils.isEmpty(et_name.text.toString())){
                et_name.setError("Digite o nome completo"); et_name.requestFocus()
            }

            else if (dadosValidos) {
                val rgm = gerarRgmAluno()
                val db = AlunoDAO(view.context)
                val res = db.adicionarUsuario(rgm, et_name.text.toString(), et_email.text.toString(), et_password.text.toString())

                val intentLogin = Intent(activity, MainActivity::class.java)

                if (res > 0) {
                    AlertDialog.Builder(view.context)
                        .setTitle("Cadastrado")
                        .setMessage("Registrado com sucesso \nSeu RGM: " + rgm)
                        .setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, i ->
                            activity?.finish()
                            startActivity(intentLogin)
                        })
                        .show()
                } else {
                    AlertDialog.Builder(view.context)
                        .setTitle("Alerta")
                        .setMessage("Algo está errado, tente novamente")
                        .setPositiveButton("Ok", null)
                        .show()
                }
            }
        }
    }

    private fun gerarRgmAluno(): String {
        val numRandom = Random().nextInt(999999)
        val rgm = "20$numRandom"
        return rgm
    }
}