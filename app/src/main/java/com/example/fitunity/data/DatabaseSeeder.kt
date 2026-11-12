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
import com.example.fitunity.data.DbContract.COLUMN_ALTURA
import com.example.fitunity.data.DbContract.COLUMN_CANDIDATURA_APRESENTACAO
import com.example.fitunity.data.DbContract.COLUMN_CANDIDATURA_ATUALIZADA_EM
import com.example.fitunity.data.DbContract.COLUMN_CANDIDATURA_CREF
import com.example.fitunity.data.DbContract.COLUMN_CANDIDATURA_CRIADA_EM
import com.example.fitunity.data.DbContract.COLUMN_CANDIDATURA_EMAIL
import com.example.fitunity.data.DbContract.COLUMN_CANDIDATURA_ESPECIALIDADES
import com.example.fitunity.data.DbContract.COLUMN_CANDIDATURA_EXPERIENCIA
import com.example.fitunity.data.DbContract.COLUMN_CANDIDATURA_NOME
import com.example.fitunity.data.DbContract.COLUMN_CANDIDATURA_STATUS
import com.example.fitunity.data.DbContract.COLUMN_CANDIDATURA_TELEFONE
import com.example.fitunity.data.DbContract.COLUMN_DATA_NASCIMENTO
import com.example.fitunity.data.DbContract.COLUMN_EMAIL
import com.example.fitunity.data.DbContract.COLUMN_FOCO
import com.example.fitunity.data.DbContract.COLUMN_GENERO
import com.example.fitunity.data.DbContract.COLUMN_HISTORICO_CALORIAS
import com.example.fitunity.data.DbContract.COLUMN_HISTORICO_DATA
import com.example.fitunity.data.DbContract.COLUMN_HISTORICO_DURACAO
import com.example.fitunity.data.DbContract.COLUMN_HISTORICO_TITULO
import com.example.fitunity.data.DbContract.COLUMN_ID
import com.example.fitunity.data.DbContract.COLUMN_NIVEL
import com.example.fitunity.data.DbContract.COLUMN_NOME
import com.example.fitunity.data.DbContract.COLUMN_OBJETIVO_PESO
import com.example.fitunity.data.DbContract.COLUMN_PESO_ATUAL
import com.example.fitunity.data.DbContract.COLUMN_PROGRESSO
import com.example.fitunity.data.DbContract.COLUMN_SENHA
import com.example.fitunity.data.DbContract.COLUMN_TEMPO_CADASTRADO
import com.example.fitunity.data.DbContract.COLUMN_TIPO_PLANO
import com.example.fitunity.data.DbContract.COLUMN_TREINOS_PENDENTES
import com.example.fitunity.data.DbContract.COLUMN_TREINOS_REALIZADOS
import com.example.fitunity.data.DbContract.COLUMN_MENSAGEM_CRIADA_EM
import com.example.fitunity.data.DbContract.COLUMN_MENSAGEM_HORARIO
import com.example.fitunity.data.DbContract.COLUMN_MENSAGEM_REMETENTE
import com.example.fitunity.data.DbContract.COLUMN_MENSAGEM_TEXTO
import com.example.fitunity.data.DbContract.COLUMN_NOTIFICACAO_HORARIO
import com.example.fitunity.data.DbContract.COLUMN_NOTIFICACAO_LIDA
import com.example.fitunity.data.DbContract.COLUMN_NOTIFICACAO_MENSAGEM
import com.example.fitunity.data.DbContract.COLUMN_NOTIFICACAO_TIPO
import com.example.fitunity.data.DbContract.COLUMN_NOTIFICACAO_TITULO
import com.example.fitunity.data.DbContract.COLUMN_TREINO_DESCRICAO
import com.example.fitunity.data.DbContract.COLUMN_TREINO_DIA
import com.example.fitunity.data.DbContract.COLUMN_TREINO_DURACAO
import com.example.fitunity.data.DbContract.COLUMN_TREINO_FOCO
import com.example.fitunity.data.DbContract.COLUMN_TREINO_IMAGEM
import com.example.fitunity.data.DbContract.COLUMN_TREINO_TITULO
import com.example.fitunity.data.DbContract.COLUMN_USUARIO_ID
import com.example.fitunity.data.DbContract.REMETENTE_PERSONAL
import com.example.fitunity.data.DbContract.REMETENTE_USUARIO
import com.example.fitunity.data.DbContract.TABLE_CANDIDATURAS_PERSONAL
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
import com.example.fitunity.data.dao.DietaDao
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

    fun garantirAlunosDemonstracao(db: SQLiteDatabase) {
        alunosDemonstracao.forEach { aluno ->
            db.insertWithOnConflict(
                TABLE_USUARIOS,
                null,
                ContentValues().apply {
                    put(COLUMN_EMAIL, aluno.email)
                    put(COLUMN_SENHA, "conta-demonstracao-sem-acesso")
                    put(COLUMN_NOME, aluno.nome)
                    put(COLUMN_DATA_NASCIMENTO, aluno.dataNascimento)
                    put(COLUMN_GENERO, aluno.genero)
                    put(COLUMN_TEMPO_CADASTRADO, aluno.tempoCadastrado)
                },
                SQLiteDatabase.CONFLICT_IGNORE
            )
            val id = db.rawQuery(
                "SELECT $COLUMN_ID FROM $TABLE_USUARIOS WHERE $COLUMN_EMAIL = ? LIMIT 1",
                arrayOf(aluno.email)
            ).use { if (it.moveToFirst()) it.getLong(0) else return@forEach }

            garantirLinhasPerfil(db, id)
            db.update(
                TABLE_PERFIS_FISICOS,
                ContentValues().apply {
                    put(COLUMN_PESO_ATUAL, aluno.peso)
                    put(COLUMN_OBJETIVO_PESO, aluno.objetivoPeso)
                    put(COLUMN_ALTURA, aluno.altura)
                    put(COLUMN_FOCO, aluno.foco)
                    put(COLUMN_NIVEL, aluno.nivel)
                    put(COLUMN_TIPO_PLANO, aluno.plano)
                },
                "$COLUMN_USUARIO_ID = ?",
                arrayOf(id.toString())
            )
            db.update(
                TABLE_ESTATISTICAS,
                ContentValues().apply {
                    put(COLUMN_TREINOS_REALIZADOS, aluno.treinos)
                    put(COLUMN_TREINOS_PENDENTES, 2)
                    put(COLUMN_PROGRESSO, aluno.progresso)
                },
                "$COLUMN_USUARIO_ID = ?",
                arrayOf(id.toString())
            )
        }
    }

    fun garantirPersonaisDemonstracao(db: SQLiteDatabase) {
        personaisDemonstracao.forEach { personal ->
            db.insertWithOnConflict(
                TABLE_USUARIOS,
                null,
                ContentValues().apply {
                    put(COLUMN_EMAIL, personal.email)
                    put(COLUMN_SENHA, "conta-demonstracao-sem-acesso")
                    put(COLUMN_NOME, personal.nome)
                    put(COLUMN_DATA_NASCIMENTO, personal.dataNascimento)
                    put(COLUMN_GENERO, personal.genero)
                    put(COLUMN_TEMPO_CADASTRADO, personal.tempoCadastrado)
                },
                SQLiteDatabase.CONFLICT_IGNORE
            )
            val id = db.rawQuery(
                "SELECT $COLUMN_ID FROM $TABLE_USUARIOS WHERE $COLUMN_EMAIL = ? LIMIT 1",
                arrayOf(personal.email)
            ).use { if (it.moveToFirst()) it.getLong(0) else return@forEach }

            garantirLinhasPerfil(db, id)

            val agora = System.currentTimeMillis()
            val existe = db.rawQuery(
                "SELECT 1 FROM $TABLE_CANDIDATURAS_PERSONAL WHERE $COLUMN_USUARIO_ID = ? LIMIT 1",
                arrayOf(id.toString())
            ).use { it.moveToFirst() }

            val valores = ContentValues().apply {
                put(COLUMN_CANDIDATURA_NOME, personal.nome)
                put(COLUMN_CANDIDATURA_EMAIL, personal.email)
                put(COLUMN_CANDIDATURA_TELEFONE, personal.telefone)
                put(COLUMN_CANDIDATURA_CREF, personal.cref)
                put(COLUMN_CANDIDATURA_ESPECIALIDADES, personal.especialidades.joinToString("|"))
                put(COLUMN_CANDIDATURA_EXPERIENCIA, personal.experiencia)
                put(COLUMN_CANDIDATURA_APRESENTACAO, personal.apresentacao)
                put(COLUMN_CANDIDATURA_STATUS, StatusCandidatura.APROVADO.name)
                put(COLUMN_CANDIDATURA_ATUALIZADA_EM, agora)
            }
            if (existe) {
                db.update(
                    TABLE_CANDIDATURAS_PERSONAL,
                    valores,
                    "$COLUMN_USUARIO_ID = ?",
                    arrayOf(id.toString())
                )
            } else {
                valores.put(COLUMN_USUARIO_ID, id)
                valores.put(COLUMN_CANDIDATURA_CRIADA_EM, agora)
                db.insertOrThrow(TABLE_CANDIDATURAS_PERSONAL, null, valores)
            }
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
            DietaDao(db).garantirPlanoDoDia(usuarioId, dataDeHoje(), planoDoDia())
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

    private data class PersonalDemo(
        val nome: String,
        val email: String,
        val telefone: String,
        val cref: String,
        val especialidades: List<String>,
        val experiencia: String,
        val apresentacao: String,
        val genero: String,
        val dataNascimento: String = "",
        val tempoCadastrado: String = "2023"
    )

    private val personaisDemonstracao = listOf(
        PersonalDemo(
            nome = "Rafael Martins",
            email = "rafael.personal@fitunity.app",
            telefone = "(11) 98765-4321",
            cref = "12345-G/SP",
            especialidades = listOf("Hipertrofia", "Emagrecimento", "Funcional"),
            experiencia = "8 anos",
            apresentacao = "Acredito em treinos consistentes e ajustados à rotina de cada aluno. " +
                "Trabalho com hipertrofia, emagrecimento e condicionamento funcional, sempre " +
                "acompanhando de perto a execução e a evolução.",
            genero = "Masculino"
        ),
        PersonalDemo(
            nome = "Juliana Costa",
            email = "juliana.personal@fitunity.app",
            telefone = "(21) 99876-5432",
            cref = "54321-G/RJ",
            especialidades = listOf("Funcional", "Mobilidade", "Pilates"),
            experiencia = "6 anos",
            apresentacao = "Meu foco é melhorar a qualidade de vida com treinos funcionais, " +
                "mobilidade e fortalecimento do core. Atendo alunos iniciantes e intermediários " +
                "que buscam mais disposição e menos dores no dia a dia.",
            genero = "Feminino"
        ),
        PersonalDemo(
            nome = "Marcos Silva",
            email = "marcos.personal@fitunity.app",
            telefone = "(31) 99123-4567",
            cref = "98765-G/MG",
            especialidades = listOf("Força", "Hipertrofia", "Reabilitação"),
            experiencia = "10 anos",
            apresentacao = "Trabalho com treinamento de força e hipertrofia, respeitando o histórico " +
                "e as limitações de cada aluno. Também acompanho retornos pós-lesão com progressão " +
                "cuidadosa e segura.",
            genero = "Masculino"
        ),
        PersonalDemo(
            nome = "Camila Rocha",
            email = "camila.personal@fitunity.app",
            telefone = "(41) 99234-5678",
            cref = "24680-G/PR",
            especialidades = listOf("Emagrecimento", "Corrida", "Nutrição esportiva"),
            experiencia = "5 anos",
            apresentacao = "Ajudo alunos a emagrecer com saúde e a evoluir na corrida, unindo " +
                "treino de força, condicionamento e orientação nutricional básica. Meu " +
                "acompanhamento é próximo e focado em constância, não em soluções rápidas.",
            genero = "Feminino"
        )
    )

    private data class AlunoDemo(
        val nome: String,
        val email: String,
        val genero: String,
        val plano: String,
        val foco: String,
        val nivel: String,
        val progresso: Double,
        val peso: Double,
        val objetivoPeso: Double,
        val altura: Double,
        val treinos: Int,
        val dataNascimento: String = "",
        val tempoCadastrado: String = "2024"
    )

    private val alunosDemonstracao = listOf(
        AlunoDemo(
            nome = "Ana Beatriz",
            email = "ana.demo@fitunity.app",
            genero = "Feminino",
            plano = "Adaptado",
            foco = "Emagrecimento",
            nivel = "Intermediário",
            progresso = 62.0,
            peso = 68.4,
            objetivoPeso = 62.0,
            altura = 1.65,
            treinos = 18
        ),
        AlunoDemo(
            nome = "Bruno Lima",
            email = "bruno.demo@fitunity.app",
            genero = "Masculino",
            plano = "Profissional",
            foco = "Hipertrofia",
            nivel = "Avançado",
            progresso = 78.0,
            peso = 82.0,
            objetivoPeso = 86.0,
            altura = 1.79,
            treinos = 32
        ),
        AlunoDemo(
            nome = "Carla Mendes",
            email = "carla.demo@fitunity.app",
            genero = "Feminino",
            plano = "Adaptado",
            foco = "Funcional",
            nivel = "Iniciante",
            progresso = 41.0,
            peso = 74.0,
            objetivoPeso = 68.0,
            altura = 1.68,
            treinos = 9
        )
    )
}
