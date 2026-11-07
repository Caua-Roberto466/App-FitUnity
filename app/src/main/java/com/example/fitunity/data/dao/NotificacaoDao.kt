package com.example.fitunity.data.dao

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.fitunity.data.DbContract.COLUMN_NOTIFICACAO_HORARIO
import com.example.fitunity.data.DbContract.COLUMN_NOTIFICACAO_ID
import com.example.fitunity.data.DbContract.COLUMN_NOTIFICACAO_LIDA
import com.example.fitunity.data.DbContract.COLUMN_NOTIFICACAO_MENSAGEM
import com.example.fitunity.data.DbContract.COLUMN_NOTIFICACAO_TIPO
import com.example.fitunity.data.DbContract.COLUMN_NOTIFICACAO_TITULO
import com.example.fitunity.data.DbContract.COLUMN_USUARIO_ID
import com.example.fitunity.data.DbContract.TABLE_NOTIFICACOES
import com.example.fitunity.data.Notificacao
import com.example.fitunity.data.TipoNotificacao

class NotificacaoDao(private val db: SQLiteDatabase) {

    fun listarNotificacoes(usuarioId: Long): List<Notificacao> {
        val cursor = db.query(
            TABLE_NOTIFICACOES,
            null,
            "$COLUMN_USUARIO_ID = ?",
            arrayOf(usuarioId.toString()),
            null, null, "$COLUMN_NOTIFICACAO_ID ASC"
        )
        return cursor.use {
            buildList {
                while (it.moveToNext()) add(it.paraNotificacao())
            }
        }
    }

    fun marcarNotificacaoLida(usuarioId: Long, notificacaoId: Long): Boolean =
        db.update(
            TABLE_NOTIFICACOES,
            ContentValues().apply { put(COLUMN_NOTIFICACAO_LIDA, 1) },
            "$COLUMN_NOTIFICACAO_ID = ? AND $COLUMN_USUARIO_ID = ?",
            arrayOf(notificacaoId.toString(), usuarioId.toString())
        ) > 0

    private fun Cursor.paraNotificacao(): Notificacao = Notificacao(
        id = getLong(getColumnIndexOrThrow(COLUMN_NOTIFICACAO_ID)),
        titulo = getString(getColumnIndexOrThrow(COLUMN_NOTIFICACAO_TITULO)),
        mensagem = getString(getColumnIndexOrThrow(COLUMN_NOTIFICACAO_MENSAGEM)),
        horario = getString(getColumnIndexOrThrow(COLUMN_NOTIFICACAO_HORARIO)),
        tipo = runCatching {
            TipoNotificacao.valueOf(getString(getColumnIndexOrThrow(COLUMN_NOTIFICACAO_TIPO)))
        }.getOrDefault(TipoNotificacao.TREINO),
        lida = getInt(getColumnIndexOrThrow(COLUMN_NOTIFICACAO_LIDA)) == 1
    )
}
