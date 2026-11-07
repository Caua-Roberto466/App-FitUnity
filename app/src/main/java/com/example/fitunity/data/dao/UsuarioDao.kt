package com.example.fitunity.data.dao

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import com.example.fitunity.data.DatabaseSeeder
import com.example.fitunity.data.DbContract.COLUMN_AGUA_ML
import com.example.fitunity.data.DbContract.COLUMN_ALTURA
import com.example.fitunity.data.DbContract.COLUMN_DATA_NASCIMENTO
import com.example.fitunity.data.DbContract.COLUMN_EMAIL
import com.example.fitunity.data.DbContract.COLUMN_FOCO
import com.example.fitunity.data.DbContract.COLUMN_FOTO_URI
import com.example.fitunity.data.DbContract.COLUMN_FREQUENCIA_TREINO
import com.example.fitunity.data.DbContract.COLUMN_GENERO
import com.example.fitunity.data.DbContract.COLUMN_ID
import com.example.fitunity.data.DbContract.COLUMN_META_CALORIAS
import com.example.fitunity.data.DbContract.COLUMN_NIVEL
import com.example.fitunity.data.DbContract.COLUMN_NOME
import com.example.fitunity.data.DbContract.COLUMN_OBJETIVO_DIETA
import com.example.fitunity.data.DbContract.COLUMN_OBJETIVO_PESO
import com.example.fitunity.data.DbContract.COLUMN_OBJETIVO_PLANO
import com.example.fitunity.data.DbContract.COLUMN_OBSERVACOES_DIETA
import com.example.fitunity.data.DbContract.COLUMN_PESO_ATUAL
import com.example.fitunity.data.DbContract.COLUMN_PROGRESSO
import com.example.fitunity.data.DbContract.COLUMN_REFEICOES_POR_DIA
import com.example.fitunity.data.DbContract.COLUMN_RESTRICAO_ALIMENTAR
import com.example.fitunity.data.DbContract.COLUMN_SENHA
import com.example.fitunity.data.DbContract.COLUMN_TEMPO_CADASTRADO
import com.example.fitunity.data.DbContract.COLUMN_TIPO_PLANO
import com.example.fitunity.data.DbContract.COLUMN_TREINOS_PENDENTES
import com.example.fitunity.data.DbContract.COLUMN_TREINOS_REALIZADOS
import com.example.fitunity.data.DbContract.COLUMN_USUARIO_ID
import com.example.fitunity.data.DbContract.TABLE_ESTATISTICAS
import com.example.fitunity.data.DbContract.TABLE_PERFIS_FISICOS
import com.example.fitunity.data.DbContract.TABLE_PREFERENCIAS_DIETA
import com.example.fitunity.data.DbContract.TABLE_PREFERENCIAS_PLANO
import com.example.fitunity.data.DbContract.TABLE_USUARIOS
import com.example.fitunity.data.PerfilCliente
import java.security.MessageDigest
import java.util.Calendar

class UsuarioDao(private val db: SQLiteDatabase) {

    fun cadastrarUsuario(
        nome: String,
        email: String,
        senha: String,
        dataNascimento: String = "",
        genero: String = ""
    ): Long {
        return try {
            db.beginTransaction()
            try {
                val id = db.insertOrThrow(
                    TABLE_USUARIOS,
                    null,
                    ContentValues().apply {
                        put(COLUMN_EMAIL, normalizarEmail(email))
                        put(COLUMN_SENHA, hashSenha(senha))
                        put(COLUMN_NOME, nome.trim())
                        put(COLUMN_DATA_NASCIMENTO, dataNascimento)
                        put(COLUMN_GENERO, genero)
                        put(COLUMN_TEMPO_CADASTRADO, anoAtual())
                    }
                )
                DatabaseSeeder.garantirDadosUsuario(db, id)
                db.setTransactionSuccessful()
                id
            } finally {
                db.endTransaction()
            }
        } catch (e: SQLiteConstraintException) {
            -1L
        }
    }

    fun autenticarUsuario(email: String, senha: String): PerfilCliente? {
        val cursor = db.query(
            TABLE_USUARIOS,
            arrayOf(COLUMN_ID),
            "$COLUMN_EMAIL = ? AND $COLUMN_SENHA = ?",
            arrayOf(normalizarEmail(email), hashSenha(senha)),
            null, null, null
        )
        val id = cursor.use {
            if (it.moveToFirst()) it.getLong(it.getColumnIndexOrThrow(COLUMN_ID)) else null
        }
        return id?.let { buscarPerfilPorId(it) }
    }

    fun buscarPerfilPorId(id: Long): PerfilCliente? {
        DatabaseSeeder.garantirLinhasPerfil(db, id)
        val cursor = db.rawQuery(
            """
            SELECT
                u.$COLUMN_ID, u.$COLUMN_NOME, u.$COLUMN_EMAIL, u.$COLUMN_DATA_NASCIMENTO,
                u.$COLUMN_GENERO, u.$COLUMN_FOTO_URI, u.$COLUMN_TEMPO_CADASTRADO,
                p.$COLUMN_PESO_ATUAL, p.$COLUMN_OBJETIVO_PESO, p.$COLUMN_ALTURA,
                p.$COLUMN_FOCO, p.$COLUMN_NIVEL, p.$COLUMN_TIPO_PLANO,
                e.$COLUMN_TREINOS_REALIZADOS, e.$COLUMN_TREINOS_PENDENTES,
                e.$COLUMN_PROGRESSO, e.$COLUMN_AGUA_ML,
                d.$COLUMN_META_CALORIAS, d.$COLUMN_REFEICOES_POR_DIA, d.$COLUMN_OBJETIVO_DIETA,
                d.$COLUMN_RESTRICAO_ALIMENTAR, d.$COLUMN_OBSERVACOES_DIETA,
                pl.$COLUMN_OBJETIVO_PLANO, pl.$COLUMN_FREQUENCIA_TREINO
            FROM $TABLE_USUARIOS u
            INNER JOIN $TABLE_PERFIS_FISICOS p ON p.$COLUMN_USUARIO_ID = u.$COLUMN_ID
            INNER JOIN $TABLE_ESTATISTICAS e ON e.$COLUMN_USUARIO_ID = u.$COLUMN_ID
            INNER JOIN $TABLE_PREFERENCIAS_DIETA d ON d.$COLUMN_USUARIO_ID = u.$COLUMN_ID
            INNER JOIN $TABLE_PREFERENCIAS_PLANO pl ON pl.$COLUMN_USUARIO_ID = u.$COLUMN_ID
            WHERE u.$COLUMN_ID = ?
            """.trimIndent(),
            arrayOf(id.toString())
        )
        return cursor.use { if (it.moveToFirst()) it.paraPerfilCliente() else null }
    }

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
        db.beginTransaction()
        return try {
            val linhasUsuario = db.update(
                TABLE_USUARIOS,
                ContentValues().apply {
                    put(COLUMN_NOME, nome.trim())
                    put(COLUMN_DATA_NASCIMENTO, dataNascimento)
                    put(COLUMN_GENERO, genero)
                    email?.let { put(COLUMN_EMAIL, normalizarEmail(it)) }
                },
                "$COLUMN_ID = ?",
                arrayOf(id.toString())
            )
            if (linhasUsuario > 0) {
                DatabaseSeeder.garantirLinhasPerfil(db, id)
                db.update(
                    TABLE_PERFIS_FISICOS,
                    ContentValues().apply {
                        put(COLUMN_PESO_ATUAL, pesoAtual)
                        put(COLUMN_OBJETIVO_PESO, objetivoPeso)
                        put(COLUMN_FOCO, foco)
                        put(COLUMN_ALTURA, altura)
                    },
                    "$COLUMN_USUARIO_ID = ?",
                    arrayOf(id.toString())
                )
            }
            db.setTransactionSuccessful()
            linhasUsuario > 0
        } finally {
            db.endTransaction()
        }
    }

    fun atualizarFotoPerfil(id: Long, caminhoArquivo: String): Boolean =
        db.update(
            TABLE_USUARIOS,
            ContentValues().apply { put(COLUMN_FOTO_URI, caminhoArquivo) },
            "$COLUMN_ID = ?",
            arrayOf(id.toString())
        ) > 0

    fun atualizarTipoPlano(id: Long, tipoPlano: String): Boolean {
        DatabaseSeeder.garantirLinhasPerfil(db, id)
        return db.update(
            TABLE_PERFIS_FISICOS,
            ContentValues().apply { put(COLUMN_TIPO_PLANO, tipoPlano) },
            "$COLUMN_USUARIO_ID = ?",
            arrayOf(id.toString())
        ) > 0
    }

    fun atualizarPreferenciasPlano(
        id: Long,
        objetivo: String,
        frequencia: String,
        restricao: String
    ): Boolean {
        DatabaseSeeder.garantirLinhasPerfil(db, id)
        db.update(
            TABLE_PREFERENCIAS_PLANO,
            ContentValues().apply {
                put(COLUMN_OBJETIVO_PLANO, objetivo)
                put(COLUMN_FREQUENCIA_TREINO, frequencia)
            },
            "$COLUMN_USUARIO_ID = ?",
            arrayOf(id.toString())
        )
        return db.update(
            TABLE_PREFERENCIAS_DIETA,
            ContentValues().apply { put(COLUMN_RESTRICAO_ALIMENTAR, restricao) },
            "$COLUMN_USUARIO_ID = ?",
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
        DatabaseSeeder.garantirLinhasPerfil(db, usuarioId)
        return db.update(
            TABLE_PREFERENCIAS_DIETA,
            ContentValues().apply {
                put(COLUMN_META_CALORIAS, metaCalorias)
                put(COLUMN_REFEICOES_POR_DIA, refeicoesPorDia)
                put(COLUMN_OBJETIVO_DIETA, objetivo)
                put(COLUMN_RESTRICAO_ALIMENTAR, restricao)
                put(COLUMN_OBSERVACOES_DIETA, observacoes)
            },
            "$COLUMN_USUARIO_ID = ?",
            arrayOf(usuarioId.toString())
        ) > 0
    }

    fun adicionarAgua(usuarioId: Long, ml: Int, limiteMl: Int? = null): Int {
        DatabaseSeeder.garantirLinhasPerfil(db, usuarioId)
        if (limiteMl != null) {
            db.execSQL(
                "UPDATE $TABLE_ESTATISTICAS SET $COLUMN_AGUA_ML = MIN($COLUMN_AGUA_ML + ?, ?) WHERE $COLUMN_USUARIO_ID = ?",
                arrayOf<Any>(ml, limiteMl, usuarioId)
            )
        } else {
            db.execSQL(
                "UPDATE $TABLE_ESTATISTICAS SET $COLUMN_AGUA_ML = $COLUMN_AGUA_ML + ? WHERE $COLUMN_USUARIO_ID = ?",
                arrayOf<Any>(ml, usuarioId)
            )
        }
        return db.rawQuery(
            "SELECT $COLUMN_AGUA_ML FROM $TABLE_ESTATISTICAS WHERE $COLUMN_USUARIO_ID = ?",
            arrayOf(usuarioId.toString())
        ).use { if (it.moveToFirst()) it.getInt(0) else 0 }
    }

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

    private fun hashSenha(senha: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(senha.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    private fun anoAtual(): String = Calendar.getInstance().get(Calendar.YEAR).toString()
}
