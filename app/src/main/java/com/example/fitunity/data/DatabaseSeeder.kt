package com.example.fitunity.data

import android.content.ContentValues
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
import com.example.fitunity.data.DbContract.COLUMN_HISTORICO_CALORIAS
import com.example.fitunity.data.DbContract.COLUMN_HISTORICO_DATA
import com.example.fitunity.data.DbContract.COLUMN_HISTORICO_DURACAO
import com.example.fitunity.data.DbContract.COLUMN_HISTORICO_TITULO
import com.example.fitunity.data.DbContract.COLUMN_ID
import com.example.fitunity.data.DbContract.COLUMN_MENSAGEM_CRIADA_EM
import com.example.fitunity.data.DbContract.COLUMN_MENSAGEM_HORARIO
import com.example.fitunity.data.DbContract.COLUMN_MENSAGEM_REMETENTE
import com.example.fitunity.data.DbContract.COLUMN_MENSAGEM_TEXTO
import com.example.fitunity.data.DbContract.COLUMN_NOTIFICACAO_HORARIO
import com.example.fitunity.data.DbContract.COLUMN_NOTIFICACAO_LIDA
import com.example.fitunity.data.DbContract.COLUMN_NOTIFICACAO_MENSAGEM
import com.example.fitunity.data.DbContract.COLUMN_NOTIFICACAO_TIPO
import com.example.fitunity.data.DbContract.COLUMN_NOTIFICACAO_TITULO
import com.example.fitunity.data.DbContract.COLUMN_REFEICAO_CALORIAS
import com.example.fitunity.data.DbContract.COLUMN_REFEICAO_CONCLUIDA
import com.example.fitunity.data.DbContract.COLUMN_REFEICAO_DIA
import com.example.fitunity.data.DbContract.COLUMN_REFEICAO_HORARIO
import com.example.fitunity.data.DbContract.COLUMN_REFEICAO_IMAGEM
import com.example.fitunity.data.DbContract.COLUMN_REFEICAO_INGREDIENTES
import com.example.fitunity.data.DbContract.COLUMN_REFEICAO_INSTRUCOES
import com.example.fitunity.data.DbContract.COLUMN_REFEICAO_PERIODO
import com.example.fitunity.data.DbContract.COLUMN_REFEICAO_TITULO
import com.example.fitunity.data.DbContract.COLUMN_TREINO_DESCRICAO
import com.example.fitunity.data.DbContract.COLUMN_TREINO_DIA
import com.example.fitunity.data.DbContract.COLUMN_TREINO_DURACAO
import com.example.fitunity.data.DbContract.COLUMN_TREINO_FOCO
import com.example.fitunity.data.DbContract.COLUMN_TREINO_IMAGEM
import com.example.fitunity.data.DbContract.COLUMN_TREINO_TITULO
import com.example.fitunity.data.DbContract.COLUMN_USUARIO_ID
import com.example.fitunity.data.DbContract.REMETENTE_PERSONAL
import com.example.fitunity.data.DbContract.REMETENTE_USUARIO
import com.example.fitunity.data.DbContract.TABLE_ESTATISTICAS
import com.example.fitunity.data.DbContract.TABLE_EXERCICIOS
import com.example.fitunity.data.DbContract.TABLE_HISTORICO
import com.example.fitunity.data.DbContract.TABLE_MENSAGENS_PERSONAL
import com.example.fitunity.data.DbContract.TABLE_NOTIFICACOES
import com.example.fitunity.data.DbContract.TABLE_PERFIS_FISICOS
import com.example.fitunity.data.DbContract.TABLE_PREFERENCIAS_DIETA
import com.example.fitunity.data.DbContract.TABLE_PREFERENCIAS_PLANO
import com.example.fitunity.data.DbContract.TABLE_REFEICOES
import com.example.fitunity.data.DbContract.TABLE_TREINOS
import com.example.fitunity.data.DbContract.TABLE_USUARIOS
import com.example.fitunity.data.dao.TreinoDao

/**
 * Centraliza a criação dos dados iniciais (catálogo de exercícios e conteúdo
 * de exemplo por usuário), mantendo o FitUnityDbHelper focado na estrutura.
 */
object DatabaseSeeder {

    fun garantirExercicios(db: SQLiteDatabase) {
        val total = db.rawQuery("SELECT COUNT(*) FROM $TABLE_EXERCICIOS", null).use {
            if (it.moveToFirst()) it.getInt(0) else 0
        }
        if (total > 0) return
        bibliotecaExercicios.forEach { exercicio ->
            db.insertWithOnConflict(
                TABLE_EXERCICIOS,
                null,
                ContentValues().apply {
                    put(COLUMN_EXERCICIO_ID, exercicio.id)
                    put(COLUMN_EXERCICIO_NOME, exercicio.nome)
                    put(COLUMN_EXERCICIO_GRUPO, exercicio.grupo)
                    put(COLUMN_EXERCICIO_SERIES, exercicio.series)
                    put(COLUMN_EXERCICIO_REPETICOES, exercicio.repeticoes)
                    put(COLUMN_EXERCICIO_INTERVALO, exercicio.intervalo)
                    put(COLUMN_EXERCICIO_CARGA, exercicio.carga)
                    put(COLUMN_EXERCICIO_IMAGEM, exercicio.imagem)
                    put(COLUMN_EXERCICIO_INSTRUCOES, exercicio.instrucoes.joinToString("\n"))
                },
                SQLiteDatabase.CONFLICT_IGNORE
            )
        }
    }

    fun garantirDadosUsuario(db: SQLiteDatabase, usuarioId: Long) {
        if (!usuarioExiste(db, usuarioId)) return
        garantirLinhasPerfil(db, usuarioId)

        if (contarPorUsuario(db, TABLE_TREINOS, usuarioId) == 0) {
            val treinoDao = TreinoDao(db)
            treinosSemana.forEach { treino ->
                val treinoId = db.insertOrThrow(
                    TABLE_TREINOS,
                    null,
                    ContentValues().apply {
                        put(COLUMN_USUARIO_ID, usuarioId)
                        put(COLUMN_TREINO_DIA, treino.diaSemana)
                        put(COLUMN_TREINO_TITULO, treino.titulo)
                        put(COLUMN_TREINO_DESCRICAO, treino.descricao)
                        put(COLUMN_TREINO_DURACAO, treino.duracao)
                        put(COLUMN_TREINO_IMAGEM, treino.imagem)
                        put(COLUMN_TREINO_FOCO, treino.descricao)
                    }
                )
                treinoDao.substituirExerciciosDoTreino(treinoId, treino.exercicios.map { it.id })
            }
        }

        if (contarPorUsuario(db, TABLE_REFEICOES, usuarioId) == 0) {
            refeicoesHoje.forEach { refeicao ->
                db.insertOrThrow(
                    TABLE_REFEICOES,
                    null,
                    ContentValues().apply {
                        put(COLUMN_USUARIO_ID, usuarioId)
                        put(COLUMN_REFEICAO_DIA, "")
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
        }

        if (contarPorUsuario(db, TABLE_NOTIFICACOES, usuarioId) == 0) {
            notificacoesMock.forEach { notificacao ->
                db.insertOrThrow(
                    TABLE_NOTIFICACOES,
                    null,
                    ContentValues().apply {
                        put(COLUMN_USUARIO_ID, usuarioId)
                        put(COLUMN_NOTIFICACAO_TITULO, notificacao.titulo)
                        put(COLUMN_NOTIFICACAO_MENSAGEM, notificacao.mensagem)
                        put(COLUMN_NOTIFICACAO_HORARIO, notificacao.horario)
                        put(COLUMN_NOTIFICACAO_TIPO, notificacao.tipo.name)
                        put(COLUMN_NOTIFICACAO_LIDA, if (notificacao.lida) 1 else 0)
                    }
                )
            }
        }

        if (contarPorUsuario(db, TABLE_HISTORICO, usuarioId) == 0) {
            historicoTreinosMock.forEach { treino ->
                db.insertOrThrow(
                    TABLE_HISTORICO,
                    null,
                    ContentValues().apply {
                        put(COLUMN_USUARIO_ID, usuarioId)
                        put(COLUMN_HISTORICO_DATA, treino.data)
                        put(COLUMN_HISTORICO_TITULO, treino.titulo)
                        put(COLUMN_HISTORICO_DURACAO, treino.duracao)
                        put(COLUMN_HISTORICO_CALORIAS, treino.calorias)
                    }
                )
            }
        }

        if (contarPorUsuario(db, TABLE_MENSAGENS_PERSONAL, usuarioId) == 0) {
            val agora = System.currentTimeMillis()
            mensagensIniciais.forEachIndexed { index, mensagem ->
                db.insertOrThrow(
                    TABLE_MENSAGENS_PERSONAL,
                    null,
                    ContentValues().apply {
                        put(COLUMN_USUARIO_ID, usuarioId)
                        put(
                            COLUMN_MENSAGEM_REMETENTE,
                            if (mensagem.enviadaPorMim) REMETENTE_USUARIO else REMETENTE_PERSONAL
                        )
                        put(COLUMN_MENSAGEM_TEXTO, mensagem.texto)
                        put(COLUMN_MENSAGEM_HORARIO, mensagem.horario)
                        put(COLUMN_MENSAGEM_CRIADA_EM, agora + index)
                    }
                )
            }
        }
    }

    internal fun garantirLinhasPerfil(db: SQLiteDatabase, usuarioId: Long) {
        if (!usuarioExiste(db, usuarioId)) return
        listOf(
            TABLE_PERFIS_FISICOS,
            TABLE_ESTATISTICAS,
            TABLE_PREFERENCIAS_DIETA,
            TABLE_PREFERENCIAS_PLANO
        ).forEach { tabela ->
            db.insertWithOnConflict(
                tabela,
                null,
                ContentValues().apply { put(COLUMN_USUARIO_ID, usuarioId) },
                SQLiteDatabase.CONFLICT_IGNORE
            )
        }
    }

    private fun usuarioExiste(db: SQLiteDatabase, usuarioId: Long): Boolean =
        db.rawQuery(
            "SELECT 1 FROM $TABLE_USUARIOS WHERE $COLUMN_ID = ? LIMIT 1",
            arrayOf(usuarioId.toString())
        ).use { it.moveToFirst() }

    private fun contarPorUsuario(db: SQLiteDatabase, tabela: String, usuarioId: Long): Int =
        db.rawQuery(
            "SELECT COUNT(*) FROM $tabela WHERE $COLUMN_USUARIO_ID = ?",
            arrayOf(usuarioId.toString())
        ).use { if (it.moveToFirst()) it.getInt(0) else 0 }
}
