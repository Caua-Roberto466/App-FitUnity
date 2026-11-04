package com.example.fitunity.ui.screens.workouts

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fitunity.data.FitUnityDbHelper
import com.example.fitunity.data.SessionManager
import com.example.fitunity.ui.components.ChevronRight
import com.example.fitunity.ui.components.DetailHeader
import com.example.fitunity.ui.components.InfoPill
import com.example.fitunity.ui.components.MenuAction
import com.example.fitunity.ui.components.MoreMenu
import com.example.fitunity.ui.components.PrimaryActionButton
import com.example.fitunity.ui.components.SectionHeading
import com.example.fitunity.ui.navigation.Rotas
import com.example.fitunity.ui.theme.BrandBlue
import com.example.fitunity.ui.theme.BrandDark
import com.example.fitunity.ui.theme.Ink
import com.example.fitunity.ui.theme.Muted
import com.example.fitunity.ui.theme.SoftBlueBackground
import com.example.fitunity.ui.theme.SurfaceWhite

@Composable
fun TreinoDetalheScreen(navController: NavController, treinoId: Int) {
    val context = LocalContext.current
    val db = remember { FitUnityDbHelper(context) }
    val usuarioId = SessionManager.usuarioAtual.value?.id ?: 0L
    val treino = remember(treinoId, usuarioId) {
        val lista = db.listarTreinos(usuarioId)
        lista.firstOrNull { it.id == treinoId } ?: lista.firstOrNull()
    } ?: return
    val totalSeries = treino.quantidadeSeries

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
                titulo = "Detalhes do treino",
                onBack = { navController.popBackStack() }
            ) {
                MoreMenu(
                    itens = listOf(
                        MenuAction("Editar treino") {
                            navController.navigate(Rotas.editarTreino(treino.id))
                        },
                        MenuAction("Ver exercícios") {
                            navController.navigate(Rotas.BIBLIOTECA_EXERCICIOS)
                        }
                    )
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "${treino.diaSemana}-feira",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = BrandBlue
            )
            Text(
                text = treino.titulo,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Ink,
                modifier = Modifier.padding(top = 3.dp)
            )
            Text(
                text = treino.descricao,
                fontSize = 13.sp,
                color = Muted,
                modifier = Modifier.padding(top = 3.dp)
            )

            Row(
                modifier = Modifier.padding(top = 13.dp),
                horizontalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                InfoPill(texto = treino.duracao)
                InfoPill(texto = "${treino.quantidadeExercicios} exercícios")
                InfoPill(texto = "$totalSeries séries")
            }

            Spacer(modifier = Modifier.height(18.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
                    .clip(RoundedCornerShape(20.dp))
            ) {
                Image(
                    painter = painterResource(id = treino.imagem),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0x00082132), Color(0x66082132))
                            )
                        )
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.92f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = "Reproduzir demonstração",
                        tint = BrandDark,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            SectionHeading(
                titulo = "Exercícios do treino",
                acao = "${treino.exercicios.size} exercícios"
            )

            treino.exercicios.forEach { exercicio ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 9.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(SurfaceWhite)
                        .clickable {
                            navController.navigate(Rotas.exercicioDetalhe(exercicio.id))
                        }
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = exercicio.imagem),
                        contentDescription = null,
                        modifier = Modifier
                            .size(54.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = exercicio.nome,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Ink
                        )
                        Text(
                            text = "${exercicio.series} × ${exercicio.repeticoes} · ${exercicio.carga}",
                            fontSize = 12.sp,
                            color = Muted
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(9.dp))
                            .background(SoftBlueBackground)
                            .padding(horizontal = 8.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = exercicio.grupo,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = BrandDark
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    ChevronRight()
                }
            }

            PrimaryActionButton(
                texto = "Iniciar treino",
                onClick = { navController.navigate(Rotas.treinoAtivo(treino.id)) }
            )

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}
