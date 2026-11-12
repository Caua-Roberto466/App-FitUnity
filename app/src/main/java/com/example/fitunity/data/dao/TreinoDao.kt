package com.example.fitunity.data.dao

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.fitunity.data.DbContract.COLUMN_EXERCICIO_CARGA
import com.example.fitunity.data.DbContract.COLUMN_EXERCICIO_GRUPO
import com.example.fitunity.data.DbContract.COLUMN_EXERCICIO_ID
import com.example.fitunity.data.DbContract.COLUMN_EXERCICIO_IMAGEM
import com.example.fitunity.data.DbContract.COLUMN_EXERCICIO_INSTRUCOES
import com.example.fitunity.data.DbContract.COLUMN_EXERCICIO_INTERVALO
import com.example.fitunity.data.DbContract.COLUMN_EXERCICIO_NOME
import com.example.fitunity.data.DbContract.COLUMN_EXERCICIO_REPETICOES
import com.example.fitunity.data.DbContract.COLUMN_EXERCICIO_SERIES
import com.example.fitunity.data.DbContract.COLUMN_ORDEM
import com.example.fitunity.data.DbContract.COLUMN_TREINO_DESCRICAO
import com.example.fitunity.data.DbContract.COLUMN_TREINO_DIA
import com.example.fitunity.data.DbContract.COLUMN_TREINO_DURACAO
import com.example.fitunity.data.DbContract.COLUMN_TREINO_FOCO
import com.example.fitunity.data.DbContract.COLUMN_TREINO_ID
import com.example.fitunity.data.DbContract.COLUMN_TREINO_IMAGEM
import com.example.fitunity.data.DbContract.COLUMN_TREINO_TITULO
import com.example.fitunity.data.DbContract.COLUMN_USUARIO_ID
import com.example.fitunity.data.DbContract.TABLE_EXERCICIOS
import com.example.fitunity.data.DbContract.TABLE_TREINOS
import com.example.fitunity.data.DbContract.TABLE_TREINO_EXERCICIOS
import com.example.fitunity.data.Exercicio
import com.example.fitunity.data.TreinoDia

class TreinoDao(private val db: SQLiteDatabase) {

    fun listarExercicios(): List<Exercicio> {
        val cursor = db.query(
            TABLE_EXERCICIOS,
            null,
            null,
            null,
            null,
            null,
            "$COLUMN_EXERCICIO_ID ASC"
        )
        return cursor.use {
            buildList {
                while (it.moveToNext()) add(it.paraExercicio())
            }
        }
    }

    fun buscarExercicio(id: Int): Exercicio? {
        val cursor = db.query(
            TABLE_EXERCICIOS,
            null,
            "$COLUMN_EXERCICIO_ID = ?",
            arrayOf(id.toString()),
            null, null, null
        )
        return cursor.use { if (it.moveToFirst()) it.paraExercicio() else null }
    }

    fun listarTreinos(usuarioId: Long): List<TreinoDia> {
        val cursor = db.query(
            TABLE_TREINOS,
            null,
            "$COLUMN_USUARIO_ID = ?",
            arrayOf(usuarioId.toString()),
            null, null, "$COLUMN_TREINO_ID ASC"
        )
        val treinos = mutableListOf<TreinoDia>()
        cursor.use {
            while (it.moveToNext()) {
                val id = it.getInt(it.getColumnIndexOrThrow(COLUMN_TREINO_ID))
                val exercicios = listarExerciciosDoTreino(id)
                val series = exercicios.sumOf { exercicio ->
                    exercicio.series.filter(Char::isDigit).toIntOrNull() ?: 1
                }
                treinos.add(
                    TreinoDia(
                        id = id,
                        diaSemana = it.getString(it.getColumnIndexOrThrow(COLUMN_TREINO_DIA)),
                        titulo = it.getString(it.getColumnIndexOrThrow(COLUMN_TREINO_TITULO)),
                        descricao = it.getString(it.getColumnIndexOrThrow(COLUMN_TREINO_DESCRICAO)),
                        duracao = it.getString(it.getColumnIndexOrThrow(COLUMN_TREINO_DURACAO)),
                        quantidadeExercicios = exercicios.size,
                        quantidadeSeries = series,
                        imagem = it.getInt(it.getColumnIndexOrThrow(COLUMN_TREINO_IMAGEM)),
                        exercicios = exercicios
                    )
                )
            }
        }
        return treinos
    }

    fun criarTreino(
        usuarioId: Long,
        diaSemana: String,
        titulo: String,
        descricao: String,
        duracao: String,
        imagem: Int,
        foco: String,
        exercicioIds: List<Int>
    ): Long {
        val valores = ContentValues().apply {
            put(COLUMN_USUARIO_ID, usuarioId)
            put(COLUMN_TREINO_DIA, diaSemana)
            put(COLUMN_TREINO_TITULO, titulo.trim())
            put(COLUMN_TREINO_DESCRICAO, descricao)
            put(COLUMN_TREINO_DURACAO, duracao)
            put(COLUMN_TREINO_IMAGEM, imagem)
            put(COLUMN_TREINO_FOCO, foco)
        }
        val treinoId = db.insertOrThrow(TABLE_TREINOS, null, valores)
        substituirExerciciosDoTreino(treinoId, exercicioIds)
        return treinoId
    }

    fun atualizarTreino(
        usuarioId: Long,
        treinoId: Int,
        diaSemana: String,
        titulo: String,
        descricao: String,
        duracao: String,
        imagem: Int,
        foco: String,
        exercicioIds: List<Int>
    ): Boolean {
        val valores = ContentValues().apply {
            put(COLUMN_TREINO_DIA, diaSemana)
            put(COLUMN_TREINO_TITULO, titulo.trim())
            put(COLUMN_TREINO_DESCRICAO, descricao)
            put(COLUMN_TREINO_DURACAO, duracao)
            put(COLUMN_TREINO_IMAGEM, imagem)
            put(COLUMN_TREINO_FOCO, foco)
        }
        val linhas = db.update(
            TABLE_TREINOS,
            valores,
            "$COLUMN_TREINO_ID = ? AND $COLUMN_USUARIO_ID = ?",
            arrayOf(treinoId.toString(), usuarioId.toString())
        )
        if (linhas > 0) substituirExerciciosDoTreino(treinoId.toLong(), exercicioIds)
        return linhas > 0
    }

    fun removerTreino(usuarioId: Long, treinoId: Int): Boolean {
        db.delete(
            TABLE_TREINO_EXERCICIOS,
            "$COLUMN_TREINO_ID = ?",
            arrayOf(treinoId.toString())
        )
        return db.delete(
            TABLE_TREINOS,
            "$COLUMN_TREINO_ID = ? AND $COLUMN_USUARIO_ID = ?",
            arrayOf(treinoId.toString(), usuarioId.toString())
        ) > 0
    }

    fun substituirExerciciosDoTreino(treinoId: Long, exercicioIds: List<Int>) {
        db.delete(
            TABLE_TREINO_EXERCICIOS,
            "$COLUMN_TREINO_ID = ?",
            arrayOf(treinoId.toString())
        )
        exercicioIds.distinct().forEachIndexed { index, exercicioId ->
            db.insertOrThrow(
                TABLE_TREINO_EXERCICIOS,
                null,
                ContentValues().apply {
                    put(COLUMN_TREINO_ID, treinoId)
                    put(COLUMN_EXERCICIO_ID, exercicioId)
                    put(COLUMN_ORDEM, index)
                }
            )
        }
    }

    private fun listarExerciciosDoTreino(treinoId: Int): List<Exercicio> {
        val cursor = db.rawQuery(
            """
            SELECT e.* FROM $TABLE_EXERCICIOS e
            INNER JOIN $TABLE_TREINO_EXERCICIOS te ON te.$COLUMN_EXERCICIO_ID = e.$COLUMN_EXERCICIO_ID
            WHERE te.$COLUMN_TREINO_ID = ?
            ORDER BY te.$COLUMN_ORDEM ASC
            """.trimIndent(),
            arrayOf(treinoId.toString())
        )
        return cursor.use {
            buildList {
                while (it.moveToNext()) add(it.paraExercicio())
            }
        }
    }

    private fun Cursor.paraExercicio(): Exercicio = Exercicio(
        id = getInt(getColumnIndexOrThrow(COLUMN_EXERCICIO_ID)),
        nome = getString(getColumnIndexOrThrow(COLUMN_EXERCICIO_NOME)),
        grupo = getString(getColumnIndexOrThrow(COLUMN_EXERCICIO_GRUPO)),
        series = getString(getColumnIndexOrThrow(COLUMN_EXERCICIO_SERIES)),
        repeticoes = getString(getColumnIndexOrThrow(COLUMN_EXERCICIO_REPETICOES)),
        intervalo = getString(getColumnIndexOrThrow(COLUMN_EXERCICIO_INTERVALO)),
        carga = getString(getColumnIndexOrThrow(COLUMN_EXERCICIO_CARGA)),
        imagem = getInt(getColumnIndexOrThrow(COLUMN_EXERCICIO_IMAGEM)),
        instrucoes = getString(getColumnIndexOrThrow(COLUMN_EXERCICIO_INSTRUCOES))
            .lineSequence()
            .filter { it.isNotBlank() }
            .toList()
    )
}
