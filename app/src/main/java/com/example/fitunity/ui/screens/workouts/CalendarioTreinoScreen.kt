package com.example.fitunity.ui.screens.workouts

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.fitunity.ui.components.PrimaryActionButton
import com.example.fitunity.ui.components.SectionHeading
import com.example.fitunity.ui.navigation.Rotas
import com.example.fitunity.ui.theme.BrandBlue
import com.example.fitunity.ui.theme.BrandDark
import com.example.fitunity.ui.theme.Ink
import com.example.fitunity.ui.theme.Muted
import com.example.fitunity.ui.theme.SurfaceWhite

@Composable
fun CalendarioTreinoScreen(navController: NavController) {
    val context = LocalContext.current
    val db = remember { FitUnityDbHelper(context) }
    val usuarioId = SessionManager.usuarioAtual.value?.id ?: 0L
    val treinos = remember(usuarioId) { db.listarTreinos(usuarioId) }
    var diaSelecionado by remember { mutableIntStateOf(18) }
    val diasDoMes = (1..30).toList()

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

            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "Abril de 2026", fontSize = 23.sp, fontWeight = FontWeight.Bold, color = Ink)
            Text(
                text = "Toque em um dia para ver os detalhes.",
                fontSize = 13.sp,
                color = Muted,
                modifier = Modifier.padding(top = 4.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 18.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                diasSemana.forEach { dia ->
                    Text(
                        text = dia.take(1),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Muted,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
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
                            val selecionado = dia == diaSelecionado
                            val temTreino = dia in listOf(1, 3, 5, 8, 10, 12, 15, 17, 18, 20, 22, 24, 27, 29)
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(11.dp))
                                    .background(
                                        when {
                                            selecionado -> BrandBlue
                                            temTreino -> Color(0xFFF1FAFF)
                                            else -> SurfaceWhite
                                        }
                                    )
                                    .clickable { diaSelecionado = dia },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "$dia",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = when {
                                        selecionado -> Color.White
                                        temTreino -> BrandDark
                                        else -> Muted
                                    }
                                )
                            }
                        }
                        repeat(7 - semana.size) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            SectionHeading(titulo = "Treino do dia $diaSelecionado")

            treinos.forEach { treino ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 9.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(SurfaceWhite)
                        .clickable { navController.navigate(Rotas.treinoDetalhe(treino.id)) }
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = treino.diaSemana,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = BrandBlue
                        )
                        Text(text = treino.titulo, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Ink)
                        Text(text = treino.descricao, fontSize = 11.sp, color = Muted)
                    }
                    Text(text = treino.duracao, fontSize = 12.sp, color = BrandDark)
                    Spacer(modifier = Modifier.size(4.dp))
                    ChevronRight()
                }
            }

            PrimaryActionButton(
                texto = "Iniciar treino do dia",
                onClick = { treinos.firstOrNull()?.let { navController.navigate(Rotas.treinoAtivo(it.id)) } }
            )

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}
