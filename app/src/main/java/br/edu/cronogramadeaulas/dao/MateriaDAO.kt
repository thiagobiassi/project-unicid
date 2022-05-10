package br.edu.cronogramadeaulas.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import br.edu.cronogramadeaulas.dbhelper.DBHelperMaterias
import br.edu.cronogramadeaulas.dbhelper.DatabaseColums

class MateriaDAO {

    var sql : DBHelperMaterias? = null
    constructor(context: Context){
        sql = DBHelperMaterias(context)
    }

    // Verificar se o aluno já possui matérias cadastradas
    fun verificarDisciplinasCadastradas(rgm: String): String? {
        val db = sql!!.readableDatabase
        val cursor : Cursor =  db.rawQuery(
            "SELECT ${DatabaseColums.USER_RGM} FROM ${DatabaseColums.TABLE_NAME_MATERIAS} WHERE ${DatabaseColums.USER_RGM} =?", arrayOf(rgm))

        cursor.moveToFirst()
        if (cursor.count > 0){
            return cursor.getString(0)
        }else{
            return null
        }
    }

    // Adicionar matérias de Ciências da Computação no RGM mencionado no parâmetro
    fun adicionarMateriasCC(rgm : String): Long{
        val db = sql!!.writableDatabase
        val cv = ContentValues()

        // Inserindo no Banco
        cv.put(DatabaseColums.MATERIAS_ID_CC, "1") // 1 = ID do curso Ciências da Computação
        cv.put(DatabaseColums.USER_RGM, rgm)
        cv.put(DatabaseColums.MATERIA_FIA_CC, "FUNDAMENTOS DE INTELIGÊNCIA ARTIFICIAL")
        cv.put(DatabaseColums.MATERIA_TCC_CC, "TRABALHO DE GRADUAÇÃO INTERDISCIPLINAR I")
        cv.put(DatabaseColums.MATERIA_LFA_CC, "LINGUAGENS FORMAIS E AUTÔMATOS")
        cv.put(DatabaseColums.MATERIA_PDM_CC, "PROGRAMAÇÃO PARA DISPOSITIVOS MÓVEIS")

        return db.insert(DatabaseColums.TABLE_NAME_MATERIAS, null, cv)
    }

    fun verificarIdMaterias(rgm: String): String? {
        val db = sql!!.readableDatabase
        val cursor : Cursor = db.rawQuery("SELECT ${DatabaseColums.MATERIAS_ID_CC} FROM ${DatabaseColums.TABLE_NAME_MATERIAS} " +
                "WHERE ${DatabaseColums.USER_RGM} = ?", arrayOf(rgm))
        cursor.moveToFirst()

        if (cursor.count > 0){
            return cursor.getString(0)
        }else{
            return null
        }
    }

    fun buscarMateria(rgm: String): Cursor? {
        val db = sql!!.readableDatabase
        return db.rawQuery("SELECT ${DatabaseColums.MATERIA_TCC_CC}, " +
                "${DatabaseColums.MATERIA_PDM_CC}, " +
                "${DatabaseColums.MATERIA_FIA_CC}, " +
                "${DatabaseColums.MATERIA_LFA_CC} " +
                "FROM ${DatabaseColums.TABLE_NAME_MATERIAS} " +
                "WHERE ${DatabaseColums.USER_RGM} = ?", arrayOf(rgm))
    }

}