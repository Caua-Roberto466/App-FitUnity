package com.example.fitunity.data

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File

/**
 * Salva o currículo escolhido pelo usuário no armazenamento interno do app,
 * mantendo uma cópia local do documento enquanto a candidatura existir.
 */
object CurriculoArquivo {

    fun nomeExibicao(context: Context, uri: Uri): String? = runCatching {
        context.contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)
            ?.use { cursor ->
                val indice = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (indice >= 0 && cursor.moveToFirst()) cursor.getString(indice) else null
            }
    }.getOrNull()

    fun salvar(
        context: Context,
        uri: Uri,
        usuarioId: Long,
        nomeOriginal: String,
        caminhoAnterior: String? = null
    ): String? = runCatching {
        val destino = File(
            context.filesDir,
            "curriculo_${usuarioId}_${System.currentTimeMillis()}_${nomeSeguro(nomeOriginal)}"
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

    fun excluir(caminho: String?) {
        if (!caminho.isNullOrBlank()) {
            runCatching { File(caminho).delete() }
        }
    }

    private fun nomeSeguro(nome: String): String =
        nome.trim()
            .ifBlank { "curriculo.pdf" }
            .replace(Regex("[^A-Za-z0-9._-]"), "_")
            .take(80)
}
