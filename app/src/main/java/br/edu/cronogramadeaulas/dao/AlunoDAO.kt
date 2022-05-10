package br.edu.cronogramadeaulas.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import br.edu.cronogramadeaulas.dbhelper.DBHelperUsers
import br.edu.cronogramadeaulas.dbhelper.DatabaseColums
import br.edu.cronogramadeaulas.model.Aluno

class AlunoDAO {

    var sql : DBHelperUsers? = null
    constructor(context: Context){
       sql = DBHelperUsers(context)
    }

    fun adicionarUsuario(rgm:String, name:String, email:String, password:String): Long{
        val db = sql!!.writableDatabase
        val cv = ContentValues()
        cv.put(DatabaseColums.USER_RGM, rgm)
        cv.put(DatabaseColums.USER_NAME, name)
        cv.put(DatabaseColums.USER_EMAIL, email)
        cv.put(DatabaseColums.USER_PASSWORD, password)
        return db.insert(DatabaseColums.TABLE_NAME, null, cv)
    }

    fun verificarRegistroDoAluno(rgm: String, password: String):Cursor{
        val db = sql!!.readableDatabase
        return db.query(
            DatabaseColums.TABLE_NAME, null, DatabaseColums.USER_RGM +"=? AND "+ DatabaseColums.USER_PASSWORD + "=?",
            arrayOf(rgm, password), null,null,null)
    }

    fun buscarNomeDoAluno(rgm: String): String? {
        val aluno = Aluno(rgm)
        val db = sql!!.readableDatabase
        val cursor : Cursor = db.rawQuery("SELECT ${DatabaseColums.USER_NAME} FROM ${DatabaseColums.TABLE_NAME} WHERE ${DatabaseColums.USER_RGM} = ?", arrayOf(rgm))
        cursor.moveToFirst()

        if (cursor.count > 0){
            aluno.nome = cursor.getString(0)
            return aluno.nome.toString()
        }else{
            return null
        }
    }

    fun recuperarRgm(email: String): String? {
        val db = sql!!.readableDatabase
        val cursor : Cursor = db.rawQuery("SELECT ${DatabaseColums.USER_RGM} FROM ${DatabaseColums.TABLE_NAME} " +
                "WHERE ${DatabaseColums.USER_EMAIL} = ?", arrayOf(email))
        cursor.moveToFirst()

        if (cursor.count > 0){
            return cursor.getString(0)
        }else{
            return null
        }
    }

    fun verificarEmailExistente(email: String): String? {
        val db = sql!!.readableDatabase
        val cursor : Cursor = db.rawQuery("SELECT ${DatabaseColums.USER_EMAIL} FROM ${DatabaseColums.TABLE_NAME} " +
                "WHERE ${DatabaseColums.USER_EMAIL} = ?", arrayOf(email))
        cursor.moveToFirst()

        if (cursor.count > 0){
            return cursor.getString(0)
        }else{
            return null
        }
    }

}