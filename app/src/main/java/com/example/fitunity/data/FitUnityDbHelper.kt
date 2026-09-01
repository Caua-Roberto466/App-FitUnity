package com.example.fitunity.data

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