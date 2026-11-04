package com.example.fitunity.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fitunity.R
import com.example.fitunity.data.FitUnityDbHelper
import com.example.fitunity.data.SessionManager
import com.example.fitunity.data.diasSemana
import com.example.fitunity.ui.components.FitUnityBottomBar
import com.example.fitunity.ui.components.SectionHeading
import com.example.fitunity.ui.navigation.Rotas
import com.example.fitunity.ui.theme.BrandBlue
import com.example.fitunity.ui.theme.BrandDark
import com.example.fitunity.ui.theme.Coral
import com.example.fitunity.ui.theme.CoralBackground
import com.example.fitunity.ui.theme.Ink
import com.example.fitunity.ui.theme.Mint
import com.example.fitunity.ui.theme.MintBackground
import com.example.fitunity.ui.theme.Muted
import com.example.fitunity.ui.theme.SoftBlue
import com.example.fitunity.ui.theme.SoftBlueBackground
import com.example.fitunity.ui.theme.SoftBlueBorder
import com.example.fitunity.ui.theme.SurfaceWhite
import com.example.fitunity.ui.theme.Track

@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    val db = remember { FitUnityDbHelper(context) }
    val perfil = SessionManager.usuarioAtual.value
    val usuarioId = perfil?.id ?: 0L
    val treinos = remember(usuarioId) { db.listarTreinos(usuarioId) }
    val refeicoes = remember(usuarioId) { db.listarRefeicoes(usuarioId) }
    val historico = remember(usuarioId) { db.listarHistorico(usuarioId) }
    val notificacoes = remember(usuarioId) { db.listarNotificacoes(usuarioId) }
    val hojeIndex = (java.time.LocalDate.now().dayOfWeek.value - 1).coerceIn(0, 6)
    val treinoHoje = treinos.firstOrNull {
        it.diaSemana.equals(diasSemana[hojeIndex], ignoreCase = true)
    } ?: treinos.firstOrNull()
    val caloriasConsumidas = refeicoes.filter { it.concluida }.sumOf { refeicao ->
        refeicao.calorias.filter { it.isDigit() }.toIntOrNull() ?: 0
    }
    val ultimoTreino = historico.firstOrNull()
    val temNaoLidas = notificacoes.any { !it.lida }

    val nome = perfil?.nome?.takeIf { it.isNotBlank() } ?: "Usuário"
    val primeiroNome = nome.substringBefore(" ")
    val iniciais = nome.split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercase() }

    val treinosConcluidos = perfil?.treinosRealizados ?: 4
    val metaSemanal = (perfil?.let { it.treinosRealizados + it.treinosPendentes } ?: 5)
        .coerceAtLeast(5)
    val percentual = ((treinosConcluidos.toFloat() / metaSemanal) * 100).toInt().coerceIn(0, 100)

    Scaffold(
        bottomBar = { FitUnityBottomBar(navController) },
        containerColor = Color(0xFFF8FBFD)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 22.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(45.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(listOf(Color(0xFF142F4A), BrandBlue))),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = iniciais.ifBlank { "FU" },
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Olá, $primeiroNome!",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Ink
                    )
                    Text(text = "Vamos evoluir hoje?", fontSize = 13.sp, color = Muted)
                }
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .shadow(4.dp, RoundedCornerShape(14.dp), clip = false)
                        .clip(RoundedCornerShape(14.dp))
                        .background(SurfaceWhite)
                        .clickable { navController.navigate(Rotas.NOTIFICACOES) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Notifications,
                        contentDescription = "Notificações",
                        tint = Ink,
                        modifier = Modifier.size(20.dp)
                    )
                    if (temNaoLidas) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(top = 7.dp, end = 8.dp)
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFF5862))
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(216.dp)
                    .shadow(12.dp, RoundedCornerShape(24.dp), clip = false)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFF047FC7))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.treino_hoje),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.62f)
                        .align(Alignment.CenterEnd),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    Color(0xFF047FC7),
                                    Color(0xFF047FC7).copy(alpha = 0.7f),
                                    Color.Transparent
                                )
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .padding(start = 24.dp, top = 26.dp, end = 12.dp)
                ) {
                    Text(
                        text = "TREINO DE HOJE",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.8f),
                        letterSpacing = 1.2.sp
                    )
                    Spacer(modifier = Modifier.height(7.dp))
                    Text(
                        text = treinoHoje?.titulo ?: "Membros superiores",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        lineHeight = 28.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Timer,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(text = treinoHoje?.duracao ?: "45 min", fontSize = 13.sp, color = Color.White)
                        Spacer(modifier = Modifier.width(7.dp))
                        Box(
                            modifier = Modifier
                                .size(3.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                        )
                        Spacer(modifier = Modifier.width(7.dp))
                        Text(text = treinoHoje?.descricao ?: "Academia", fontSize = 13.sp, color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(18.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(SurfaceWhite)
                            .clickable {
                                treinoHoje?.let { navController.navigate(Rotas.treinoDetalhe(it.id)) }
                                    ?: navController.navigate(Rotas.TREINOS)
                            }
                            .padding(horizontal = 22.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = "Ver treino",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = BrandDark
                        )
                    }
                }
            }

            SectionHeading(
                titulo = "Seu progresso",
                subtitulo = "Meta semanal",
                acao = "$treinosConcluidos de $metaSemanal dias"
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(20.dp), clip = false)
                    .clip(RoundedCornerShape(20.dp))
                    .background(SurfaceWhite)
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.width(88.dp)) {
                    Text(
                        text = "$percentual%",
                        fontSize = 27.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrandBlue
                    )
                    Text(
                        text = if (percentual >= 70) "Excelente ritmo!" else "Bom começo!",
                        fontSize = 12.sp,
                        color = Muted
                    )
                }
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    listOf("S", "T", "Q", "Q", "S", "S", "D").forEachIndexed { index, dia ->
                        val feito = index < treinosConcluidos.coerceAtMost(4)
                        val hoje = index == treinosConcluidos.coerceAtMost(4)
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .width(10.dp)
                                    .height(if (hoje) 31.dp else 43.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            feito -> BrandBlue
                                            hoje -> Color(0xFFBCE8FF)
                                            else -> Track
                                        }
                                    )
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(text = dia, fontSize = 11.sp, color = Muted)
                        }
                    }
                }
            }

            SectionHeading(
                titulo = "Resumo de hoje",
                acao = "Ver detalhes",
                onAcao = { navController.navigate(Rotas.PROGRESSO) }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(9.dp)
            ) {
                MetricCard(
                    modifier = Modifier.weight(1f),
                    icone = Icons.Filled.DirectionsWalk,
                    cor = BrandBlue,
                    fundo = SoftBlue,
                    valor = "6.240",
                    rotulo = "Passos"
                )
                MetricCard(
                    modifier = Modifier.weight(1f),
                    icone = Icons.Filled.LocalFireDepartment,
                    cor = Coral,
                    fundo = CoralBackground,
                    valor = "$caloriasConsumidas",
                    rotulo = "Calorias"
                )
                MetricCard(
                    modifier = Modifier.weight(1f),
                    icone = Icons.Filled.Timer,
                    cor = Mint,
                    fundo = MintBackground,
                    valor = ultimoTreino?.duracao ?: "0 min",
                    rotulo = "Atividade"
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(SoftBlueBackground)
                    .padding(15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(31.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(SoftBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Bolt,
                        contentDescription = null,
                        tint = BrandBlue,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Dica do personal",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Ink
                    )
                    Text(
                        text = "Não esqueça de se hidratar durante o treino.",
                        fontSize = 12.sp,
                        color = Muted
                    )
                }
                Icon(
                    imageVector = Icons.Filled.ChevronRight,
                    contentDescription = null,
                    tint = BrandBlue,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
private fun MetricCard(
    modifier: Modifier,
    icone: ImageVector,
    cor: Color,
    fundo: Color,
    valor: String,
    rotulo: String
) {
    Row(
        modifier = modifier
            .shadow(5.dp, RoundedCornerShape(15.dp), clip = false)
            .clip(RoundedCornerShape(15.dp))
            .background(SurfaceWhite)
            .padding(horizontal = 8.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(31.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(fundo),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icone,
                contentDescription = null,
                tint = cor,
                modifier = Modifier.size(16.dp)
            )
        }
        Spacer(modifier = Modifier.width(7.dp))
        Column {
            Text(text = valor, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Ink)
            Text(text = rotulo, fontSize = 11.sp, color = Muted)
        }
    }
}
