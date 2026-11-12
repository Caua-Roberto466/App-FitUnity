package com.example.fitunity.data.dao

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.fitunity.data.DatabaseSeeder
import com.example.fitunity.data.DbContract.COLUMN_HISTORICO_CALORIAS
import com.example.fitunity.data.DbContract.COLUMN_HISTORICO_DATA
import com.example.fitunity.data.DbContract.COLUMN_HISTORICO_DURACAO
import com.example.fitunity.data.DbContract.COLUMN_HISTORICO_ID
import com.example.fitunity.data.DbContract.COLUMN_HISTORICO_TITULO
import com.example.fitunity.data.DbContract.COLUMN_TREINOS_REALIZADOS
import com.example.fitunity.data.DbContract.COLUMN_USUARIO_ID
import com.example.fitunity.data.DbContract.TABLE_ESTATISTICAS
import com.example.fitunity.data.DbContract.TABLE_HISTORICO
import com.example.fitunity.data.HistoricoTreino

class HistoricoDao(private val db: SQLiteDatabase) {

    fun listarHistorico(usuarioId: Long): List<HistoricoTreino> {
        val cursor = db.query(
            TABLE_HISTORICO,
            null,
            "$COLUMN_USUARIO_ID = ?",
            arrayOf(usuarioId.toString()),
            null, null, "$COLUMN_HISTORICO_ID DESC"
        )
        return cursor.use {
            buildList {
                while (it.moveToNext()) add(it.paraHistorico())
            }
        }
    }

    fun registrarTreinoConcluido(
        usuarioId: Long,
        titulo: String,
        duracao: String,
        calorias: String,
        data: String = "Hoje"
    ): Long {
        DatabaseSeeder.garantirLinhasPerfil(db, usuarioId)
        db.beginTransaction()
        return try {
            val id = db.insertOrThrow(
                TABLE_HISTORICO,
                null,
                ContentValues().apply {
                    put(COLUMN_USUARIO_ID, usuarioId)
                    put(COLUMN_HISTORICO_DATA, data)
                    put(COLUMN_HISTORICO_TITULO, titulo)
                    put(COLUMN_HISTORICO_DURACAO, duracao)
                    put(COLUMN_HISTORICO_CALORIAS, calorias)
                }
            )
            db.execSQL(
                "UPDATE $TABLE_ESTATISTICAS SET $COLUMN_TREINOS_REALIZADOS = $COLUMN_TREINOS_REALIZADOS + 1 WHERE $COLUMN_USUARIO_ID = ?",
                arrayOf(usuarioId)
            )
            db.setTransactionSuccessful()
            id
        } finally {
            db.endTransaction()
        }
    }

    private fun Cursor.paraHistorico(): HistoricoTreino = HistoricoTreino(
        id = getLong(getColumnIndexOrThrow(COLUMN_HISTORICO_ID)),
        data = getString(getColumnIndexOrThrow(COLUMN_HISTORICO_DATA)),
        titulo = getString(getColumnIndexOrThrow(COLUMN_HISTORICO_TITULO)),
        duracao = getString(getColumnIndexOrThrow(COLUMN_HISTORICO_DURACAO)),
        calorias = getString(getColumnIndexOrThrow(COLUMN_HISTORICO_CALORIAS))
    )
}
