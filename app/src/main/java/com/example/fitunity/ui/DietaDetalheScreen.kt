package com.example.fitunity.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ---------- Modelo de dados ----------

data class Alimento(
    val nome: String,
    val quantidade: String
)

data class Refeicao(
    val nomeRefeicao: String, // "Café da manhã", "Almoço", "Lanche", "Jantar"
    val horario: String,
    val alimentos: List<Alimento>
)

// Mapa de exemplo: id da dieta -> lista de refeições
// Troque pelos dados reais (banco de dados, API, etc)
val refeicoesPorDieta: Map<String, List<Refeicao>> = mapOf(
    "1" to listOf(
        Refeicao(
            nomeRefeicao = "Café da manhã",
            horario = "07:00",
            alimentos = listOf(
                Alimento("Mamão", "1 fatia média"),
                Alimento("Aveia em flocos", "2 colheres de sopa"),
                Alimento("Chá verde", "1 xícara")
            )
        ),
        Refeicao(
            nomeRefeicao = "Almoço",
            horario = "12:30",
            alimentos = listOf(
                Alimento("Arroz integral", "3 colheres de sopa"),
                Alimento("Feijão", "1 concha"),
                Alimento("Legumes cozidos", "à vontade"),
                Alimento("Salada verde", "à vontade")
            )
        ),
        Refeicao(
            nomeRefeicao = "Lanche da tarde",
            horario = "16:00",
            alimentos = listOf(
                Alimento("Maçã", "1 unidade"),
                Alimento("Castanhas", "1 punhado pequeno")
            )
        ),
        Refeicao(
            nomeRefeicao = "Jantar",
            horario = "19:30",
            alimentos = listOf(
                Alimento("Sopa de legumes", "1 prato fundo"),
                Alimento("Torrada integral", "2 fatias")
            )
        )
    ),

    "3" to listOf(
        Refeicao(
            nomeRefeicao = "Café da manhã",
            horario = "07:30",
            alimentos = listOf(
                Alimento("Iogurte natural", "1 pote"),
                Alimento("Granola", "2 colheres de sopa"),
                Alimento("Morango", "5 unidades")
            )
        ),
        Refeicao(
            nomeRefeicao = "Almoço",
            horario = "12:30",
            alimentos = listOf(
                Alimento("Arroz integral", "3 colheres de sopa"),
                Alimento("Feijão", "1 concha"),
                Alimento("Peito de frango grelhado", "120g"),
                Alimento("Salada de folhas variadas", "à vontade")
            )
        ),
        Refeicao(
            nomeRefeicao = "Lanche da tarde",
            horario = "16:00",
            alimentos = listOf(
                Alimento("Banana", "1 unidade"),
                Alimento("Pasta de amendoim", "1 colher de sopa")
            )
        ),
        Refeicao(
            nomeRefeicao = "Jantar",
            horario = "19:30",
            alimentos = listOf(
                Alimento("Omelete de legumes", "2 ovos"),
                Alimento("Salada verde", "à vontade")
            )
        )
    ),

    "2" to listOf(
        Refeicao(
            nomeRefeicao = "Café da manhã",
            horario = "07:00",
            alimentos = listOf(
                Alimento("Ovos mexidos", "2 unidades"),
                Alimento("Pão integral", "1 fatia"),
                Alimento("Café sem açúcar", "1 xícara")
            )
        ),
        Refeicao(
            nomeRefeicao = "Almoço",
            horario = "12:30",
            alimentos = listOf(
                Alimento("Picanha grelhada", "150g"),
                Alimento("Arroz branco", "3 colheres de sopa"),
                Alimento("Feijão", "1 concha"),
                Alimento("Salada de folhas", "à vontade")
            )
        ),
        Refeicao(
            nomeRefeicao = "Lanche da tarde",
            horario = "16:00",
            alimentos = listOf(
                Alimento("Queijo minas", "2 fatias"),
                Alimento("Tomate cereja", "5 unidades")
            )
        ),
        Refeicao(
            nomeRefeicao = "Jantar",
            horario = "19:30",
            alimentos = listOf(
                Alimento("Filé de frango grelhado", "150g"),
                Alimento("Legumes assados", "à vontade")
            )
        )
    ),

    "4" to listOf(
        Refeicao(
            nomeRefeicao = "Café da manhã",
            horario = "07:00",
            alimentos = listOf(
                Alimento("Ovos mexidos", "3 unidades"),
                Alimento("Aveia em flocos", "3 colheres de sopa"),
                Alimento("Banana", "1 unidade"),
                Alimento("Whey protein", "1 dose")
            )
        ),
        Refeicao(
            nomeRefeicao = "Almoço",
            horario = "12:30",
            alimentos = listOf(
                Alimento("Peito de frango grelhado", "200g"),
                Alimento("Arroz integral", "4 colheres de sopa"),
                Alimento("Batata doce", "1 unidade média"),
                Alimento("Brócolis cozido", "à vontade")
            )
        ),
        Refeicao(
            nomeRefeicao = "Lanche da tarde",
            horario = "16:00",
            alimentos = listOf(
                Alimento("Iogurte grego", "1 pote"),
                Alimento("Castanhas", "1 punhado"),
                Alimento("Mel", "1 colher de chá")
            )
        ),
        Refeicao(
            nomeRefeicao = "Jantar",
            horario = "19:30",
            alimentos = listOf(
                Alimento("Carne moída magra", "150g"),
                Alimento("Purê de batata doce", "1 porção"),
                Alimento("Salada verde", "à vontade")
            )
        ),
        Refeicao(
            nomeRefeicao = "Ceia",
            horario = "22:00",
            alimentos = listOf(
                Alimento("Caseína ou queijo cottage", "1 porção")
            )
        )
    ),

    "5" to listOf(
        Refeicao(
            nomeRefeicao = "Café da manhã",
            horario = "07:00",
            alimentos = listOf(
                Alimento("Leite de amêndoas", "1 copo"),
                Alimento("Aveia em flocos", "3 colheres de sopa"),
                Alimento("Frutas vermelhas", "1 punhado"),
                Alimento("Sementes de chia", "1 colher de sopa")
            )
        ),
        Refeicao(
            nomeRefeicao = "Almoço",
            horario = "12:30",
            alimentos = listOf(
                Alimento("Grão de bico refogado", "1 concha"),
                Alimento("Quinoa", "3 colheres de sopa"),
                Alimento("Legumes salteados", "à vontade"),
                Alimento("Salada verde com azeite", "à vontade")
            )
        ),
        Refeicao(
            nomeRefeicao = "Lanche da tarde",
            horario = "16:00",
            alimentos = listOf(
                Alimento("Hummus", "2 colheres de sopa"),
                Alimento("Palitos de cenoura e pepino", "à vontade")
            )
        ),
        Refeicao(
            nomeRefeicao = "Jantar",
            horario = "19:30",
            alimentos = listOf(
                Alimento("Tofu grelhado", "150g"),
                Alimento("Arroz integral", "3 colheres de sopa"),
                Alimento("Legumes no vapor", "à vontade")
            )
        )
    ),

    "6" to listOf(
        Refeicao(
            nomeRefeicao = "Café da manhã",
            horario = "07:00",
            alimentos = listOf(
                Alimento("Pão sem glúten", "2 fatias"),
                Alimento("Queijo branco", "2 fatias"),
                Alimento("Suco de laranja natural", "1 copo")
            )
        ),
        Refeicao(
            nomeRefeicao = "Almoço",
            horario = "12:30",
            alimentos = listOf(
                Alimento("Filé de peixe grelhado", "150g"),
                Alimento("Arroz branco", "3 colheres de sopa"),
                Alimento("Legumes cozidos", "à vontade"),
                Alimento("Salada verde", "à vontade")
            )
        ),
        Refeicao(
            nomeRefeicao = "Lanche da tarde",
            horario = "16:00",
            alimentos = listOf(
                Alimento("Iogurte natural sem glúten", "1 pote"),
                Alimento("Frutas picadas", "1 porção")
            )
        ),
        Refeicao(
            nomeRefeicao = "Jantar",
            horario = "19:30",
            alimentos = listOf(
                Alimento("Omelete de legumes", "2 ovos"),
                Alimento("Salada de folhas", "à vontade")
            )
        )
    )
)

// ---------- Tela principal ----------

// onVoltarClick -> deve voltar para a tela de listagem de dietas
@Composable
fun DietaDetalheScreen(
    dieta: Dieta,
    refeicoes: List<Refeicao> = refeicoesPorDieta[dieta.id] ?: emptyList(),
    onVoltarClick: () -> Unit = {},
    onIniciarDietaClick: () -> Unit = {}
) {
    Scaffold(
        topBar = { DietaTopBar() },
        containerColor = Color.White
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Imagem de topo com botão voltar
            item {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(id = dieta.imagemRes),
                        contentDescription = dieta.titulo,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                    )

                    IconButton(
                        onClick = onVoltarClick,
                        modifier = Modifier
                            .padding(top = 40.dp, start = 12.dp)
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color.Black
                        )
                    }
                }
            }

            // Cabeçalho com título, descrição e estatística
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = dieta.categoria,
                        color = FitUnityBlue,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = dieta.titulo,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = dieta.descricao,
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "${formatarNumero(dieta.pessoasFizeram)} pessoas já fizeram essa dieta",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Refeições do dia",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            // Lista de refeições
            if (refeicoes.isEmpty()) {
                item {
                    Text(
                        text = "Detalhes das refeições ainda não cadastrados para esta dieta.",
                        color = Color.Gray,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                }
            } else {
                items(refeicoes) { refeicao ->
                    RefeicaoCard(refeicao = refeicao)
                    Spacer(modifier = Modifier.height(14.dp))
                }
            }

            // Botão iniciar dieta
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = onIniciarDietaClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = FitUnityBlue)
                    ) {
                        Text(text = "Iniciar essa dieta", fontSize = 18.sp, color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

// ---------- Componentes ----------

@Composable
private fun RefeicaoCard(refeicao: Refeicao) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .background(Color(0xFFF7F9FC), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = refeicao.nomeRefeicao,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = refeicao.horario,
                fontSize = 13.sp,
                color = FitUnityBlue,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        refeicao.alimentos.forEach { alimento ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = FitUnityBlue,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = alimento.nome,
                    fontSize = 14.sp,
                    color = Color.Black,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = alimento.quantidade,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}
