package com.example.fitunity.data

import androidx.annotation.DrawableRes

data class PerfilCliente(
    val id: Long,
    val nome: String,
    val email: String,
    val dataNascimento: String,
    val genero: String,
    val pesoAtual: Double,
    val objetivoPeso: Double,
    val treinosRealizados: Int,
    val treinosPendentes: Int,
    val nivel: String,
    val tipoPlano: String,
    val tempoCadastrado: String,
    val progresso: Double,
    val foco: String = "Hipertrofia",
    val fotoUri: String? = null,
    val altura: Double = 1.75,
    val aguaMl: Int = 0,
    val metaCalorias: Int = 2200,
    val refeicoesPorDia: Int = 4,
    val objetivoDieta: String = "Manter",
    val restricaoAlimentar: String = "Nenhuma",
    val observacoesDieta: String = "",
    val objetivoPlano: String = "Manter a saúde",
    val frequenciaTreino: String = "4 vezes por semana"
)

data class Exercicio(
    val id: Int,
    val nome: String,
    val grupo: String,
    val series: String,
    val repeticoes: String,
    val intervalo: String,
    val carga: String,
    @DrawableRes val imagem: Int,
    val instrucoes: List<String>
)

data class TreinoDia(
    val id: Int,
    val diaSemana: String,
    val titulo: String,
    val descricao: String,
    val duracao: String,
    val quantidadeExercicios: Int,
    val quantidadeSeries: Int,
    @DrawableRes val imagem: Int,
    val exercicios: List<Exercicio>
)

data class RefeicaoDia(
    val id: Long,
    val periodo: String,
    val titulo: String,
    val calorias: String,
    val horario: String,
    @DrawableRes val imagem: Int,
    val ingredientes: List<Ingrediente> = emptyList(),
    val instrucoes: List<String> = emptyList(),
    val concluida: Boolean = false
)

data class Ingrediente(val nome: String, val quantidade: String)

enum class TipoNotificacao { TREINO, AGUA, CONQUISTA, MENSAGEM, RELATORIO }

data class Notificacao(
    val titulo: String,
    val mensagem: String,
    val horario: String,
    val tipo: TipoNotificacao,
    val id: Long = 0,
    val lida: Boolean = false
)

data class HistoricoTreino(
    val data: String,
    val titulo: String,
    val duracao: String,
    val calorias: String,
    val id: Long = 0
)

data class MensagemChat(
    val id: Long,
    val texto: String,
    val enviadaPorMim: Boolean,
    val horario: String
)
