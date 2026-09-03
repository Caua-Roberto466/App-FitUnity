package com.example.fitunity.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

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

    // ==========================================
    // 1. PARTE DEDICADA AO CADASTRO
    // ==========================================
    fun cadastrarUsuario(
        email: String,
        nome: String,
        dataNascimento: String,
        genero: String,
        senha: String
    ): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_EMAIL, email)
            put(COLUMN_NOME, nome)
            put(COLUMN_DATA_NASCIMENTO, dataNascimento)
            put(COLUMN_GENERO, genero)
            put(COLUMN_SENHA, senha)
            put(COLUMN_NIVEL, "Iniciante")
            put(COLUMN_TIPO_PLANO, "Básico")
        }
        return db.insert(TABLE_USUARIOS, null, values)
    }

    // ==========================================
    // 2. PARTE DEDICADA AO LOGIN
    // ==========================================
    fun realizarLogin(email: String, senha: String): Long? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_USUARIOS,
            arrayOf(COLUMN_ID),
            "$COLUMN_EMAIL = ? AND $COLUMN_SENHA = ?",
            arrayOf(email, senha),
            null,
            null,
            null
        )
        cursor.use {
            if (it.moveToFirst()) {
                return it.getLong(it.getColumnIndexOrThrow(COLUMN_ID))
            }
        }
        return null
    }

    // ==========================================
    // 3. PARTE DEDICADA AO PERFIL CLIENTE (DADOS VISÍVEIS)
    // ==========================================
    fun obterPerfilCliente(usuarioId: Long): PerfilCliente? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_USUARIOS,
            arrayOf(
                COLUMN_ID,
                COLUMN_NOME,
                COLUMN_PESO_ATUAL,
                COLUMN_OBJETIVO_PESO,
                COLUMN_TREINOS_REALIZADOS,
                COLUMN_TREINOS_PENDENTES,
                COLUMN_NIVEL,
                COLUMN_TIPO_PLANO,
                COLUMN_TEMPO_CADASTRADO,
                COLUMN_PROGRESSO
            ),
            "$COLUMN_ID = ?",
            arrayOf(usuarioId.toString()),
            null,
            null,
            null
        )

        cursor.use {
            if (it.moveToFirst()) {
                return PerfilCliente(
                    id = it.getLong(it.getColumnIndexOrThrow(COLUMN_ID)),
                    nome = it.getString(it.getColumnIndexOrThrow(COLUMN_NOME)),
                    pesoAtual = it.getDouble(it.getColumnIndexOrThrow(COLUMN_PESO_ATUAL)),
                    objetivoPeso = it.getDouble(it.getColumnIndexOrThrow(COLUMN_OBJETIVO_PESO)),
                    treinosRealizados = it.getInt(it.getColumnIndexOrThrow(COLUMN_TREINOS_REALIZADOS)),
                    treinosPendentes = it.getInt(it.getColumnIndexOrThrow(COLUMN_TREINOS_PENDENTES)),
                    nivel = it.getString(it.getColumnIndexOrThrow(COLUMN_NIVEL)),
                    tipoPlano = it.getString(it.getColumnIndexOrThrow(COLUMN_TIPO_PLANO)),
                    tempoCadastrado = it.getString(it.getColumnIndexOrThrow(COLUMN_TEMPO_CADASTRADO)),
                    progresso = it.getDouble(it.getColumnIndexOrThrow(COLUMN_PROGRESSO))
                )
            }
        }
        return null
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