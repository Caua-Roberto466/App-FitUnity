package com.example.fitunity.data

import com.example.fitunity.R

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

val ingredientesLeve = listOf(
    Ingrediente("Filé de salmão", "140g"),
    Ingrediente("Quinoa cozida", "100g"),
    Ingrediente("Legumes assados", "120g"),
    Ingrediente("Limão e ervas", "a gosto")
)

val instrucoesLeve = listOf(
    "Tempere o salmão com limão, sal e ervas e asse por 15 minutos.",
    "Cozinhe a quinoa e reserve.",
    "Sirva o salmão com os legumes assados e a quinoa."
)

val ingredientesCafe = listOf(
    Ingrediente("Ovos", "2 unidades"),
    Ingrediente("Aveia em flocos", "30g"),
    Ingrediente("Banana", "1 unidade"),
    Ingrediente("Mel", "1 colher de chá")
)

val instrucoesCafe = listOf(
    "Bata os ovos com a aveia até formar uma massa homogênea.",
    "Prepare a omelete em fogo médio, dobrando ao meio.",
    "Sirva com a banana fatiada e finalize com o mel."
)

val ingredientesVegano = listOf(
    Ingrediente("Grão-de-bico cozido", "130g"),
    Ingrediente("Mix de folhas", "80g"),
    Ingrediente("Tomate e pepino", "100g"),
    Ingrediente("Azeite e limão", "a gosto")
)

val instrucoesVegano = listOf(
    "Monte a base com o mix de folhas, tomate e pepino.",
    "Adicione o grão-de-bico cozido por cima.",
    "Tempere com azeite, limão, sal e pimenta a gosto."
)

private val opcoesCafeManha = listOf(
    RefeicaoDia(0, "Café da manhã", "Omelete com aveia", "420 kcal", "07:30", R.drawable.dieta_frutas_legumes, ingredientesCafe, instrucoesCafe),
    RefeicaoDia(0, "Café da manhã", "Pão integral com ovo", "380 kcal", "07:00", R.drawable.dieta_populares, ingredientesCafe, instrucoesCafe),
    RefeicaoDia(0, "Café da manhã", "Iogurte com granola", "350 kcal", "07:30", R.drawable.dieta_zero_gluten, ingredientesLeve, instrucoesLeve),
    RefeicaoDia(0, "Café da manhã", "Tapioca com queijo", "400 kcal", "07:15", R.drawable.dieta_frutas_legumes, ingredientesCafe, instrucoesCafe),
    RefeicaoDia(0, "Café da manhã", "Panqueca de banana", "390 kcal", "07:20", R.drawable.dieta_vegana, ingredientesVegano, instrucoesVegano)
)

private val opcoesAlmoco = listOf(
    RefeicaoDia(0, "Almoço", "Frango, arroz e brócolis", "560 kcal", "12:30", R.drawable.dieta_populares, ingredientesRefeicao, instrucoesRefeicao),
    RefeicaoDia(0, "Almoço", "Carne magra com batata-doce", "590 kcal", "12:30", R.drawable.dieta_carnes, ingredientesRefeicao, instrucoesRefeicao),
    RefeicaoDia(0, "Almoço", "Salmão com quinoa e legumes", "540 kcal", "12:45", R.drawable.dieta_proteina, ingredientesLeve, instrucoesLeve),
    RefeicaoDia(0, "Almoço", "Feijão, arroz e frango grelhado", "580 kcal", "12:30", R.drawable.dieta_populares, ingredientesRefeicao, instrucoesRefeicao),
    RefeicaoDia(0, "Almoço", "Bowl vegano de grão-de-bico", "520 kcal", "12:30", R.drawable.dieta_vegana, ingredientesVegano, instrucoesVegano)
)

private val opcoesLanche = listOf(
    RefeicaoDia(0, "Lanche", "Iogurte, frutas e granola", "210 kcal", "16:00", R.drawable.dieta_zero_gluten, ingredientesLeve, instrucoesLeve),
    RefeicaoDia(0, "Lanche", "Mix de castanhas e banana", "230 kcal", "16:00", R.drawable.dieta_frutas_legumes, ingredientesCafe, instrucoesCafe),
    RefeicaoDia(0, "Lanche", "Sanduíche natural de atum", "250 kcal", "16:15", R.drawable.dieta_populares, ingredientesRefeicao, instrucoesRefeicao),
    RefeicaoDia(0, "Lanche", "Suco verde com torrada", "180 kcal", "15:45", R.drawable.dieta_vegana, ingredientesVegano, instrucoesVegano),
    RefeicaoDia(0, "Lanche", "Queijo cottage com frutas", "200 kcal", "16:00", R.drawable.dieta_proteina, ingredientesLeve, instrucoesLeve)
)

private val opcoesJantar = listOf(
    RefeicaoDia(0, "Jantar", "Salmão com legumes", "380 kcal", "20:00", R.drawable.dieta_carnes, ingredientesLeve, instrucoesLeve),
    RefeicaoDia(0, "Jantar", "Frango grelhado com salada", "360 kcal", "19:45", R.drawable.dieta_populares, ingredientesRefeicao, instrucoesRefeicao),
    RefeicaoDia(0, "Jantar", "Omelete de claras e legumes", "320 kcal", "19:30", R.drawable.dieta_frutas_legumes, ingredientesCafe, instrucoesCafe),
    RefeicaoDia(0, "Jantar", "Wrap integral de frango", "400 kcal", "20:00", R.drawable.dieta_populares, ingredientesRefeicao, instrucoesRefeicao),
    RefeicaoDia(0, "Jantar", "Sopa de lentilha com legumes", "340 kcal", "19:30", R.drawable.dieta_vegana, ingredientesVegano, instrucoesVegano)
)

/**
 * Monta o plano alimentar do dia girando as opções de cada refeição.
 * O [indice] garante que o cardápio mude a cada dia e se repita em ciclos.
 */
fun planoDoDia(indice: Int): List<RefeicaoDia> {
    fun <T> escolher(lista: List<T>, deslocamento: Int): T {
        val posicao = ((indice + deslocamento) % lista.size + lista.size) % lista.size
        return lista[posicao]
    }
    return listOf(
        escolher(opcoesCafeManha, 0),
        escolher(opcoesAlmoco, 1),
        escolher(opcoesLanche, 2),
        escolher(opcoesJantar, 3)
    )
}

/** Data atual no formato ISO (AAAA-MM-DD), usada para controlar a troca diária. */
fun dataDeHoje(): String = java.time.LocalDate.now().toString()

/** Índice do dia atual, usado para rotacionar o cardápio. */
fun indiceDoDia(): Int = java.time.LocalDate.now().toEpochDay().toInt()

/** Cardápio correspondente ao dia atual. */
fun planoDoDia(): List<RefeicaoDia> = planoDoDia(indiceDoDia())

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

val mensagensIniciais = listOf(
    MensagemChat(
        id = 1,
        texto = "Oi! Vi que você concluiu o treino de superior ontem. Como foi a recuperação?",
        enviadaPorMim = false,
        horario = "08:42"
    ),
    MensagemChat(
        id = 2,
        texto = "Foi ótima! Só senti um pouco o ombro no desenvolvimento.",
        enviadaPorMim = true,
        horario = "08:45"
    ),
    MensagemChat(
        id = 3,
        texto = "Isso é normal no começo. Vou ajustar a carga e incluir uma mobilidade antes do próximo treino.",
        enviadaPorMim = false,
        horario = "08:47"
    ),
    MensagemChat(
        id = 4,
        texto = "No próximo treino foque na execução controlada. Qualquer dúvida, me chama por aqui!",
        enviadaPorMim = false,
        horario = "08:48"
    )
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
