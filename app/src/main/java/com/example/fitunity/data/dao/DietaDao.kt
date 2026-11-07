package com.example.fitunity.data.dao

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.fitunity.data.DbContract.COLUMN_REFEICAO_CALORIAS
import com.example.fitunity.data.DbContract.COLUMN_REFEICAO_CONCLUIDA
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
