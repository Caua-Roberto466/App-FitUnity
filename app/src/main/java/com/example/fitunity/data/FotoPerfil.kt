package com.example.fitunity.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File

/**
 * Utilitário para salvar a foto de perfil escolhida pelo usuário no armazenamento
 * interno do app e carregá-la novamente nas telas.
 */
object FotoPerfil {

    fun salvar(
        context: Context,
        uri: Uri,
        usuarioId: Long,
        caminhoAnterior: String? = null
    ): String? = runCatching {
        val destino = File(
            context.filesDir,
            "perfil_${usuarioId}_${System.currentTimeMillis()}.jpg"
        )
        val entrada = context.contentResolver.openInputStream(uri) ?: return@runCatching null
        entrada.use { origem ->
            destino.outputStream().use { saida -> origem.copyTo(saida) }
        }
        if (!caminhoAnterior.isNullOrBlank() && caminhoAnterior != destino.absolutePath) {
            runCatching { File(caminhoAnterior).delete() }
        }
        destino.absolutePath
    }.getOrNull()

    fun carregar(caminho: String?): Bitmap? =
        caminho?.let { runCatching { BitmapFactory.decodeFile(it) }.getOrNull() }
}
