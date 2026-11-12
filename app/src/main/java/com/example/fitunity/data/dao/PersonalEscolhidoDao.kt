package com.example.fitunity.data.dao

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.example.fitunity.data.CandidaturaPersonal
import com.example.fitunity.data.DbContract.COLUMN_ESCOLHIDO_EM
import com.example.fitunity.data.DbContract.COLUMN_PERSONAL_ID
import com.example.fitunity.data.DbContract.COLUMN_USUARIO_ID
import com.example.fitunity.data.DbContract.TABLE_PERSONAL_ESCOLHIDO

class PersonalEscolhidoDao(private val db: SQLiteDatabase) {

    fun personalIdEscolhido(alunoId: Long): Long? {
        val cursor = db.query(
            TABLE_PERSONAL_ESCOLHIDO,
            arrayOf(COLUMN_PERSONAL_ID),
            "$COLUMN_USUARIO_ID = ?",
            arrayOf(alunoId.toString()),
            null, null, null
        )
        return cursor.use {
            if (it.moveToFirst()) it.getLong(it.getColumnIndexOrThrow(COLUMN_PERSONAL_ID)) else null
        }
    }

    fun buscarPersonalEscolhido(alunoId: Long): CandidaturaPersonal? {
        val personalId = personalIdEscolhido(alunoId) ?: return null
        return CandidaturaDao(db).buscarAprovado(personalId)
    }

    fun escolher(alunoId: Long, personalId: Long): Boolean {
        if (alunoId == personalId) return false
        val personal = CandidaturaDao(db).buscarAprovado(personalId) ?: return false
        if (personal.usuarioId != personalId) return false
        db.insertWithOnConflict(
            TABLE_PERSONAL_ESCOLHIDO,
            null,
            ContentValues().apply {
                put(COLUMN_USUARIO_ID, alunoId)
                put(COLUMN_PERSONAL_ID, personalId)
                put(COLUMN_ESCOLHIDO_EM, System.currentTimeMillis())
            },
            SQLiteDatabase.CONFLICT_REPLACE
        )
        return true
    }

    fun remover(alunoId: Long): Boolean =
        db.delete(
            TABLE_PERSONAL_ESCOLHIDO,
            "$COLUMN_USUARIO_ID = ?",
            arrayOf(alunoId.toString())
        ) > 0

    fun contarAlunos(personalId: Long): Int {
        val cursor = db.rawQuery(
            "SELECT COUNT(*) FROM $TABLE_PERSONAL_ESCOLHIDO WHERE $COLUMN_PERSONAL_ID = ?",
            arrayOf(personalId.toString())
        )
        return cursor.use { if (it.moveToFirst()) it.getInt(0) else 0 }
    }

    fun alunoEscolheu(alunoId: Long, personalId: Long): Boolean {
        val cursor = db.rawQuery(
            """
            SELECT 1 FROM $TABLE_PERSONAL_ESCOLHIDO
            WHERE $COLUMN_USUARIO_ID = ? AND $COLUMN_PERSONAL_ID = ?
            LIMIT 1
            """.trimIndent(),
            arrayOf(alunoId.toString(), personalId.toString())
        )
        return cursor.use { it.moveToFirst() }
    }
}
