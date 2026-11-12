package com.example.fitunity.data

/**
 * Níveis dos planos oferecidos pela FitUnity, do mais básico ao mais completo.
 * A ordem é usada para comparar se o plano atual libera um recurso.
 */
enum class PlanoNivel(val ordem: Int, val nomeExibicao: String) {
    GRATUITO(0, "Gratuito"),
    ADAPTADO(1, "Adaptado"),
    PROFISSIONAL(2, "Profissional")
}

/**
 * Funções do app que exigem um plano mínimo para serem usadas.
 * Cada recurso aponta para o nível de plano que o desbloqueia.
 */
enum class RecursoPlano(
    val titulo: String,
    val descricao: String,
    val nivelMinimo: PlanoNivel
) {
    PERSONAL_TRAINER(
        titulo = "Personal trainer",
        descricao = "Tenha um treinador acompanhando seus treinos, ajustando sua rotina e respondendo suas dúvidas.",
        nivelMinimo = PlanoNivel.ADAPTADO
    ),
    MENSAGENS_PERSONAL(
        titulo = "Chat com o personal",
        descricao = "Converse diretamente com seu personal trainer para tirar dúvidas e acompanhar sua evolução.",
        nivelMinimo = PlanoNivel.ADAPTADO
    ),
    RELATORIO_MENSAL(
        titulo = "Relatório mensal",
        descricao = "Receba um relatório completo da sua evolução, revisado por um profissional.",
        nivelMinimo = PlanoNivel.PROFISSIONAL
    )
}

/**
 * Converte o texto salvo no banco (incluindo nomes antigos) em um nível de plano.
 * Valores desconhecidos caem no nível mais baixo (fail-closed), para nunca
 * liberar recursos pagos por engano — por exemplo após um downgrade.
 */
fun nivelDoPlano(tipoPlano: String?): PlanoNivel = when (tipoPlano?.trim()?.lowercase()) {
    null, "", "gratuito", "grátis", "gratis", "básico", "basico", "free" -> PlanoNivel.GRATUITO
    "adaptado", "intermediário", "intermediario" -> PlanoNivel.ADAPTADO
    "profissional", "premium" -> PlanoNivel.PROFISSIONAL
    else -> PlanoNivel.GRATUITO
}

/** Nível do plano do usuário logado no momento. */
fun planoAtual(): PlanoNivel = nivelDoPlano(SessionManager.usuarioAtual.value?.tipoPlano)

/** Retorna true apenas se o plano atual do usuário libera o recurso informado. */
fun possuiAcesso(recurso: RecursoPlano): Boolean =
    planoAtual().ordem >= recurso.nivelMinimo.ordem
