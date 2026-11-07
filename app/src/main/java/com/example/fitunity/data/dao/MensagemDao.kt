package com.example.fitunity.data.dao

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.fitunity.data.DbContract.COLUMN_MENSAGEM_CRIADA_EM
import com.example.fitunity.data.DbContract.COLUMN_MENSAGEM_HORARIO
import com.example.fitunity.data.DbContract.COLUMN_MENSAGEM_ID
import com.example.fitunity.data.DbContract.COLUMN_MENSAGEM_REMETENTE
import com.example.fitunity.data.DbContract.COLUMN_MENSAGEM_TEXTO
import com.example.fitunity.data.DbContract.COLUMN_USUARIO_ID
import com.example.fitunity.data.DbContract.REMETENTE_USUARIO
import com.example.fitunity.data.DbContract.TABLE_MENSAGENS_PERSONAL
import com.example.fitunity.data.MensagemChat
import java.util.Calendar

class MensagemDao(private val db: SQLiteDatabase) {

    fun listarMensagens(usuarioId: Long): List<MensagemChat> {
        val cursor = db.query(
            TABLE_MENSAGENS_PERSONAL,
            null,
            "$COLUMN_USUARIO_ID = ?",
            arrayOf(usuarioId.toString()),
            null, null, "$COLUMN_MENSAGEM_CRIADA_EM ASC, $COLUMN_MENSAGEM_ID ASC"
        )
        return cursor.use {
            buildList {
                while (it.moveToNext()) add(it.paraMensagem())
            }
        }
    }

    fun enviarMensagem(usuarioId: Long, texto: String): MensagemChat? {
        val conteudo = texto.trim()
        if (conteudo.isEmpty()) return null
        val horario = horarioAtual()
        val id = db.insertOrThrow(
            TABLE_MENSAGENS_PERSONAL,
            null,
            ContentValues().apply {
                put(COLUMN_USUARIO_ID, usuarioId)
                put(COLUMN_MENSAGEM_REMETENTE, REMETENTE_USUARIO)
                put(COLUMN_MENSAGEM_TEXTO, conteudo)
                put(COLUMN_MENSAGEM_HORARIO, horario)
                put(COLUMN_MENSAGEM_CRIADA_EM, System.currentTimeMillis())
            }
        )
        return MensagemChat(
            id = id,
            texto = conteudo,
            enviadaPorMim = true,
            horario = horario
        )
    }

    private fun Cursor.paraMensagem(): MensagemChat = MensagemChat(
        id = getLong(getColumnIndexOrThrow(COLUMN_MENSAGEM_ID)),
        texto = getString(getColumnIndexOrThrow(COLUMN_MENSAGEM_TEXTO)),
        enviadaPorMim = getString(getColumnIndexOrThrow(COLUMN_MENSAGEM_REMETENTE)) == REMETENTE_USUARIO,
        horario = getString(getColumnIndexOrThrow(COLUMN_MENSAGEM_HORARIO))
    )

    private fun horarioAtual(): String {
        val calendario = Calendar.getInstance()
        return "%02d:%02d".format(
            calendario.get(Calendar.HOUR_OF_DAY),
            calendario.get(Calendar.MINUTE)
        )
    }
}
