package br.edu.cronogramadeaulas.dbhelper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelperUsers(context: Context?) :
    SQLiteOpenHelper(context, DATA_BASE, null, DATABASE_VERSION) {

    // Declarando nome do banco e versão
    companion object DbConstants{
        val DATA_BASE = "LoginUser.sqlite"
        val DATABASE_VERSION = 2
    }

    // Cria tabela dos alunos
    val user_table = "CREATE TABLE ${DatabaseColums.TABLE_NAME} (${DatabaseColums.USER_RGM} TEXT, ${DatabaseColums.USER_NAME} TEXT, "+
            "${DatabaseColums.USER_EMAIL} TEXT, ${DatabaseColums.USER_PASSWORD} TEXT)"

    // Exclui tabela dos alunos
    val user_drop = "DROP TABLE IF EXISTS ${DatabaseColums.TABLE_NAME}"

    // Criando Tabela
    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(user_table)
    }

    // Em caso de alteração de versão, recria a tabela com os mesmos dados
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL(user_drop)
        onCreate(db)
    }



}