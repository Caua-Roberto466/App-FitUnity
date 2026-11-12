package com.example.fitunity.data.dao

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.fitunity.data.CandidaturaPersonal
import com.example.fitunity.data.DbContract.COLUMN_CANDIDATURA_APRESENTACAO
import com.example.fitunity.data.DbContract.COLUMN_CANDIDATURA_ATUALIZADA_EM
import com.example.fitunity.data.DbContract.COLUMN_CANDIDATURA_CREF
import com.example.fitunity.data.DbContract.COLUMN_CANDIDATURA_CRIADA_EM
import com.example.fitunity.data.DbContract.COLUMN_CANDIDATURA_CURRICULO_NOME
import com.example.fitunity.data.DbContract.COLUMN_CANDIDATURA_CURRICULO_URI
import com.example.fitunity.data.DbContract.COLUMN_CANDIDATURA_EMAIL
import com.example.fitunity.data.DbContract.COLUMN_CANDIDATURA_ESPECIALIDADES
import com.example.fitunity.data.DbContract.COLUMN_CANDIDATURA_EXPERIENCIA
import com.example.fitunity.data.DbContract.COLUMN_CANDIDATURA_ID
import com.example.fitunity.data.DbContract.COLUMN_CANDIDATURA_NOME
import com.example.fitunity.data.DbContract.COLUMN_CANDIDATURA_STATUS
import com.example.fitunity.data.DbContract.COLUMN_CANDIDATURA_TELEFONE
import com.example.fitunity.data.DbContract.COLUMN_FOTO_URI
import com.example.fitunity.data.DbContract.COLUMN_ID
import com.example.fitunity.data.DbContract.COLUMN_USUARIO_ID
import com.example.fitunity.data.DbContract.TABLE_CANDIDATURAS_PERSONAL
import com.example.fitunity.data.DbContract.TABLE_USUARIOS
import com.example.fitunity.data.StatusCandidatura

class CandidaturaDao(private val db: SQLiteDatabase) {

    fun buscarPorUsuario(usuarioId: Long): CandidaturaPersonal? {
        val cursor = db.rawQuery(
            """
            SELECT c.*, u.$COLUMN_FOTO_URI AS $COLUMN_FOTO_URI
            FROM $TABLE_CANDIDATURAS_PERSONAL c
            LEFT JOIN $TABLE_USUARIOS u ON u.$COLUMN_ID = c.$COLUMN_USUARIO_ID
            WHERE c.$COLUMN_USUARIO_ID = ?
            LIMIT 1
            """.trimIndent(),
            arrayOf(usuarioId.toString())
        )
        return cursor.use { if (it.moveToFirst()) it.paraCandidatura() else null }
    }

    fun listarAprovados(): List<CandidaturaPersonal> {
        val cursor = db.rawQuery(
            """
            SELECT c.*, u.$COLUMN_FOTO_URI AS $COLUMN_FOTO_URI
            FROM $TABLE_CANDIDATURAS_PERSONAL c
            LEFT JOIN $TABLE_USUARIOS u ON u.$COLUMN_ID = c.$COLUMN_USUARIO_ID
            WHERE c.$COLUMN_CANDIDATURA_STATUS = ?
            ORDER BY c.$COLUMN_CANDIDATURA_NOME COLLATE NOCASE ASC
            """.trimIndent(),
            arrayOf(StatusCandidatura.APROVADO.name)
        )
        return cursor.use {
            buildList {
                while (it.moveToNext()) add(it.paraCandidatura())
            }
        }
    }

    fun buscarAprovado(usuarioId: Long): CandidaturaPersonal? {
        val cursor = db.rawQuery(
            """
            SELECT c.*, u.$COLUMN_FOTO_URI AS $COLUMN_FOTO_URI
            FROM $TABLE_CANDIDATURAS_PERSONAL c
            LEFT JOIN $TABLE_USUARIOS u ON u.$COLUMN_ID = c.$COLUMN_USUARIO_ID
            WHERE c.$COLUMN_USUARIO_ID = ? AND c.$COLUMN_CANDIDATURA_STATUS = ?
            LIMIT 1
            """.trimIndent(),
            arrayOf(usuarioId.toString(), StatusCandidatura.APROVADO.name)
        )
        return cursor.use { if (it.moveToFirst()) it.paraCandidatura() else null }
    }

    fun salvar(candidatura: CandidaturaPersonal): CandidaturaPersonal {
        val agora = System.currentTimeMillis()
        val valores = ContentValues().apply {
            put(COLUMN_USUARIO_ID, candidatura.usuarioId)
            put(COLUMN_CANDIDATURA_NOME, candidatura.nome.trim())
            put(COLUMN_CANDIDATURA_EMAIL, candidatura.email.trim().lowercase())
            put(COLUMN_CANDIDATURA_TELEFONE, candidatura.telefone.trim())
            put(COLUMN_CANDIDATURA_CREF, candidatura.cref.trim())
            put(COLUMN_CANDIDATURA_ESPECIALIDADES, candidatura.especialidades.joinToString(SEPARADOR))
            put(COLUMN_CANDIDATURA_EXPERIENCIA, candidatura.experiencia.trim())
            put(COLUMN_CANDIDATURA_APRESENTACAO, candidatura.apresentacao.trim())
            put(COLUMN_CANDIDATURA_CURRICULO_URI, candidatura.curriculoUri)
            put(COLUMN_CANDIDATURA_CURRICULO_NOME, candidatura.curriculoNome)
            put(COLUMN_CANDIDATURA_STATUS, candidatura.status.name)
            put(COLUMN_CANDIDATURA_ATUALIZADA_EM, agora)
        }

        val existente = buscarPorUsuario(candidatura.usuarioId)
        if (existente == null) {
            valores.put(COLUMN_CANDIDATURA_CRIADA_EM, agora)
            db.insertOrThrow(TABLE_CANDIDATURAS_PERSONAL, null, valores)
        } else {
            db.update(
                TABLE_CANDIDATURAS_PERSONAL,
                valores,
                "$COLUMN_USUARIO_ID = ?",
                arrayOf(candidatura.usuarioId.toString())
            )
        }
        return buscarPorUsuario(candidatura.usuarioId) ?: candidatura.copy(
            criadaEm = agora,
            atualizadaEm = agora
        )
    }

    fun atualizarStatus(usuarioId: Long, status: StatusCandidatura): Boolean =
        db.update(
            TABLE_CANDIDATURAS_PERSONAL,
            ContentValues().apply {
                put(COLUMN_CANDIDATURA_STATUS, status.name)
                put(COLUMN_CANDIDATURA_ATUALIZADA_EM, System.currentTimeMillis())
            },
            "$COLUMN_USUARIO_ID = ?",
            arrayOf(usuarioId.toString())
        ) > 0

    fun remover(usuarioId: Long): Boolean =
        db.delete(
            TABLE_CANDIDATURAS_PERSONAL,
            "$COLUMN_USUARIO_ID = ?",
            arrayOf(usuarioId.toString())
        ) > 0

    internal fun Cursor.paraCandidatura(): CandidaturaPersonal = CandidaturaPersonal(
        id = getLong(getColumnIndexOrThrow(COLUMN_CANDIDATURA_ID)),
        usuarioId = getLong(getColumnIndexOrThrow(COLUMN_USUARIO_ID)),
        nome = getString(getColumnIndexOrThrow(COLUMN_CANDIDATURA_NOME)),
        email = getString(getColumnIndexOrThrow(COLUMN_CANDIDATURA_EMAIL)),
        fotoUri = getString(getColumnIndexOrThrow(COLUMN_FOTO_URI)),
        telefone = getString(getColumnIndexOrThrow(COLUMN_CANDIDATURA_TELEFONE)),
        cref = getString(getColumnIndexOrThrow(COLUMN_CANDIDATURA_CREF)),
        especialidades = getString(getColumnIndexOrThrow(COLUMN_CANDIDATURA_ESPECIALIDADES))
            .split(SEPARADOR)
            .filter { it.isNotBlank() },
        experiencia = getString(getColumnIndexOrThrow(COLUMN_CANDIDATURA_EXPERIENCIA)),
        apresentacao = getString(getColumnIndexOrThrow(COLUMN_CANDIDATURA_APRESENTACAO)),
        curriculoUri = getString(getColumnIndexOrThrow(COLUMN_CANDIDATURA_CURRICULO_URI)),
        curriculoNome = getString(getColumnIndexOrThrow(COLUMN_CANDIDATURA_CURRICULO_NOME)),
        status = StatusCandidatura.de(getString(getColumnIndexOrThrow(COLUMN_CANDIDATURA_STATUS))),
        criadaEm = getLong(getColumnIndexOrThrow(COLUMN_CANDIDATURA_CRIADA_EM)),
        atualizadaEm = getLong(getColumnIndexOrThrow(COLUMN_CANDIDATURA_ATUALIZADA_EM))
    )

    private companion object {
        const val SEPARADOR = "|"
    }
}
