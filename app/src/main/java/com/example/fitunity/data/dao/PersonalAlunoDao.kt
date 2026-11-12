package com.example.fitunity.data.dao

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.fitunity.data.AlunoPersonal
import com.example.fitunity.data.DbContract.COLUMN_CONVERSA_ALUNO_ID
import com.example.fitunity.data.DbContract.COLUMN_CONVERSA_CRIADA_EM
import com.example.fitunity.data.DbContract.COLUMN_CONVERSA_HORARIO
import com.example.fitunity.data.DbContract.COLUMN_CONVERSA_ID
import com.example.fitunity.data.DbContract.COLUMN_CONVERSA_PERSONAL_ID
import com.example.fitunity.data.DbContract.COLUMN_CONVERSA_REMETENTE
import com.example.fitunity.data.DbContract.COLUMN_CONVERSA_TEXTO
import com.example.fitunity.data.DbContract.COLUMN_EMAIL
import com.example.fitunity.data.DbContract.COLUMN_FOCO
import com.example.fitunity.data.DbContract.COLUMN_FOTO_URI
import com.example.fitunity.data.DbContract.COLUMN_ID
import com.example.fitunity.data.DbContract.COLUMN_NIVEL
import com.example.fitunity.data.DbContract.COLUMN_NOME
import com.example.fitunity.data.DbContract.COLUMN_PERSONAL_ID
import com.example.fitunity.data.DbContract.COLUMN_PROGRESSO
import com.example.fitunity.data.DbContract.COLUMN_TIPO_PLANO
import com.example.fitunity.data.DbContract.COLUMN_USUARIO_ID
import com.example.fitunity.data.DbContract.REMETENTE_PERSONAL
import com.example.fitunity.data.DbContract.REMETENTE_USUARIO
import com.example.fitunity.data.DbContract.TABLE_CONVERSAS_PERSONAL_ALUNO
import com.example.fitunity.data.DbContract.TABLE_ESTATISTICAS
import com.example.fitunity.data.DbContract.TABLE_PERFIS_FISICOS
import com.example.fitunity.data.DbContract.TABLE_PERSONAL_ESCOLHIDO
import com.example.fitunity.data.DbContract.TABLE_USUARIOS
import com.example.fitunity.data.MensagemChat
import com.example.fitunity.data.PlanoNivel
import com.example.fitunity.data.nivelDoPlano
import java.util.Calendar

class PersonalAlunoDao(private val db: SQLiteDatabase) {

    fun listarAlunosElegiveis(personalId: Long): List<AlunoPersonal> {
        val cursor = db.rawQuery(
            """
            SELECT
                u.$COLUMN_ID, u.$COLUMN_NOME, u.$COLUMN_EMAIL, u.$COLUMN_FOTO_URI,
                p.$COLUMN_TIPO_PLANO, p.$COLUMN_FOCO, p.$COLUMN_NIVEL,
                e.$COLUMN_PROGRESSO
            FROM $TABLE_USUARIOS u
            INNER JOIN $TABLE_PERFIS_FISICOS p ON p.$COLUMN_USUARIO_ID = u.$COLUMN_ID
            INNER JOIN $TABLE_ESTATISTICAS e ON e.$COLUMN_USUARIO_ID = u.$COLUMN_ID
            INNER JOIN $TABLE_PERSONAL_ESCOLHIDO pe
                ON pe.$COLUMN_USUARIO_ID = u.$COLUMN_ID
                AND pe.$COLUMN_PERSONAL_ID = ?
            WHERE u.$COLUMN_ID != ?
            ORDER BY u.$COLUMN_NOME COLLATE NOCASE
            """.trimIndent(),
            arrayOf(personalId.toString(), personalId.toString())
        )
        return cursor.use {
            buildList {
                while (it.moveToNext()) add(it.paraAluno())
            }
        }.filter { nivelDoPlano(it.plano).ordem >= PlanoNivel.ADAPTADO.ordem }
    }

    fun buscarAlunoDoPersonal(personalId: Long, alunoId: Long): AlunoPersonal? {
        val cursor = db.rawQuery(
            """
            SELECT
                u.$COLUMN_ID, u.$COLUMN_NOME, u.$COLUMN_EMAIL, u.$COLUMN_FOTO_URI,
                p.$COLUMN_TIPO_PLANO, p.$COLUMN_FOCO, p.$COLUMN_NIVEL,
                e.$COLUMN_PROGRESSO
            FROM $TABLE_USUARIOS u
            INNER JOIN $TABLE_PERFIS_FISICOS p ON p.$COLUMN_USUARIO_ID = u.$COLUMN_ID
            INNER JOIN $TABLE_ESTATISTICAS e ON e.$COLUMN_USUARIO_ID = u.$COLUMN_ID
            INNER JOIN $TABLE_PERSONAL_ESCOLHIDO pe
                ON pe.$COLUMN_USUARIO_ID = u.$COLUMN_ID
                AND pe.$COLUMN_PERSONAL_ID = ?
            WHERE u.$COLUMN_ID = ?
            LIMIT 1
            """.trimIndent(),
            arrayOf(personalId.toString(), alunoId.toString())
        )
        return cursor.use { if (it.moveToFirst()) it.paraAluno() else null }
    }

    fun listarMensagens(personalId: Long, alunoId: Long): List<MensagemChat> {
        val cursor = db.query(
            TABLE_CONVERSAS_PERSONAL_ALUNO,
            null,
            "$COLUMN_CONVERSA_PERSONAL_ID = ? AND $COLUMN_CONVERSA_ALUNO_ID = ?",
            arrayOf(personalId.toString(), alunoId.toString()),
            null, null,
            "$COLUMN_CONVERSA_CRIADA_EM ASC, $COLUMN_CONVERSA_ID ASC"
        )
        return cursor.use {
            buildList {
                while (it.moveToNext()) add(it.paraMensagem())
            }
        }
    }

    fun listarMensagensDoAluno(alunoId: Long, personalId: Long): List<MensagemChat> {
        val cursor = db.query(
            TABLE_CONVERSAS_PERSONAL_ALUNO,
            null,
            "$COLUMN_CONVERSA_PERSONAL_ID = ? AND $COLUMN_CONVERSA_ALUNO_ID = ?",
            arrayOf(personalId.toString(), alunoId.toString()),
            null, null,
            "$COLUMN_CONVERSA_CRIADA_EM ASC, $COLUMN_CONVERSA_ID ASC"
        )
        return cursor.use {
            buildList {
                while (it.moveToNext()) add(it.paraMensagemDoAluno())
            }
        }
    }

    fun ultimaMensagem(personalId: Long, alunoId: Long): MensagemChat? {
        val cursor = db.query(
            TABLE_CONVERSAS_PERSONAL_ALUNO,
            null,
            "$COLUMN_CONVERSA_PERSONAL_ID = ? AND $COLUMN_CONVERSA_ALUNO_ID = ?",
            arrayOf(personalId.toString(), alunoId.toString()),
            null, null,
            "$COLUMN_CONVERSA_CRIADA_EM DESC, $COLUMN_CONVERSA_ID DESC",
            "1"
        )
        return cursor.use { if (it.moveToFirst()) it.paraMensagem() else null }
    }

    fun enviarMensagem(personalId: Long, alunoId: Long, texto: String): MensagemChat? {
        val conteudo = texto.trim()
        if (conteudo.isEmpty()) return null
        val horario = horarioAtual()
        val id = db.insertOrThrow(
            TABLE_CONVERSAS_PERSONAL_ALUNO,
            null,
            ContentValues().apply {
                put(COLUMN_CONVERSA_PERSONAL_ID, personalId)
                put(COLUMN_CONVERSA_ALUNO_ID, alunoId)
                put(COLUMN_CONVERSA_REMETENTE, REMETENTE_PERSONAL)
                put(COLUMN_CONVERSA_TEXTO, conteudo)
                put(COLUMN_CONVERSA_HORARIO, horario)
                put(COLUMN_CONVERSA_CRIADA_EM, System.currentTimeMillis())
            }
        )
        return MensagemChat(
            id = id,
            texto = conteudo,
            enviadaPorMim = true,
            horario = horario
        )
    }

    fun enviarMensagemDoAluno(alunoId: Long, personalId: Long, texto: String): MensagemChat? {
        val conteudo = texto.trim()
        if (conteudo.isEmpty()) return null
        val horario = horarioAtual()
        val id = db.insertOrThrow(
            TABLE_CONVERSAS_PERSONAL_ALUNO,
            null,
            ContentValues().apply {
                put(COLUMN_CONVERSA_PERSONAL_ID, personalId)
                put(COLUMN_CONVERSA_ALUNO_ID, alunoId)
                put(COLUMN_CONVERSA_REMETENTE, REMETENTE_USUARIO)
                put(COLUMN_CONVERSA_TEXTO, conteudo)
                put(COLUMN_CONVERSA_HORARIO, horario)
                put(COLUMN_CONVERSA_CRIADA_EM, System.currentTimeMillis())
            }
        )
        return MensagemChat(
            id = id,
            texto = conteudo,
            enviadaPorMim = true,
            horario = horario
        )
    }

    fun ultimaMensagemDoAluno(alunoId: Long, personalId: Long): MensagemChat? {
        val cursor = db.query(
            TABLE_CONVERSAS_PERSONAL_ALUNO,
            null,
            "$COLUMN_CONVERSA_PERSONAL_ID = ? AND $COLUMN_CONVERSA_ALUNO_ID = ?",
            arrayOf(personalId.toString(), alunoId.toString()),
            null, null,
            "$COLUMN_CONVERSA_CRIADA_EM DESC, $COLUMN_CONVERSA_ID DESC",
            "1"
        )
        return cursor.use { if (it.moveToFirst()) it.paraMensagemDoAluno() else null }
    }

    private fun Cursor.paraAluno(): AlunoPersonal = AlunoPersonal(
        id = getLong(getColumnIndexOrThrow(COLUMN_ID)),
        nome = getString(getColumnIndexOrThrow(COLUMN_NOME)),
        email = getString(getColumnIndexOrThrow(COLUMN_EMAIL)),
        fotoUri = getString(getColumnIndexOrThrow(COLUMN_FOTO_URI)),
        plano = getString(getColumnIndexOrThrow(COLUMN_TIPO_PLANO)),
        foco = getString(getColumnIndexOrThrow(COLUMN_FOCO)),
        nivel = getString(getColumnIndexOrThrow(COLUMN_NIVEL)),
        progresso = getDouble(getColumnIndexOrThrow(COLUMN_PROGRESSO))
    )

    private fun Cursor.paraMensagem(): MensagemChat = MensagemChat(
        id = getLong(getColumnIndexOrThrow(COLUMN_CONVERSA_ID)),
        texto = getString(getColumnIndexOrThrow(COLUMN_CONVERSA_TEXTO)),
        enviadaPorMim = getString(getColumnIndexOrThrow(COLUMN_CONVERSA_REMETENTE)) == REMETENTE_PERSONAL,
        horario = getString(getColumnIndexOrThrow(COLUMN_CONVERSA_HORARIO))
    )

    private fun Cursor.paraMensagemDoAluno(): MensagemChat = MensagemChat(
        id = getLong(getColumnIndexOrThrow(COLUMN_CONVERSA_ID)),
        texto = getString(getColumnIndexOrThrow(COLUMN_CONVERSA_TEXTO)),
        enviadaPorMim = getString(getColumnIndexOrThrow(COLUMN_CONVERSA_REMETENTE)) == REMETENTE_USUARIO,
        horario = getString(getColumnIndexOrThrow(COLUMN_CONVERSA_HORARIO))
    )

    private fun horarioAtual(): String {
        val calendario = Calendar.getInstance()
        return "%02d:%02d".format(
            calendario.get(Calendar.HOUR_OF_DAY),
            calendario.get(Calendar.MINUTE)
        )
    }
}
