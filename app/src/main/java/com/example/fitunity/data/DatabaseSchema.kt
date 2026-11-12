package com.example.fitunity.data

import android.database.sqlite.SQLiteDatabase
import com.example.fitunity.data.DbContract.COLUMN_AGUA_ML
import com.example.fitunity.data.DbContract.COLUMN_ALTURA
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
import com.example.fitunity.data.DbContract.COLUMN_CONVERSA_ALUNO_ID
import com.example.fitunity.data.DbContract.COLUMN_CONVERSA_CRIADA_EM
import com.example.fitunity.data.DbContract.COLUMN_CONVERSA_HORARIO
import com.example.fitunity.data.DbContract.COLUMN_CONVERSA_ID
import com.example.fitunity.data.DbContract.COLUMN_CONVERSA_PERSONAL_ID
import com.example.fitunity.data.DbContract.COLUMN_CONVERSA_REMETENTE
import com.example.fitunity.data.DbContract.COLUMN_CONVERSA_TEXTO
import com.example.fitunity.data.DbContract.COLUMN_DATA_NASCIMENTO
import com.example.fitunity.data.DbContract.COLUMN_EMAIL
import com.example.fitunity.data.DbContract.COLUMN_ESCOLHIDO_EM
import com.example.fitunity.data.DbContract.COLUMN_EXERCICIO_CARGA
import com.example.fitunity.data.DbContract.COLUMN_EXERCICIO_GRUPO
import com.example.fitunity.data.DbContract.COLUMN_EXERCICIO_ID
import com.example.fitunity.data.DbContract.COLUMN_EXERCICIO_IMAGEM
import com.example.fitunity.data.DbContract.COLUMN_EXERCICIO_INSTRUCOES
import com.example.fitunity.data.DbContract.COLUMN_EXERCICIO_INTERVALO
import com.example.fitunity.data.DbContract.COLUMN_EXERCICIO_NOME
import com.example.fitunity.data.DbContract.COLUMN_EXERCICIO_REPETICOES
import com.example.fitunity.data.DbContract.COLUMN_EXERCICIO_SERIES
import com.example.fitunity.data.DbContract.COLUMN_FOCO
import com.example.fitunity.data.DbContract.COLUMN_FOTO_URI
import com.example.fitunity.data.DbContract.COLUMN_FREQUENCIA_TREINO
import com.example.fitunity.data.DbContract.COLUMN_GENERO
import com.example.fitunity.data.DbContract.COLUMN_HISTORICO_CALORIAS
import com.example.fitunity.data.DbContract.COLUMN_HISTORICO_DATA
import com.example.fitunity.data.DbContract.COLUMN_HISTORICO_DURACAO
import com.example.fitunity.data.DbContract.COLUMN_HISTORICO_ID
import com.example.fitunity.data.DbContract.COLUMN_HISTORICO_TITULO
import com.example.fitunity.data.DbContract.COLUMN_ID
import com.example.fitunity.data.DbContract.COLUMN_MENSAGEM_CRIADA_EM
import com.example.fitunity.data.DbContract.COLUMN_MENSAGEM_HORARIO
import com.example.fitunity.data.DbContract.COLUMN_MENSAGEM_ID
import com.example.fitunity.data.DbContract.COLUMN_MENSAGEM_REMETENTE
import com.example.fitunity.data.DbContract.COLUMN_MENSAGEM_TEXTO
import com.example.fitunity.data.DbContract.COLUMN_META_CALORIAS
import com.example.fitunity.data.DbContract.COLUMN_NIVEL
import com.example.fitunity.data.DbContract.COLUMN_NOME
import com.example.fitunity.data.DbContract.COLUMN_NOTIFICACAO_HORARIO
import com.example.fitunity.data.DbContract.COLUMN_NOTIFICACAO_ID
import com.example.fitunity.data.DbContract.COLUMN_NOTIFICACAO_LIDA
import com.example.fitunity.data.DbContract.COLUMN_NOTIFICACAO_MENSAGEM
import com.example.fitunity.data.DbContract.COLUMN_NOTIFICACAO_TIPO
import com.example.fitunity.data.DbContract.COLUMN_NOTIFICACAO_TITULO
import com.example.fitunity.data.DbContract.COLUMN_OBJETIVO_DIETA
import com.example.fitunity.data.DbContract.COLUMN_OBJETIVO_PESO
import com.example.fitunity.data.DbContract.COLUMN_OBJETIVO_PLANO
import com.example.fitunity.data.DbContract.COLUMN_OBSERVACOES_DIETA
import com.example.fitunity.data.DbContract.COLUMN_ORDEM
import com.example.fitunity.data.DbContract.COLUMN_PERSONAL_ID
import com.example.fitunity.data.DbContract.COLUMN_PESO_ATUAL
import com.example.fitunity.data.DbContract.COLUMN_PROGRESSO
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
import com.example.fitunity.data.DbContract.COLUMN_REFEICOES_POR_DIA
import com.example.fitunity.data.DbContract.COLUMN_RESTRICAO_ALIMENTAR
import com.example.fitunity.data.DbContract.COLUMN_SENHA
import com.example.fitunity.data.DbContract.COLUMN_TEMPO_CADASTRADO
import com.example.fitunity.data.DbContract.COLUMN_TIPO_PLANO
import com.example.fitunity.data.DbContract.COLUMN_TREINO_DESCRICAO
import com.example.fitunity.data.DbContract.COLUMN_TREINO_DIA
import com.example.fitunity.data.DbContract.COLUMN_TREINO_DURACAO
import com.example.fitunity.data.DbContract.COLUMN_TREINO_FOCO
import com.example.fitunity.data.DbContract.COLUMN_TREINO_ID
import com.example.fitunity.data.DbContract.COLUMN_TREINO_IMAGEM
import com.example.fitunity.data.DbContract.COLUMN_TREINO_TITULO
import com.example.fitunity.data.DbContract.COLUMN_TREINOS_PENDENTES
import com.example.fitunity.data.DbContract.COLUMN_TREINOS_REALIZADOS
import com.example.fitunity.data.DbContract.COLUMN_USUARIO_ID
import com.example.fitunity.data.DbContract.TABLE_CANDIDATURAS_PERSONAL
import com.example.fitunity.data.DbContract.TABLE_CONVERSAS_PERSONAL_ALUNO
import com.example.fitunity.data.DbContract.TABLE_ESTATISTICAS
import com.example.fitunity.data.DbContract.TABLE_EXERCICIOS
import com.example.fitunity.data.DbContract.TABLE_HISTORICO
import com.example.fitunity.data.DbContract.TABLE_MENSAGENS_PERSONAL
import com.example.fitunity.data.DbContract.TABLE_NOTIFICACOES
import com.example.fitunity.data.DbContract.TABLE_PERFIS_FISICOS
import com.example.fitunity.data.DbContract.TABLE_PERSONAL_ESCOLHIDO
import com.example.fitunity.data.DbContract.TABLE_PREFERENCIAS_DIETA
import com.example.fitunity.data.DbContract.TABLE_PREFERENCIAS_PLANO
import com.example.fitunity.data.DbContract.TABLE_REFEICOES
import com.example.fitunity.data.DbContract.TABLE_TREINOS
import com.example.fitunity.data.DbContract.TABLE_TREINO_EXERCICIOS
import com.example.fitunity.data.DbContract.TABLE_USUARIOS

/**
 * Responsável apenas por criar e migrar a estrutura do banco.
 * O schema é dividido por responsabilidade:
 * - usuarios: autenticação e identidade
 * - perfis_fisicos: medidas e características físicas
 * - estatisticas_usuario: números de evolução
 * - preferencias_dieta / preferencias_plano: preferências do usuário
 * - tabelas de conteúdo: exercícios, treinos, refeições, notificações etc.
 */
object DatabaseSchema {

    fun criar(db: SQLiteDatabase) {
        criarTabelaUsuarios(db)
        criarTabelasPerfil(db)
        criarTabelasPersonal(db)
        criarTabelaPersonalEscolhido(db)
        criarTabelasConteudo(db)
        criarIndices(db)
    }

    fun migrar(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
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
            DatabaseSeeder.garantirExercicios(db)
        }
        if (oldVersion < 5) {
            migrarParaV5(db)
        }
        if (oldVersion < 6) {
            criarTabelasPersonal(db)
            criarIndices(db)
        }
        if (oldVersion < 7) {
            criarTabelaPersonalEscolhido(db)
            criarIndices(db)
        }
    }

    private fun criarTabelaUsuarios(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS $TABLE_USUARIOS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_EMAIL TEXT UNIQUE NOT NULL,
                $COLUMN_SENHA TEXT NOT NULL,
                $COLUMN_NOME TEXT NOT NULL,
                $COLUMN_DATA_NASCIMENTO TEXT NOT NULL,
                $COLUMN_GENERO TEXT NOT NULL,
                $COLUMN_FOTO_URI TEXT DEFAULT NULL,
                $COLUMN_TEMPO_CADASTRADO TEXT DEFAULT 'Recente'
            )
            """.trimIndent()
        )
    }

    private fun criarTabelasPerfil(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS $TABLE_PERFIS_FISICOS (
                $COLUMN_USUARIO_ID INTEGER PRIMARY KEY,
                $COLUMN_PESO_ATUAL REAL DEFAULT 0.0,
                $COLUMN_OBJETIVO_PESO REAL DEFAULT 0.0,
                $COLUMN_ALTURA REAL DEFAULT 1.75,
                $COLUMN_FOCO TEXT DEFAULT 'Hipertrofia',
                $COLUMN_NIVEL TEXT DEFAULT 'Iniciante',
                $COLUMN_TIPO_PLANO TEXT DEFAULT 'Básico',
                FOREIGN KEY ($COLUMN_USUARIO_ID) REFERENCES $TABLE_USUARIOS ($COLUMN_ID) ON DELETE CASCADE
            )
            """.trimIndent()
        )
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS $TABLE_ESTATISTICAS (
                $COLUMN_USUARIO_ID INTEGER PRIMARY KEY,
                $COLUMN_TREINOS_REALIZADOS INTEGER DEFAULT 0,
                $COLUMN_TREINOS_PENDENTES INTEGER DEFAULT 0,
                $COLUMN_PROGRESSO REAL DEFAULT 0.0,
                $COLUMN_AGUA_ML INTEGER DEFAULT 0,
                FOREIGN KEY ($COLUMN_USUARIO_ID) REFERENCES $TABLE_USUARIOS ($COLUMN_ID) ON DELETE CASCADE
            )
            """.trimIndent()
        )
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS $TABLE_PREFERENCIAS_DIETA (
                $COLUMN_USUARIO_ID INTEGER PRIMARY KEY,
                $COLUMN_META_CALORIAS INTEGER DEFAULT 2200,
                $COLUMN_REFEICOES_POR_DIA INTEGER DEFAULT 4,
                $COLUMN_OBJETIVO_DIETA TEXT DEFAULT 'Manter',
                $COLUMN_RESTRICAO_ALIMENTAR TEXT DEFAULT 'Nenhuma',
                $COLUMN_OBSERVACOES_DIETA TEXT DEFAULT '',
                FOREIGN KEY ($COLUMN_USUARIO_ID) REFERENCES $TABLE_USUARIOS ($COLUMN_ID) ON DELETE CASCADE
            )
            """.trimIndent()
        )
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS $TABLE_PREFERENCIAS_PLANO (
                $COLUMN_USUARIO_ID INTEGER PRIMARY KEY,
                $COLUMN_OBJETIVO_PLANO TEXT DEFAULT 'Manter a saúde',
                $COLUMN_FREQUENCIA_TREINO TEXT DEFAULT '4 vezes por semana',
                FOREIGN KEY ($COLUMN_USUARIO_ID) REFERENCES $TABLE_USUARIOS ($COLUMN_ID) ON DELETE CASCADE
            )
            """.trimIndent()
        )
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS $TABLE_MENSAGENS_PERSONAL (
                $COLUMN_MENSAGEM_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USUARIO_ID INTEGER NOT NULL,
                $COLUMN_MENSAGEM_REMETENTE TEXT NOT NULL,
                $COLUMN_MENSAGEM_TEXTO TEXT NOT NULL,
                $COLUMN_MENSAGEM_HORARIO TEXT NOT NULL,
                $COLUMN_MENSAGEM_CRIADA_EM INTEGER NOT NULL,
                FOREIGN KEY ($COLUMN_USUARIO_ID) REFERENCES $TABLE_USUARIOS ($COLUMN_ID) ON DELETE CASCADE
            )
            """.trimIndent()
        )
    }

    private fun criarTabelasPersonal(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS $TABLE_CANDIDATURAS_PERSONAL (
                $COLUMN_CANDIDATURA_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USUARIO_ID INTEGER NOT NULL UNIQUE,
                $COLUMN_CANDIDATURA_NOME TEXT NOT NULL,
                $COLUMN_CANDIDATURA_EMAIL TEXT NOT NULL,
                $COLUMN_CANDIDATURA_TELEFONE TEXT NOT NULL,
                $COLUMN_CANDIDATURA_CREF TEXT NOT NULL,
                $COLUMN_CANDIDATURA_ESPECIALIDADES TEXT NOT NULL,
                $COLUMN_CANDIDATURA_EXPERIENCIA TEXT NOT NULL,
                $COLUMN_CANDIDATURA_APRESENTACAO TEXT NOT NULL,
                $COLUMN_CANDIDATURA_CURRICULO_URI TEXT DEFAULT NULL,
                $COLUMN_CANDIDATURA_CURRICULO_NOME TEXT DEFAULT NULL,
                $COLUMN_CANDIDATURA_STATUS TEXT NOT NULL DEFAULT 'EM_ANALISE',
                $COLUMN_CANDIDATURA_CRIADA_EM INTEGER NOT NULL,
                $COLUMN_CANDIDATURA_ATUALIZADA_EM INTEGER NOT NULL,
                FOREIGN KEY ($COLUMN_USUARIO_ID) REFERENCES $TABLE_USUARIOS ($COLUMN_ID) ON DELETE CASCADE
            )
            """.trimIndent()
        )
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS $TABLE_CONVERSAS_PERSONAL_ALUNO (
                $COLUMN_CONVERSA_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_CONVERSA_PERSONAL_ID INTEGER NOT NULL,
                $COLUMN_CONVERSA_ALUNO_ID INTEGER NOT NULL,
                $COLUMN_CONVERSA_REMETENTE TEXT NOT NULL,
                $COLUMN_CONVERSA_TEXTO TEXT NOT NULL,
                $COLUMN_CONVERSA_HORARIO TEXT NOT NULL,
                $COLUMN_CONVERSA_CRIADA_EM INTEGER NOT NULL,
                FOREIGN KEY ($COLUMN_CONVERSA_PERSONAL_ID) REFERENCES $TABLE_USUARIOS ($COLUMN_ID) ON DELETE CASCADE,
                FOREIGN KEY ($COLUMN_CONVERSA_ALUNO_ID) REFERENCES $TABLE_USUARIOS ($COLUMN_ID) ON DELETE CASCADE
            )
            """.trimIndent()
        )
    }

    private fun criarTabelaPersonalEscolhido(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS $TABLE_PERSONAL_ESCOLHIDO (
                $COLUMN_USUARIO_ID INTEGER PRIMARY KEY,
                $COLUMN_PERSONAL_ID INTEGER NOT NULL,
                $COLUMN_ESCOLHIDO_EM INTEGER NOT NULL,
                FOREIGN KEY ($COLUMN_USUARIO_ID) REFERENCES $TABLE_USUARIOS ($COLUMN_ID) ON DELETE CASCADE,
                FOREIGN KEY ($COLUMN_PERSONAL_ID) REFERENCES $TABLE_USUARIOS ($COLUMN_ID) ON DELETE CASCADE
            )
            """.trimIndent()
        )
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

    private fun criarIndices(db: SQLiteDatabase) {
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_treinos_usuario ON $TABLE_TREINOS ($COLUMN_USUARIO_ID)")
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_refeicoes_usuario ON $TABLE_REFEICOES ($COLUMN_USUARIO_ID)")
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_notificacoes_usuario ON $TABLE_NOTIFICACOES ($COLUMN_USUARIO_ID)")
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_historico_usuario ON $TABLE_HISTORICO ($COLUMN_USUARIO_ID)")
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_mensagens_usuario ON $TABLE_MENSAGENS_PERSONAL ($COLUMN_USUARIO_ID)")
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_candidaturas_usuario ON $TABLE_CANDIDATURAS_PERSONAL ($COLUMN_USUARIO_ID)")
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_conversas_personal ON $TABLE_CONVERSAS_PERSONAL_ALUNO ($COLUMN_CONVERSA_PERSONAL_ID)")
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_conversas_aluno ON $TABLE_CONVERSAS_PERSONAL_ALUNO ($COLUMN_CONVERSA_ALUNO_ID)")
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_personal_escolhido_personal ON $TABLE_PERSONAL_ESCOLHIDO ($COLUMN_PERSONAL_ID)")
    }

    private fun migrarParaV5(db: SQLiteDatabase) {
        // Guarda os dados antigos antes de reconstruir a tabela usuarios.
        db.execSQL("CREATE TEMP TABLE usuarios_legado AS SELECT * FROM $TABLE_USUARIOS")

        db.execSQL(
            """
            CREATE TABLE usuarios_novo (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_EMAIL TEXT UNIQUE NOT NULL,
                $COLUMN_SENHA TEXT NOT NULL,
                $COLUMN_NOME TEXT NOT NULL,
                $COLUMN_DATA_NASCIMENTO TEXT NOT NULL,
                $COLUMN_GENERO TEXT NOT NULL,
                $COLUMN_FOTO_URI TEXT DEFAULT NULL,
                $COLUMN_TEMPO_CADASTRADO TEXT DEFAULT 'Recente'
            )
            """.trimIndent()
        )
        db.execSQL(
            """
            INSERT INTO usuarios_novo (
                $COLUMN_ID, $COLUMN_EMAIL, $COLUMN_SENHA, $COLUMN_NOME,
                $COLUMN_DATA_NASCIMENTO, $COLUMN_GENERO, $COLUMN_FOTO_URI, $COLUMN_TEMPO_CADASTRADO
            )
            SELECT
                $COLUMN_ID, $COLUMN_EMAIL, $COLUMN_SENHA, $COLUMN_NOME,
                $COLUMN_DATA_NASCIMENTO, $COLUMN_GENERO, $COLUMN_FOTO_URI, $COLUMN_TEMPO_CADASTRADO
            FROM usuarios_legado
            """.trimIndent()
        )
        db.execSQL("DROP TABLE $TABLE_USUARIOS")
        db.execSQL("ALTER TABLE usuarios_novo RENAME TO $TABLE_USUARIOS")

        criarTabelasPerfil(db)

        db.execSQL(
            """
            INSERT INTO $TABLE_PERFIS_FISICOS (
                $COLUMN_USUARIO_ID, $COLUMN_PESO_ATUAL, $COLUMN_OBJETIVO_PESO, $COLUMN_ALTURA,
                $COLUMN_FOCO, $COLUMN_NIVEL, $COLUMN_TIPO_PLANO
            )
            SELECT
                $COLUMN_ID, $COLUMN_PESO_ATUAL, $COLUMN_OBJETIVO_PESO, $COLUMN_ALTURA,
                $COLUMN_FOCO, $COLUMN_NIVEL, $COLUMN_TIPO_PLANO
            FROM usuarios_legado
            """.trimIndent()
        )
        db.execSQL(
            """
            INSERT INTO $TABLE_ESTATISTICAS (
                $COLUMN_USUARIO_ID, $COLUMN_TREINOS_REALIZADOS, $COLUMN_TREINOS_PENDENTES,
                $COLUMN_PROGRESSO, $COLUMN_AGUA_ML
            )
            SELECT
                $COLUMN_ID, $COLUMN_TREINOS_REALIZADOS, $COLUMN_TREINOS_PENDENTES,
                $COLUMN_PROGRESSO, $COLUMN_AGUA_ML
            FROM usuarios_legado
            """.trimIndent()
        )
        db.execSQL(
            """
            INSERT INTO $TABLE_PREFERENCIAS_DIETA (
                $COLUMN_USUARIO_ID, $COLUMN_META_CALORIAS, $COLUMN_REFEICOES_POR_DIA,
                $COLUMN_OBJETIVO_DIETA, $COLUMN_RESTRICAO_ALIMENTAR, $COLUMN_OBSERVACOES_DIETA
            )
            SELECT
                $COLUMN_ID, $COLUMN_META_CALORIAS, $COLUMN_REFEICOES_POR_DIA,
                $COLUMN_OBJETIVO_DIETA, $COLUMN_RESTRICAO_ALIMENTAR, $COLUMN_OBSERVACOES_DIETA
            FROM usuarios_legado
            """.trimIndent()
        )
        db.execSQL(
            """
            INSERT INTO $TABLE_PREFERENCIAS_PLANO (
                $COLUMN_USUARIO_ID, $COLUMN_OBJETIVO_PLANO, $COLUMN_FREQUENCIA_TREINO
            )
            SELECT
                $COLUMN_ID, $COLUMN_OBJETIVO_PLANO, $COLUMN_FREQUENCIA_TREINO
            FROM usuarios_legado
            """.trimIndent()
        )

        db.execSQL("DROP TABLE usuarios_legado")
        criarIndices(db)
    }
}
