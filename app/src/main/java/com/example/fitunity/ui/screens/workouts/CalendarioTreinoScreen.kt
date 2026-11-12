package com.example.fitunity.ui.screens.workouts

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fitunity.data.FitUnityDbHelper
import com.example.fitunity.data.SessionManager
import com.example.fitunity.data.diasSemana
import com.example.fitunity.ui.components.ChevronRight
import com.example.fitunity.ui.components.DetailHeader
import com.example.fitunity.ui.components.EmptyNote
import com.example.fitunity.ui.components.PrimaryActionButton
import com.example.fitunity.ui.components.RevealOnAppear
import com.example.fitunity.ui.components.SectionHeading
import com.example.fitunity.ui.components.revealShadow
import com.example.fitunity.ui.navigation.Rotas
import com.example.fitunity.ui.theme.BrandBlue
import com.example.fitunity.ui.theme.BrandDark
import com.example.fitunity.ui.theme.Ink
import com.example.fitunity.ui.theme.Muted
import com.example.fitunity.ui.theme.SoftBlue
import com.example.fitunity.ui.theme.SoftBlueBackground
import com.example.fitunity.ui.theme.SoftBlueBorder
import com.example.fitunity.ui.theme.SurfaceWhite

@Composable
fun CalendarioTreinoScreen(navController: NavController) {
    val context = LocalContext.current
    val db = remember { FitUnityDbHelper(context) }
    val usuarioId = SessionManager.usuarioAtual.value?.id ?: 0L
    val treinos = remember(usuarioId) { db.listarTreinos(usuarioId) }
    var diaSelecionado by remember { mutableIntStateOf(18) }
    val diasDoMes = (1..30).toList()
    val diasComTreino = remember { setOf(1, 3, 5, 8, 10, 12, 15, 17, 18, 20, 22, 24, 27, 29) }
    val hoje = 18

    Scaffold(containerColor = Color(0xFFF8FBFD)) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 22.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            DetailHeader(
                titulo = "Calendário de treinos",
                onBack = { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.height(18.dp))

            RevealOnAppear(
                modifier = Modifier.fillMaxWidth(),
                delayMillis = 0,
                offsetY = 14.dp
            ) {
                MesHeroCard(totalTreinos = diasComTreino.size)
            }

            RevealOnAppear(
                modifier = Modifier.fillMaxWidth(),
                delayMillis = 70,
                offsetY = 14.dp
            ) {
                Column {
                    Spacer(modifier = Modifier.height(18.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        diasSemana.forEach { dia ->
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = dia.take(1),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Muted
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                        diasDoMes.chunked(7).forEach { semana ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                semana.forEach { dia ->
                                    DiaCelula(
                                        dia = dia,
                                        selecionado = dia == diaSelecionado,
                                        temTreino = dia in diasComTreino,
                                        hoje = dia == hoje,
                                        onClick = { diaSelecionado = dia },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                repeat(7 - semana.size) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 14.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        LegendaItem(cor = BrandBlue, texto = "Selecionado")
                        LegendaItem(cor = SoftBlue, texto = "Dia com treino")
                        LegendaItem(cor = SoftBlueBorder, texto = "Hoje")
                    }
                }
            }

            RevealOnAppear(
                modifier = Modifier.fillMaxWidth(),
                delayMillis = 140,
                offsetY = 14.dp
            ) {
                Column {
                    SectionHeading(
                        titulo = "Treino do dia $diaSelecionado",
                        subtitulo = if (diaSelecionado in diasComTreino) "Você tem treino marcado" else "Nenhum treino agendado"
                    )

                    if (treinos.isEmpty()) {
                        EmptyNote(
                            texto = "Nenhum treino cadastrado ainda.",
                            modifier = Modifier.padding(top = 6.dp)
                        )
                    } else {
                        treinos.forEach { treino ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 10.dp)
                                    .revealShadow(6.dp, RoundedCornerShape(18.dp), clip = false)
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(SurfaceWhite)
                                    .clickable { navController.navigate(Rotas.treinoDetalhe(treino.id)) }
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(RoundedCornerShape(13.dp))
                                        .background(SoftBlueBackground),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.FitnessCenter,
                                        contentDescription = null,
                                        tint = BrandBlue,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = treino.diaSemana.uppercase(),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 0.6.sp,
                                        color = BrandBlue
                                    )
                                    Text(
                                        text = treino.titulo,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Ink,
                                        modifier = Modifier.padding(top = 1.dp)
                                    )
                                    Text(
                                        text = treino.descricao,
                                        fontSize = 11.sp,
                                        color = Muted,
                                        modifier = Modifier.padding(top = 1.dp)
                                    )
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = treino.duracao,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = BrandDark
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    ChevronRight()
                                }
                            }
                        }
                    }
                }
            }

            RevealOnAppear(
                modifier = Modifier.fillMaxWidth(),
                delayMillis = 210,
                offsetY = 14.dp
            ) {
                PrimaryActionButton(
                    texto = "Iniciar treino do dia",
                    onClick = { treinos.firstOrNull()?.let { navController.navigate(Rotas.treinoAtivo(it.id)) } }
                )
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
private fun MesHeroCard(totalTreinos: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .revealShadow(10.dp, RoundedCornerShape(22.dp), clip = false)
            .clip(RoundedCornerShape(22.dp))
            .background(Brush.linearGradient(listOf(Color(0xFF3AA6EA), Color(0xFF0E7BB8))))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color.White.copy(alpha = 0.18f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.CalendarMonth,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Abril de 2026",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "Toque em um dia para ver os detalhes",
                fontSize = 11.sp,
                color = Color.White.copy(alpha = 0.85f),
                modifier = Modifier.padding(top = 2.dp)
            )
        }
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White.copy(alpha = 0.18f))
                .padding(horizontal = 11.dp, vertical = 6.dp)
        ) {
            Text(
                text = "$totalTreinos treinos",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
private fun DiaCelula(
    dia: Int,
    selecionado: Boolean,
    temTreino: Boolean,
    hoje: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val forma = RoundedCornerShape(12.dp)
    val fundo = when {
        selecionado -> BrandBlue
        temTreino -> SoftBlue
        else -> SurfaceWhite
    }
    val corTexto = when {
        selecionado -> Color.White
        temTreino -> BrandDark
        else -> Muted
    }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(forma)
            .background(fundo)
            .border(
                width = if (hoje && !selecionado) 1.5.dp else 1.dp,
                color = when {
                    selecionado -> Color.Transparent
                    hoje -> BrandBlue
                    else -> SoftBlueBorder.copy(alpha = 0.6f)
                },
                shape = forma
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$dia",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = corTexto
        )
        if (temTreino) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 5.dp)
                    .size(4.dp)
                    .clip(CircleShape)
                    .background(if (selecionado) Color.White else BrandBlue)
            )
        }
    }
}

@Composable
private fun LegendaItem(cor: Color, texto: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(cor)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = texto, fontSize = 11.sp, color = Muted)
    }
}
