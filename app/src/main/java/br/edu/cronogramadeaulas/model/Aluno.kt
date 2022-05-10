package br.edu.cronogramadeaulas.model

class Aluno {

    var nome : String? = null
    var rgm : String? = null
    var email : String? = null
    var senha : String? = null

    constructor(nome : String, rgm : String, email : String, senha : String){
        this.nome = nome
        this.rgm = rgm
        this.email = email
        this.senha = senha
    }

    constructor(rgm : String){
        this.rgm = rgm
    }

    override fun toString(): String {
        return "$nome"
    }


}