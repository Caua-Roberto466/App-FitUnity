package com.example.fitunity.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.security.MessageDigest

// Modelo de dados com as informações visíveis do perfil
data class PerfilCliente(
    val id: Long,
    val nome: String,
    val pesoAtual: Double,
    val objetivoPeso: Double,
    val treinosRealizados: Int,
    val treinosPendentes: Int,
    val nivel: String,         // Iniciante, Intermediário, Avançado
    val tipoPlano: String,     // Básico, Adaptado, Profissional
    val tempoCadastrado: String,
    val progresso: Double      // Porcentagem
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
                $COLUMN_PROGRESSO REAL DEFAULT 0.0
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USUARIOS")
        onCreate(db)
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
        }
        return try {
            writableDatabase.insertOrThrow(TABLE_USUARIOS, null, valores)
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

    private fun Cursor.paraPerfilCliente(): PerfilCliente = PerfilCliente(
        id = getLong(getColumnIndexOrThrow(COLUMN_ID)),
        nome = getString(getColumnIndexOrThrow(COLUMN_NOME)),
        pesoAtual = getDouble(getColumnIndexOrThrow(COLUMN_PESO_ATUAL)),
        objetivoPeso = getDouble(getColumnIndexOrThrow(COLUMN_OBJETIVO_PESO)),
        treinosRealizados = getInt(getColumnIndexOrThrow(COLUMN_TREINOS_REALIZADOS)),
        treinosPendentes = getInt(getColumnIndexOrThrow(COLUMN_TREINOS_PENDENTES)),
        nivel = getString(getColumnIndexOrThrow(COLUMN_NIVEL)),
        tipoPlano = getString(getColumnIndexOrThrow(COLUMN_TIPO_PLANO)),
        tempoCadastrado = getString(getColumnIndexOrThrow(COLUMN_TEMPO_CADASTRADO)),
        progresso = getDouble(getColumnIndexOrThrow(COLUMN_PROGRESSO))
    )

    private fun normalizarEmail(email: String): String = email.trim().lowercase()

    // Guarda apenas o hash da senha (SHA-256), nunca o texto puro.
    private fun hashSenha(senha: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(senha.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    companion object {
        private const val DATABASE_NAME = "fitunity.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_USUARIOS = "usuarios"

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
    }
}
