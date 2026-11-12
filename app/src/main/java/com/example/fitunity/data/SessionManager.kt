package com.example.fitunity.data

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

/**
 * Guarda em memória o usuário atualmente logado, para que qualquer tela
 * (ex.: Home) consiga ler seus dados reativamente, sem precisar repassar
 * o perfil manualmente por toda a árvore de navegação.
 *
 * Também guarda o id do usuário em SharedPreferences ("permanecer conectado"):
 * ao abrir o app de novo, a Splash consulta [usuarioIdSalvo] e, se houver um
 * id salvo, faz login automático sem passar por Onboarding/Login.
 */
object SessionManager {
    private const val PREFS_NAME = "fitunity_session"
    private const val KEY_USUARIO_ID = "usuario_id"

    private val _usuarioAtual = mutableStateOf<PerfilCliente?>(null)
    val usuarioAtual: State<PerfilCliente?> = _usuarioAtual

    /**
     * Loga o usuário em memória. Se um [context] for passado, também salva o id
     * dele em disco, para que ele continue conectado da próxima vez que abrir o app.
     */
    fun login(perfil: PerfilCliente, context: Context? = null) {
        _usuarioAtual.value = perfil
        context?.let { salvarUsuarioId(it, perfil.id) }
    }

    /**
     * Desloga o usuário em memória. Se um [context] for passado, também apaga
     * o id salvo em disco, para não logar automaticamente na próxima abertura.
     */
    fun logout(context: Context? = null) {
        _usuarioAtual.value = null
        context?.let { limparUsuarioId(it) }
    }

    /** Id salvo em disco de uma sessão anterior, ou null se ninguém pediu para continuar conectado. */
    fun usuarioIdSalvo(context: Context): Long? {
        val id = prefs(context).getLong(KEY_USUARIO_ID, -1L)
        return if (id != -1L) id else null
    }

    private fun salvarUsuarioId(context: Context, id: Long) {
        prefs(context).edit().putLong(KEY_USUARIO_ID, id).apply()
    }

    private fun limparUsuarioId(context: Context) {
        prefs(context).edit().remove(KEY_USUARIO_ID).apply()
    }

    private fun prefs(context: Context) =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
}
