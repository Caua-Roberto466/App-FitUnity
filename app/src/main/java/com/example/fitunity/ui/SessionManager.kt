package com.example.fitunity.data

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

/**
 * Guarda em memória o usuário atualmente logado, para que qualquer tela
 * (ex.: Home) consiga ler seus dados reativamente, sem precisar repassar
 * o perfil manualmente por toda a árvore de navegação.
 *
 * OBS: isso é suficiente para o estágio atual do app (sem login persistente
 * entre reaberturas). Se no futuro for necessário manter o usuário logado
 * mesmo depois de fechar o app, isso precisará ser trocado por algo como
 * DataStore/SharedPreferences guardando o id do usuário.
 */
object SessionManager {
    private val _usuarioAtual = mutableStateOf<PerfilCliente?>(null)
    val usuarioAtual: State<PerfilCliente?> = _usuarioAtual

    fun login(perfil: PerfilCliente) {
        _usuarioAtual.value = perfil
    }

    fun logout() {
        _usuarioAtual.value = null
    }
}
