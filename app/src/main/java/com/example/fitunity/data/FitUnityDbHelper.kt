package com.example.fitunity.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.security.MessageDigest
import java.util.Calendar

// Modelo de dados com as informações visíveis do perfil
data class PerfilCliente(
    val id: Long,
    val nome: String,
    val email: String,
    val dataNascimento: String,
    val genero: String,
    val pesoAtual: Double,
    val objetivoPeso: Double,
    val treinosRealizados: Int,
    val treinosPendentes: Int,
    val nivel: String,         // Iniciante, Intermediário, Avançado
    val tipoPlano: String,     // Básico, Adaptado, Profissional
    val tempoCadastrado: String,
    val progresso: Double,     // Porcentagem
    val foco: String = "Hipertrofia", // Ex.: Hipertrofia, Emagrecimento
    val fotoUri: String? = null, // Caminho do arquivo de foto de perfil salvo localmente, ou null se não houver
    val altura: Double = 1.75,
    val aguaMl: Int = 0,
    val metaCalorias: Int = 2200,
    val refeicoesPorDia: Int = 4,
    val objetivoDieta: String = "Manter",
    val restricaoAlimentar: String = "Nenhuma",
    val observacoesDieta: String = "",
    val objetivoPlano: String = "Manter a saúde",
    val frequenciaTreino: String = "4 vezes por semana"
)

class FitUnityDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE $TABLE_USUARIOS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_EMAIL TEXT UNIQUE NOT NULL,
                $COLUMN_SENHA TEXT NOT NULL,
                $COLUMN_NOME TEXT NOT NULL,
                $COLUMN_DATA_NASCIMENTO TEXT NOT NULL,
                $COLUMN_GENERO TEXT NOT NULL,
                $COLUMN_PESO_ATUAL REAL DEFAULT 0.0,
                $COLUMN_OBJETIVO_PESO REAL DEFAULT 0.0,
                $COLUMN_TREINOS_REALIZADOS INTEGER DEFAULT 0,
                $COLUMN_TREINOS_PENDENTES INTEGER DEFAULT 0,
                $COLUMN_NIVEL TEXT DEFAULT 'Iniciante',
                $COLUMN_TIPO_PLANO TEXT DEFAULT 'Básico',
                $COLUMN_TEMPO_CADASTRADO TEXT DEFAULT 'Recente',
                $COLUMN_PROGRESSO REAL DEFAULT 0.0,
                $COLUMN_FOCO TEXT DEFAULT 'Hipertrofia',
                $COLUMN_FOTO_URI TEXT DEFAULT NULL,
                $COLUMN_ALTURA REAL DEFAULT 1.75,
                $COLUMN_AGUA_ML INTEGER DEFAULT 0,
                $COLUMN_META_CALORIAS INTEGER DEFAULT 2200,
                $COLUMN_REFEICOES_POR_DIA INTEGER DEFAULT 4,
                $COLUMN_OBJETIVO_DIETA TEXT DEFAULT 'Manter',
                $COLUMN_RESTRICAO_ALIMENTAR TEXT DEFAULT 'Nenhuma',
                $COLUMN_OBSERVACOES_DIETA TEXT DEFAULT '',
                $COLUMN_OBJETIVO_PLANO TEXT DEFAULT 'Manter a saúde',
                $COLUMN_FREQUENCIA_TREINO TEXT DEFAULT '4 vezes por semana'
            )
            """.trimIndent()
        )
        criarTabelasConteudo(db)
        garantirExercicios(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Migração aditiva: quem já tinha o banco em versões anteriores ganha as
        // colunas novas sem perder os usuários já cadastrados.
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE $TABLE_USUARIOS ADD COLUMN $COLUMN_FOCO TEXT DEFAULT 'Hipertrofia'")
        }
        if (oldVersion < 3) {
            db.execSQL("ALTER TABLE $TABLE_USUARIOS ADD COLUMN $COLUMN_FOTO_URI TEXT DEFAULT NULL")
        }
        if (oldVersion < 4) {
            db.execSQL("ALTER TABLE $TABLE_USUARIOS ADD COLUMN $COLUMN_ALTURA REAL DEFAULT 1.75")
            db.execSQL("ALTER TABLE $TABLE_USUARIOS ADD COLUMN $COLUMN_AGUA_ML INTEGER DEFAULT 0")
            db.execSQL("ALTER TABLE $TABLE_USUARIOS ADD COLUMN $COLUMN_META_CALORIAS INTEGER DEFAULT 2200")
            db.execSQL("ALTER TABLE $TABLE_USUARIOS ADD COLUMN $COLUMN_REFEICOES_POR_DIA INTEGER DEFAULT 4")
            db.execSQL("ALTER TABLE $TABLE_USUARIOS ADD COLUMN $COLUMN_OBJETIVO_DIETA TEXT DEFAULT 'Manter'")
            db.execSQL("ALTER TABLE $TABLE_USUARIOS ADD COLUMN $COLUMN_RESTRICAO_ALIMENTAR TEXT DEFAULT 'Nenhuma'")
            db.execSQL("ALTER TABLE $TABLE_USUARIOS ADD COLUMN $COLUMN_OBSERVACOES_DIETA TEXT DEFAULT ''")
            db.execSQL("ALTER TABLE $TABLE_USUARIOS ADD COLUMN $COLUMN_OBJETIVO_PLANO TEXT DEFAULT 'Manter a saúde'")
            db.execSQL("ALTER TABLE $TABLE_USUARIOS ADD COLUMN $COLUMN_FREQUENCIA_TREINO TEXT DEFAULT '4 vezes por semana'")
            criarTabelasConteudo(db)
            garantirExercicios(db)
        }
    }

    private fun criarTabelasConteudo(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS $TABLE_EXERCICIOS (
                $COLUMN_EXERCICIO_ID INTEGER PRIMARY KEY,
                $COLUMN_EXERCICIO_NOME TEXT NOT NULL,
                $COLUMN_EXERCICIO_GRUPO TEXT NOT NULL,
                $COLUMN_EXERCICIO_SERIES TEXT NOT NULL,
                $COLUMN_EXERCICIO_REPETICOES TEXT NOT NULL,
                $COLUMN_EXERCICIO_INTERVALO TEXT NOT NULL,
                $COLUMN_EXERCICIO_CARGA TEXT NOT NULL,
                $COLUMN_EXERCICIO_IMAGEM INTEGER NOT NULL,
                $COLUMN_EXERCICIO_INSTRUCOES TEXT NOT NULL
            )
            """.trimIndent()
        )
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS $TABLE_TREINOS (
                $COLUMN_TREINO_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USUARIO_ID INTEGER NOT NULL,
                $COLUMN_TREINO_DIA TEXT NOT NULL,
                $COLUMN_TREINO_TITULO TEXT NOT NULL,
                $COLUMN_TREINO_DESCRICAO TEXT NOT NULL,
                $COLUMN_TREINO_DURACAO TEXT NOT NULL,
                $COLUMN_TREINO_IMAGEM INTEGER NOT NULL,
                $COLUMN_TREINO_FOCO TEXT DEFAULT ''
            )
            """.trimIndent()
        )
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS $TABLE_TREINO_EXERCICIOS (
                $COLUMN_TREINO_ID INTEGER NOT NULL,
                $COLUMN_EXERCICIO_ID INTEGER NOT NULL,
                $COLUMN_ORDEM INTEGER NOT NULL DEFAULT 0,
                PRIMARY KEY ($COLUMN_TREINO_ID, $COLUMN_EXERCICIO_ID)
            )
            """.trimIndent()
        )
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS $TABLE_REFEICOES (
                $COLUMN_REFEICAO_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USUARIO_ID INTEGER NOT NULL,
                $COLUMN_REFEICAO_DIA TEXT NOT NULL DEFAULT '',
                $COLUMN_REFEICAO_PERIODO TEXT NOT NULL,
                $COLUMN_REFEICAO_TITULO TEXT NOT NULL,
                $COLUMN_REFEICAO_CALORIAS TEXT NOT NULL,
                $COLUMN_REFEICAO_HORARIO TEXT NOT NULL,
                $COLUMN_REFEICAO_IMAGEM INTEGER NOT NULL,
                $COLUMN_REFEICAO_INGREDIENTES TEXT NOT NULL DEFAULT '',
                $COLUMN_REFEICAO_INSTRUCOES TEXT NOT NULL DEFAULT '',
                $COLUMN_REFEICAO_CONCLUIDA INTEGER NOT NULL DEFAULT 0
            )
            """.trimIndent()
        )
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS $TABLE_NOTIFICACOES (
                $COLUMN_NOTIFICACAO_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USUARIO_ID INTEGER NOT NULL,
                $COLUMN_NOTIFICACAO_TITULO TEXT NOT NULL,
                $COLUMN_NOTIFICACAO_MENSAGEM TEXT NOT NULL,
                $COLUMN_NOTIFICACAO_HORARIO TEXT NOT NULL,
                $COLUMN_NOTIFICACAO_TIPO TEXT NOT NULL,
                $COLUMN_NOTIFICACAO_LIDA INTEGER NOT NULL DEFAULT 0
            )
            """.trimIndent()
        )
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS $TABLE_HISTORICO (
                $COLUMN_HISTORICO_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USUARIO_ID INTEGER NOT NULL,
                $COLUMN_HISTORICO_DATA TEXT NOT NULL,
                $COLUMN_HISTORICO_TITULO TEXT NOT NULL,
                $COLUMN_HISTORICO_DURACAO TEXT NOT NULL,
                $COLUMN_HISTORICO_CALORIAS TEXT NOT NULL
            )
            """.trimIndent()
        )
    }

    /**
     * Cria um novo usuário. Retorna o id gerado, ou -1L se o e-mail já estiver cadastrado.
     */
    fun cadastrarUsuario(
        nome: String,
        email: String,
        senha: String,
        dataNascimento: String = "",
        genero: String = ""
    ): Long {
        val valores = ContentValues().apply {
            put(COLUMN_EMAIL, normalizarEmail(email))
            put(COLUMN_SENHA, hashSenha(senha))
            put(COLUMN_NOME, nome.trim())
            put(COLUMN_DATA_NASCIMENTO, dataNascimento)
            put(COLUMN_GENERO, genero)
            // Grava o ano em que a conta foi criada (ex.: "2026"), em vez de
            // deixar o valor padrão 'Recente' da coluna no banco.
            put(COLUMN_TEMPO_CADASTRADO, anoAtual())
        }
        return try {
            val id = writableDatabase.insertOrThrow(TABLE_USUARIOS, null, valores)
            if (id != -1L) garantirDadosUsuario(writableDatabase, id)
            id
        } catch (e: SQLiteConstraintException) {
            -1L // e-mail já existe (coluna é UNIQUE)
        }
    }

    /**
     * Verifica e-mail e senha. Retorna o perfil do usuário se as credenciais
     * baterem, ou null caso contrário.
     */
    fun autenticarUsuario(email: String, senha: String): PerfilCliente? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_USUARIOS,
            null,
            "$COLUMN_EMAIL = ? AND $COLUMN_SENHA = ?",
            arrayOf(normalizarEmail(email), hashSenha(senha)),
            null, null, null
        )
        return cursor.use { if (it.moveToFirst()) it.paraPerfilCliente() else null }
    }

    /**
     * Busca o perfil pelo id. Útil para recarregar os dados atualizados do usuário logado.
     */
    fun buscarPerfilPorId(id: Long): PerfilCliente? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_USUARIOS,
            null,
            "$COLUMN_ID = ?",
            arrayOf(id.toString()),
            null, null, null
        )
        return cursor.use { if (it.moveToFirst()) it.paraPerfilCliente() else null }
    }

    /**
     * Atualiza os dados editáveis do perfil (usado pela tela "Editar Dados").
     * Retorna true se o registro foi encontrado e alterado.
     */
    fun atualizarPerfil(
        id: Long,
        nome: String,
        dataNascimento: String,
        genero: String,
        pesoAtual: Double,
        objetivoPeso: Double,
        foco: String,
        altura: Double = 1.75,
        email: String? = null
    ): Boolean {
        val valores = ContentValues().apply {
            put(COLUMN_NOME, nome.trim())
            put(COLUMN_DATA_NASCIMENTO, dataNascimento)
            put(COLUMN_GENERO, genero)
            put(COLUMN_PESO_ATUAL, pesoAtual)
            put(COLUMN_OBJETIVO_PESO, objetivoPeso)
            put(COLUMN_FOCO, foco)
            put(COLUMN_ALTURA, altura)
            email?.let { put(COLUMN_EMAIL, it.trim()) }
        }
        val linhasAfetadas = writableDatabase.update(
            TABLE_USUARIOS,
            valores,
            "$COLUMN_ID = ?",
            arrayOf(id.toString())
        )
        return linhasAfetadas > 0
    }

    /**
     * Atualiza apenas a foto de perfil (usado ao trocar a foto na tela de Perfil).
     * [caminhoArquivo] é o caminho local do arquivo de imagem já copiado para o
     * armazenamento interno do app (ver PerfilScreen/AppNavigation). Retorna true
     * se o registro foi encontrado e alterado.
     */
    fun atualizarFotoPerfil(id: Long, caminhoArquivo: String): Boolean {
        val valores = ContentValues().apply {
            put(COLUMN_FOTO_URI, caminhoArquivo)
        }
        val linhasAfetadas = writableDatabase.update(
            TABLE_USUARIOS,
            valores,
            "$COLUMN_ID = ?",
            arrayOf(id.toString())
        )
        return linhasAfetadas > 0
    }

    fun atualizarPreferenciasPlano(
        id: Long,
        objetivo: String,
        frequencia: String,
        restricao: String
    ): Boolean {
        val valores = ContentValues().apply {
            put(COLUMN_OBJETIVO_PLANO, objetivo)
            put(COLUMN_FREQUENCIA_TREINO, frequencia)
            put(COLUMN_RESTRICAO_ALIMENTAR, restricao)
        }
        return writableDatabase.update(
            TABLE_USUARIOS,
            valores,
            "$COLUMN_ID = ?",
            arrayOf(id.toString())
        ) > 0
    }

    fun atualizarDieta(
        usuarioId: Long,
        metaCalorias: Int,
        refeicoesPorDia: Int,
        objetivo: String,
        restricao: String,
        observacoes: String
    ): Boolean {
        val valores = ContentValues().apply {
            put(COLUMN_META_CALORIAS, metaCalorias)
            put(COLUMN_REFEICOES_POR_DIA, refeicoesPorDia)
            put(COLUMN_OBJETIVO_DIETA, objetivo)
            put(COLUMN_RESTRICAO_ALIMENTAR, restricao)
            put(COLUMN_OBSERVACOES_DIETA, observacoes)
        }
        return writableDatabase.update(
            TABLE_USUARIOS,
            valores,
            "$COLUMN_ID = ?",
            arrayOf(usuarioId.toString())
        ) > 0
    }

    fun adicionarAgua(usuarioId: Long, ml: Int): Int {
        val db = writableDatabase
        db.execSQL(
            "UPDATE $TABLE_USUARIOS SET $COLUMN_AGUA_ML = $COLUMN_AGUA_ML + ? WHERE $COLUMN_ID = ?",
            arrayOf(ml, usuarioId)
        )
        return buscarPerfilPorId(usuarioId)?.aguaMl ?: 0
    }

    fun listarExercicios(): List<Exercicio> {
        val db = readableDatabase
        garantirExercicios(db)
        val cursor = db.query(TABLE_EXERCICIOS, null, null, null, null, null, "$COLUMN_EXERCICIO_ID ASC")
        return cursor.use {
            buildList {
                while (it.moveToNext()) add(it.paraExercicio())
            }
        }
    }

    fun buscarExercicio(id: Int): Exercicio? {
        val cursor = readableDatabase.query(
            TABLE_EXERCICIOS,
            null,
            "$COLUMN_EXERCICIO_ID = ?",
            arrayOf(id.toString()),
            null, null, null
        )
        return cursor.use { if (it.moveToFirst()) it.paraExercicio() else null }
    }

    fun listarTreinos(usuarioId: Long): List<TreinoDia> {
        val db = readableDatabase
        garantirDadosUsuario(db, usuarioId)
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
                val exercicios = listarExerciciosDoTreino(db, id)
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
        val db = writableDatabase
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
        substituirExerciciosDoTreino(db, treinoId, exercicioIds)
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
        val db = writableDatabase
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
        if (linhas > 0) substituirExerciciosDoTreino(db, treinoId.toLong(), exercicioIds)
        return linhas > 0
    }

    fun removerTreino(usuarioId: Long, treinoId: Int): Boolean {
        val db = writableDatabase
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

    fun listarRefeicoes(usuarioId: Long): List<RefeicaoDia> {
        val db = readableDatabase
        garantirDadosUsuario(db, usuarioId)
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
        val cursor = readableDatabase.query(
            TABLE_REFEICOES,
            null,
            "$COLUMN_REFEICAO_ID = ? AND $COLUMN_USUARIO_ID = ?",
            arrayOf(refeicaoId.toString(), usuarioId.toString()),
            null, null, null
        )
        return cursor.use { if (it.moveToFirst()) it.paraRefeicao() else null }
    }

    fun concluirRefeicao(usuarioId: Long, refeicaoId: Long): Boolean =
        writableDatabase.update(
            TABLE_REFEICOES,
            ContentValues().apply { put(COLUMN_REFEICAO_CONCLUIDA, 1) },
            "$COLUMN_REFEICAO_ID = ? AND $COLUMN_USUARIO_ID = ?",
            arrayOf(refeicaoId.toString(), usuarioId.toString())
        ) > 0

    fun listarNotificacoes(usuarioId: Long): List<Notificacao> {
        val db = readableDatabase
        garantirDadosUsuario(db, usuarioId)
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
        writableDatabase.update(
            TABLE_NOTIFICACOES,
            ContentValues().apply { put(COLUMN_NOTIFICACAO_LIDA, 1) },
            "$COLUMN_NOTIFICACAO_ID = ? AND $COLUMN_USUARIO_ID = ?",
            arrayOf(notificacaoId.toString(), usuarioId.toString())
        ) > 0

    fun listarHistorico(usuarioId: Long): List<HistoricoTreino> {
        val db = readableDatabase
        garantirDadosUsuario(db, usuarioId)
        val cursor = db.query(
            TABLE_HISTORICO,
            null,
            "$COLUMN_USUARIO_ID = ?",
            arrayOf(usuarioId.toString()),
            null, null, "$COLUMN_HISTORICO_ID DESC"
        )
        return cursor.use {
            buildList {
                while (it.moveToNext()) add(it.paraHistorico())
            }
        }
    }

    fun registrarTreinoConcluido(
        usuarioId: Long,
        titulo: String,
        duracao: String,
        calorias: String,
        data: String = "Hoje"
    ): Long {
        val db = writableDatabase
        val id = db.insertOrThrow(
            TABLE_HISTORICO,
            null,
            ContentValues().apply {
                put(COLUMN_USUARIO_ID, usuarioId)
                put(COLUMN_HISTORICO_DATA, data)
                put(COLUMN_HISTORICO_TITULO, titulo)
                put(COLUMN_HISTORICO_DURACAO, duracao)
                put(COLUMN_HISTORICO_CALORIAS, calorias)
            }
        )
        db.execSQL(
            "UPDATE $TABLE_USUARIOS SET $COLUMN_TREINOS_REALIZADOS = $COLUMN_TREINOS_REALIZADOS + 1 WHERE $COLUMN_ID = ?",
            arrayOf(usuarioId)
        )
        return id
    }

    private fun listarExerciciosDoTreino(db: SQLiteDatabase, treinoId: Int): List<Exercicio> {
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

    private fun substituirExerciciosDoTreino(
        db: SQLiteDatabase,
        treinoId: Long,
        exercicioIds: List<Int>
    ) {
        db.delete(TABLE_TREINO_EXERCICIOS, "$COLUMN_TREINO_ID = ?", arrayOf(treinoId.toString()))
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

    private fun garantirExercicios(db: SQLiteDatabase) {
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

    private fun garantirDadosUsuario(db: SQLiteDatabase, usuarioId: Long) {
        if (contarPorUsuario(db, TABLE_TREINOS, usuarioId) == 0) {
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
                substituirExerciciosDoTreino(db, treinoId, treino.exercicios.map { it.id })
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
    }

    private fun contarPorUsuario(db: SQLiteDatabase, tabela: String, usuarioId: Long): Int =
        db.rawQuery(
            "SELECT COUNT(*) FROM $tabela WHERE $COLUMN_USUARIO_ID = ?",
            arrayOf(usuarioId.toString())
        ).use { if (it.moveToFirst()) it.getInt(0) else 0 }

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

    private fun Cursor.paraHistorico(): HistoricoTreino = HistoricoTreino(
        id = getLong(getColumnIndexOrThrow(COLUMN_HISTORICO_ID)),
        data = getString(getColumnIndexOrThrow(COLUMN_HISTORICO_DATA)),
        titulo = getString(getColumnIndexOrThrow(COLUMN_HISTORICO_TITULO)),
        duracao = getString(getColumnIndexOrThrow(COLUMN_HISTORICO_DURACAO)),
        calorias = getString(getColumnIndexOrThrow(COLUMN_HISTORICO_CALORIAS))
    )

    private fun Cursor.paraPerfilCliente(): PerfilCliente = PerfilCliente(
        id = getLong(getColumnIndexOrThrow(COLUMN_ID)),
        nome = getString(getColumnIndexOrThrow(COLUMN_NOME)),
        email = getString(getColumnIndexOrThrow(COLUMN_EMAIL)),
        dataNascimento = getString(getColumnIndexOrThrow(COLUMN_DATA_NASCIMENTO)),
        genero = getString(getColumnIndexOrThrow(COLUMN_GENERO)),
        pesoAtual = getDouble(getColumnIndexOrThrow(COLUMN_PESO_ATUAL)),
        objetivoPeso = getDouble(getColumnIndexOrThrow(COLUMN_OBJETIVO_PESO)),
        treinosRealizados = getInt(getColumnIndexOrThrow(COLUMN_TREINOS_REALIZADOS)),
        treinosPendentes = getInt(getColumnIndexOrThrow(COLUMN_TREINOS_PENDENTES)),
        nivel = getString(getColumnIndexOrThrow(COLUMN_NIVEL)),
        tipoPlano = getString(getColumnIndexOrThrow(COLUMN_TIPO_PLANO)),
        tempoCadastrado = getString(getColumnIndexOrThrow(COLUMN_TEMPO_CADASTRADO)),
        progresso = getDouble(getColumnIndexOrThrow(COLUMN_PROGRESSO)),
        foco = getString(getColumnIndexOrThrow(COLUMN_FOCO)),
        fotoUri = getString(getColumnIndexOrThrow(COLUMN_FOTO_URI)),
        altura = getDouble(getColumnIndexOrThrow(COLUMN_ALTURA)),
        aguaMl = getInt(getColumnIndexOrThrow(COLUMN_AGUA_ML)),
        metaCalorias = getInt(getColumnIndexOrThrow(COLUMN_META_CALORIAS)),
        refeicoesPorDia = getInt(getColumnIndexOrThrow(COLUMN_REFEICOES_POR_DIA)),
        objetivoDieta = getString(getColumnIndexOrThrow(COLUMN_OBJETIVO_DIETA)),
        restricaoAlimentar = getString(getColumnIndexOrThrow(COLUMN_RESTRICAO_ALIMENTAR)),
        observacoesDieta = getString(getColumnIndexOrThrow(COLUMN_OBSERVACOES_DIETA)),
        objetivoPlano = getString(getColumnIndexOrThrow(COLUMN_OBJETIVO_PLANO)),
        frequenciaTreino = getString(getColumnIndexOrThrow(COLUMN_FREQUENCIA_TREINO))
    )

    private fun normalizarEmail(email: String): String = email.trim().lowercase()

    // Guarda apenas o hash da senha (SHA-256), nunca o texto puro.
    private fun hashSenha(senha: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(senha.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    // Ano atual (ex.: "2026"), usado para preencher "Aluno desde <ano>" no momento do cadastro.
    private fun anoAtual(): String = Calendar.getInstance().get(Calendar.YEAR).toString()

    companion object {
        private const val DATABASE_NAME = "fitunity.db"
        private const val DATABASE_VERSION = 4

        const val TABLE_USUARIOS = "usuarios"
        const val TABLE_EXERCICIOS = "exercicios"
        const val TABLE_TREINOS = "treinos"
        const val TABLE_TREINO_EXERCICIOS = "treino_exercicios"
        const val TABLE_REFEICOES = "refeicoes"
        const val TABLE_NOTIFICACOES = "notificacoes"
        const val TABLE_HISTORICO = "historico_treinos"

        const val COLUMN_ID = "id"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_SENHA = "senha"
        const val COLUMN_NOME = "nome"
        const val COLUMN_DATA_NASCIMENTO = "data_nascimento"
        const val COLUMN_GENERO = "genero"

        const val COLUMN_PESO_ATUAL = "peso_atual"
        const val COLUMN_OBJETIVO_PESO = "objetivo_peso"
        const val COLUMN_TREINOS_REALIZADOS = "treinos_realizados"
        const val COLUMN_TREINOS_PENDENTES = "treinos_pendentes"
        const val COLUMN_NIVEL = "nivel"
        const val COLUMN_TIPO_PLANO = "tipo_plano"
        const val COLUMN_TEMPO_CADASTRADO = "tempo_cadastrado"
        const val COLUMN_PROGRESSO = "progresso"
        const val COLUMN_FOCO = "foco"
        const val COLUMN_FOTO_URI = "foto_uri"
        const val COLUMN_ALTURA = "altura"
        const val COLUMN_AGUA_ML = "agua_ml"
        const val COLUMN_META_CALORIAS = "meta_calorias"
        const val COLUMN_REFEICOES_POR_DIA = "refeicoes_por_dia"
        const val COLUMN_OBJETIVO_DIETA = "objetivo_dieta"
        const val COLUMN_RESTRICAO_ALIMENTAR = "restricao_alimentar"
        const val COLUMN_OBSERVACOES_DIETA = "observacoes_dieta"
        const val COLUMN_OBJETIVO_PLANO = "objetivo_plano"
        const val COLUMN_FREQUENCIA_TREINO = "frequencia_treino"

        const val COLUMN_USUARIO_ID = "usuario_id"

        const val COLUMN_EXERCICIO_ID = "exercicio_id"
        const val COLUMN_EXERCICIO_NOME = "nome"
        const val COLUMN_EXERCICIO_GRUPO = "grupo"
        const val COLUMN_EXERCICIO_SERIES = "series"
        const val COLUMN_EXERCICIO_REPETICOES = "repeticoes"
        const val COLUMN_EXERCICIO_INTERVALO = "intervalo"
        const val COLUMN_EXERCICIO_CARGA = "carga"
        const val COLUMN_EXERCICIO_IMAGEM = "imagem"
        const val COLUMN_EXERCICIO_INSTRUCOES = "instrucoes"

        const val COLUMN_TREINO_ID = "treino_id"
        const val COLUMN_TREINO_DIA = "dia_semana"
        const val COLUMN_TREINO_TITULO = "titulo"
        const val COLUMN_TREINO_DESCRICAO = "descricao"
        const val COLUMN_TREINO_DURACAO = "duracao"
        const val COLUMN_TREINO_IMAGEM = "imagem"
        const val COLUMN_TREINO_FOCO = "foco"
        const val COLUMN_ORDEM = "ordem"

        const val COLUMN_REFEICAO_ID = "refeicao_id"
        const val COLUMN_REFEICAO_DIA = "dia_semana"
        const val COLUMN_REFEICAO_PERIODO = "periodo"
        const val COLUMN_REFEICAO_TITULO = "titulo"
        const val COLUMN_REFEICAO_CALORIAS = "calorias"
        const val COLUMN_REFEICAO_HORARIO = "horario"
        const val COLUMN_REFEICAO_IMAGEM = "imagem"
        const val COLUMN_REFEICAO_INGREDIENTES = "ingredientes"
        const val COLUMN_REFEICAO_INSTRUCOES = "instrucoes"
        const val COLUMN_REFEICAO_CONCLUIDA = "concluida"

        const val COLUMN_NOTIFICACAO_ID = "notificacao_id"
        const val COLUMN_NOTIFICACAO_TITULO = "titulo"
        const val COLUMN_NOTIFICACAO_MENSAGEM = "mensagem"
        const val COLUMN_NOTIFICACAO_HORARIO = "horario"
        const val COLUMN_NOTIFICACAO_TIPO = "tipo"
        const val COLUMN_NOTIFICACAO_LIDA = "lida"

        const val COLUMN_HISTORICO_ID = "historico_id"
        const val COLUMN_HISTORICO_DATA = "data"
        const val COLUMN_HISTORICO_TITULO = "titulo"
        const val COLUMN_HISTORICO_DURACAO = "duracao"
        const val COLUMN_HISTORICO_CALORIAS = "calorias"
    }
}