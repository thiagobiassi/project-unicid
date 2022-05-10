package br.edu.cronogramadeaulas.dbhelper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelperMaterias(context: Context?) :
    SQLiteOpenHelper(context, DATA_BASE, null, DATABASE_VERSION) {

    // Declarando nome do banco e versão
    companion object DbConstants{
        val DATA_BASE = "Materias.sqlite"
        val DATABASE_VERSION = 3
    }

    // Cria tabela das matérias
    val user_materias = "CREATE TABLE ${DatabaseColums.TABLE_NAME_MATERIAS} (${DatabaseColums.MATERIAS_ID_CC} TEXT, ${DatabaseColums.USER_RGM} TEXT, ${DatabaseColums.MATERIA_FIA_CC} TEXT, ${DatabaseColums.MATERIA_TCC_CC} TEXT, "+
            "${DatabaseColums.MATERIA_LFA_CC} TEXT, ${DatabaseColums.MATERIA_PDM_CC} TEXT)"

    // Exclui tabela das matérias
    val user_materias_drop = "DROP TABLE IF EXISTS ${DatabaseColums.TABLE_NAME_MATERIAS}"

    // Criando Tabela
    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(user_materias)
    }

    // Em caso de alteração de versão, recria a tabela com os mesmos dados
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL(user_materias_drop)
        onCreate(db)
    }

}