package com.example.fitunity.data

import androidx.annotation.DrawableRes
import com.example.fitunity.R

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

val exerciciosSuperior = listOf(
    Exercicio(
        id = 1,
        nome = "Supino reto",
        grupo = "Peito",
        series = "3",
        repeticoes = "10–12",
        intervalo = "60s",
        carga = "40 kg",
        imagem = R.drawable.supino_inclinado,
        instrucoes = listOf(
            "Deite no banco com os pés firmes no chão e segure a barra um pouco além da largura dos ombros.",
            "Desça a barra de forma controlada até a região central do peito.",
            "Empurre a barra para cima, mantendo os ombros encaixados no banco."
        )
    ),
    Exercicio(
        id = 2,
        nome = "Remada baixa",
        grupo = "Costas",
        series = "3",
        repeticoes = "10–12",
        intervalo = "60s",
        carga = "35 kg",
        imagem = R.drawable.barra_fixa,
        instrucoes = listOf(
            "Sente-se com a coluna ereta e segure o puxador com as duas mãos.",
            "Puxe o puxador em direção ao abdômen, contraindo as costas.",
            "Retorne de forma controlada, sem deixar os ombros subirem."
        )
    ),
    Exercicio(
        id = 3,
        nome = "Desenvolvimento",
        grupo = "Ombros",
        series = "3",
        repeticoes = "8–10",
        intervalo = "60s",
        carga = "20 kg",
        imagem = R.drawable.treino_flexoes,
        instrucoes = listOf(
            "Sente-se com as costas apoiadas e segure os halteres na altura dos ombros.",
            "Empurre os halteres para cima até quase estender os braços.",
            "Desça lentamente até a posição inicial."
        )
    ),
    Exercicio(
        id = 4,
        nome = "Rosca direta",
        grupo = "Bíceps",
        series = "3",
        repeticoes = "12",
        intervalo = "45s",
        carga = "12 kg",
        imagem = R.drawable.treino_personal,
        instrucoes = listOf(
            "Em pé, segure a barra com as palmas voltadas para cima.",
            "Flexione os cotovelos levando a barra até a altura do peito.",
            "Desça controlando o movimento, sem balançar o corpo."
        )
    )
)

val treinosSemana = listOf(
    TreinoDia(
        id = 1,
        diaSemana = "Segunda",
        titulo = "Superior",
        descricao = "Peito, ombros e tríceps",
        duracao = "45 min",
        quantidadeExercicios = 4,
        quantidadeSeries = 12,
        imagem = R.drawable.treino_hoje,
        exercicios = exerciciosSuperior
    ),
    TreinoDia(
        id = 2,
        diaSemana = "Terça",
        titulo = "Inferior",
        descricao = "Pernas e glúteos",
        duracao = "50 min",
        quantidadeExercicios = 4,
        quantidadeSeries = 12,
        imagem = R.drawable.treino_pernas_gluteos,
        exercicios = exerciciosSuperior
    ),
    TreinoDia(
        id = 3,
        diaSemana = "Quarta",
        titulo = "Superior",
        descricao = "Costas e bíceps",
        duracao = "45 min",
        quantidadeExercicios = 4,
        quantidadeSeries = 12,
        imagem = R.drawable.treino_full_body,
        exercicios = exerciciosSuperior
    ),
    TreinoDia(
        id = 4,
        diaSemana = "Quinta",
        titulo = "Cardio",
        descricao = "Resistência e mobilidade",
        duracao = "30 min",
        quantidadeExercicios = 3,
        quantidadeSeries = 9,
        imagem = R.drawable.corrida,
        exercicios = exerciciosSuperior.take(3)
    )
)

val ingredientesRefeicao = listOf(
    Ingrediente("Peito de frango", "150g"),
    Ingrediente("Arroz integral cozido", "120g"),
    Ingrediente("Brócolis no vapor", "100g"),
    Ingrediente("Azeite de oliva", "1 colher")
)

val instrucoesRefeicao = listOf(
    "Tempere o frango com ervas, sal e pimenta. Grelhe até dourar dos dois lados.",
    "Cozinhe o brócolis no vapor por cerca de 5 minutos para manter a textura.",
    "Monte o prato com o arroz e finalize os vegetais com azeite."
)

val refeicoesHoje = listOf(
    RefeicaoDia(
        id = 1,
        periodo = "Café da manhã",
        titulo = "Omelete com aveia",
        calorias = "420 kcal",
        horario = "07:30",
        imagem = R.drawable.dieta_frutas_legumes,
        ingredientes = ingredientesRefeicao,
        instrucoes = instrucoesRefeicao
    ),
    RefeicaoDia(
        id = 2,
        periodo = "Almoço",
        titulo = "Frango, arroz e brócolis",
        calorias = "560 kcal",
        horario = "12:30",
        imagem = R.drawable.dieta_populares,
        ingredientes = ingredientesRefeicao,
        instrucoes = instrucoesRefeicao
    ),
    RefeicaoDia(
        id = 3,
        periodo = "Lanche",
        titulo = "Iogurte, frutas e granola",
        calorias = "210 kcal",
        horario = "16:00",
        imagem = R.drawable.dieta_zero_gluten,
        ingredientes = ingredientesRefeicao,
        instrucoes = instrucoesRefeicao
    ),
    RefeicaoDia(
        id = 4,
        periodo = "Jantar",
        titulo = "Salmão com legumes",
        calorias = "380 kcal",
        horario = "20:00",
        imagem = R.drawable.dieta_carnes,
        ingredientes = ingredientesRefeicao,
        instrucoes = instrucoesRefeicao
    )
)

val notificacoesMock = listOf(
    Notificacao(
        "Treino de hoje",
        "Seu treino de membros superiores começa em 30 minutos.",
        "Agora",
        TipoNotificacao.TREINO
    ),
    Notificacao(
        "Lembrete de hidratação",
        "Você está quase atingindo sua meta diária de água.",
        "10:30",
        TipoNotificacao.AGUA
    ),
    Notificacao(
        "Parabéns, Lucas!",
        "Você completou 4 treinos nesta semana. Continue assim!",
        "Ontem",
        TipoNotificacao.CONQUISTA
    ),
    Notificacao(
        "Mensagem do Rafael",
        "Ótimo desempenho no último treino. Ajustei sua próxima série.",
        "Ontem",
        TipoNotificacao.MENSAGEM
    ),
    Notificacao(
        "Relatório semanal",
        "Seu novo relatório de evolução já está disponível.",
        "Seg",
        TipoNotificacao.RELATORIO
    )
)

val historicoTreinosMock = listOf(
    HistoricoTreino("Hoje", "Superior", "45 min", "320 kcal"),
    HistoricoTreino("21 abr", "Inferior", "52 min", "410 kcal"),
    HistoricoTreino("18 abr", "Cardio e mobilidade", "35 min", "280 kcal"),
    HistoricoTreino("16 abr", "Superior", "48 min", "335 kcal"),
    HistoricoTreino("14 abr", "Inferior", "50 min", "398 kcal")
)

val bibliotecaExercicios = listOf(
    exerciciosSuperior[0],
    exerciciosSuperior[1],
    exerciciosSuperior[2],
    exerciciosSuperior[3],
    exerciciosSuperior[0].copy(
        id = 5,
        nome = "Agachamento livre",
        grupo = "Pernas",
        imagem = R.drawable.treino_pernas_gluteos
    ),
    exerciciosSuperior[1].copy(
        id = 6,
        nome = "Prancha",
        grupo = "Core",
        imagem = R.drawable.treino_full_body
    )
)

val diasSemana = listOf("Seg", "Ter", "Qua", "Qui", "Sex", "Sáb", "Dom")
