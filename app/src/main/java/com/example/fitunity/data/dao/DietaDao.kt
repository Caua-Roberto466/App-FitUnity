package com.example.fitunity.data.dao

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.fitunity.data.DbContract.COLUMN_REFEICAO_CALORIAS
import com.example.fitunity.data.DbContract.COLUMN_REFEICAO_CONCLUIDA
import com.example.fitunity.data.DbContract.COLUMN_REFEICAO_DIA
import com.example.fitunity.data.DbContract.COLUMN_REFEICAO_HORARIO
import com.example.fitunity.data.DbContract.COLUMN_REFEICAO_ID
import com.example.fitunity.data.DbContract.COLUMN_REFEICAO_IMAGEM
import com.example.fitunity.data.DbContract.COLUMN_REFEICAO_INGREDIENTES
import com.example.fitunity.data.DbContract.COLUMN_REFEICAO_INSTRUCOES
import com.example.fitunity.data.DbContract.COLUMN_REFEICAO_PERIODO
import com.example.fitunity.data.DbContract.COLUMN_REFEICAO_TITULO
import com.example.fitunity.data.DbContract.COLUMN_USUARIO_ID
import com.example.fitunity.data.DbContract.TABLE_REFEICOES
import com.example.fitunity.data.Ingrediente
import com.example.fitunity.data.RefeicaoDia

class DietaDao(private val db: SQLiteDatabase) {

    fun listarRefeicoes(usuarioId: Long): List<RefeicaoDia> {
        val cursor = db.query(
            TABLE_REFEICOES,
            null,
            "$COLUMN_USUARIO_ID = ?",
            arrayOf(usuarioId.toString()),
            null, null, "$COLUMN_REFEICAO_ID ASC"
        )
        return cursor.use {
            buildList {
                while (it.moveToNext()) add(it.paraRefeicao())
            }
        }
    }

    fun buscarRefeicao(usuarioId: Long, refeicaoId: Long): RefeicaoDia? {
        val cursor = db.query(
            TABLE_REFEICOES,
            null,
            "$COLUMN_REFEICAO_ID = ? AND $COLUMN_USUARIO_ID = ?",
            arrayOf(refeicaoId.toString(), usuarioId.toString()),
            null, null, null
        )
        return cursor.use { if (it.moveToFirst()) it.paraRefeicao() else null }
    }

    fun concluirRefeicao(usuarioId: Long, refeicaoId: Long): Boolean =
        db.update(
            TABLE_REFEICOES,
            ContentValues().apply { put(COLUMN_REFEICAO_CONCLUIDA, 1) },
            "$COLUMN_REFEICAO_ID = ? AND $COLUMN_USUARIO_ID = ?",
            arrayOf(refeicaoId.toString(), usuarioId.toString())
        ) > 0

    /** Data (ISO) para a qual o plano alimentar atual do usuário foi gerado. */
    fun diaDoPlano(usuarioId: Long): String? {
        val cursor = db.query(
            TABLE_REFEICOES,
            arrayOf(COLUMN_REFEICAO_DIA),
            "$COLUMN_USUARIO_ID = ?",
            arrayOf(usuarioId.toString()),
            null, null, "$COLUMN_REFEICAO_ID ASC", "1"
        )
        return cursor.use { if (it.moveToFirst()) it.getString(0) else null }
    }

    /**
     * Garante que o plano alimentar do usuário seja o do dia informado.
     * Se ainda não existir plano ou se ele for de outro dia, substitui o
     * cardápio pelas refeições recebidas (zerando o status de concluída).
     */
    fun garantirPlanoDoDia(usuarioId: Long, dataIso: String, refeicoes: List<RefeicaoDia>) {
        if (usuarioId <= 0L) return
        val diaAtual = diaDoPlano(usuarioId)
        if (diaAtual == dataIso) return
        substituirPlanoDoDia(usuarioId, dataIso, refeicoes)
    }

    /** Remove o cardápio atual do usuário e grava o plano informado para a data. */
    fun substituirPlanoDoDia(usuarioId: Long, dataIso: String, refeicoes: List<RefeicaoDia>) {
        if (usuarioId <= 0L) return
        db.beginTransaction()
        try {
            db.delete(
                TABLE_REFEICOES,
                "$COLUMN_USUARIO_ID = ?",
                arrayOf(usuarioId.toString())
            )
            refeicoes.forEach { refeicao ->
                db.insertOrThrow(
                    TABLE_REFEICOES,
                    null,
                    ContentValues().apply {
                        put(COLUMN_USUARIO_ID, usuarioId)
                        put(COLUMN_REFEICAO_DIA, dataIso)
                        put(COLUMN_REFEICAO_PERIODO, refeicao.periodo)
                        put(COLUMN_REFEICAO_TITULO, refeicao.titulo)
                        put(COLUMN_REFEICAO_CALORIAS, refeicao.calorias)
                        put(COLUMN_REFEICAO_HORARIO, refeicao.horario)
                        put(COLUMN_REFEICAO_IMAGEM, refeicao.imagem)
                        put(
                            COLUMN_REFEICAO_INGREDIENTES,
                            refeicao.ingredientes.joinToString("\n") { "${it.nome}|${it.quantidade}" }
                        )
                        put(COLUMN_REFEICAO_INSTRUCOES, refeicao.instrucoes.joinToString("\n"))
                        put(COLUMN_REFEICAO_CONCLUIDA, 0)
                    }
                )
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    private fun Cursor.paraRefeicao(): RefeicaoDia = RefeicaoDia(
        id = getLong(getColumnIndexOrThrow(COLUMN_REFEICAO_ID)),
        periodo = getString(getColumnIndexOrThrow(COLUMN_REFEICAO_PERIODO)),
        titulo = getString(getColumnIndexOrThrow(COLUMN_REFEICAO_TITULO)),
        calorias = getString(getColumnIndexOrThrow(COLUMN_REFEICAO_CALORIAS)),
        horario = getString(getColumnIndexOrThrow(COLUMN_REFEICAO_HORARIO)),
        imagem = getInt(getColumnIndexOrThrow(COLUMN_REFEICAO_IMAGEM)),
        ingredientes = getString(getColumnIndexOrThrow(COLUMN_REFEICAO_INGREDIENTES))
            .lineSequence()
            .filter { it.isNotBlank() }
            .map { linha ->
                val partes = linha.split("|", limit = 2)
                Ingrediente(partes[0], partes.getOrElse(1) { "" })
            }
            .toList(),
        instrucoes = getString(getColumnIndexOrThrow(COLUMN_REFEICAO_INSTRUCOES))
            .lineSequence()
            .filter { it.isNotBlank() }
            .toList(),
        concluida = getInt(getColumnIndexOrThrow(COLUMN_REFEICAO_CONCLUIDA)) == 1
    )
}
