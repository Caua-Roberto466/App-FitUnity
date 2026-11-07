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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.example.fitunity.ui.components.AnimatedCheck
import com.example.fitunity.ui.components.ChevronRight
import com.example.fitunity.ui.components.FitUnityBottomBar
import com.example.fitunity.ui.components.MenuAction
import com.example.fitunity.ui.components.MoreMenu
import com.example.fitunity.ui.components.PageHeader
import com.example.fitunity.ui.components.PrimaryActionButton
import com.example.fitunity.ui.components.RevealOnAppear
import com.example.fitunity.ui.components.SegmentedControl
import com.example.fitunity.ui.components.revealShadow
import com.example.fitunity.ui.navigation.Rotas
import com.example.fitunity.ui.theme.BrandBlue
import com.example.fitunity.ui.theme.BrandDark
import com.example.fitunity.ui.theme.Ink
import com.example.fitunity.ui.theme.Muted
import com.example.fitunity.ui.theme.SoftBlueBackground
import com.example.fitunity.ui.theme.SoftBlueBorder
import com.example.fitunity.ui.theme.SurfaceWhite

@Composable
fun TreinosScreen(navController: NavController) {
    val context = LocalContext.current
    val db = remember { FitUnityDbHelper(context) }
    val usuarioId = SessionManager.usuarioAtual.value?.id ?: 0L
    val treinos = remember(usuarioId) { db.listarTreinos(usuarioId) }
    val minutosTotais = treinos.sumOf { treino ->
        treino.duracao.filter { it.isDigit() }.toIntOrNull() ?: 0
    }

    var selecionado by remember { mutableIntStateOf(0) }
    var aba by remember { mutableIntStateOf(0) }
    val treinoSelecionado = treinos.getOrNull(selecionado)

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

            RevealOnAppear(
                modifier = Modifier.fillMaxWidth(),
                delayMillis = 0,
                offsetY = 14.dp
            ) {
            PageHeader(
                titulo = "Plano de treino",
                subtitulo = "Semana 12 de 16"
            ) {
                MoreMenu(
                    itens = listOf(
                        MenuAction("Editar plano", "Ajustar treinos da semana") {
                            treinoSelecionado?.let { navController.navigate(Rotas.editarTreino(it.id)) }
                        },
                        MenuAction("Criar novo treino", "Montar uma nova rotina") {
                            navController.navigate(Rotas.CRIAR_TREINO)
                        },
                        MenuAction("Ver histórico") {
                            navController.navigate(Rotas.HISTORICO_TREINOS)
                        }
                    )
                )
            }
            }

            Spacer(modifier = Modifier.height(20.dp))

            RevealOnAppear(
                modifier = Modifier.fillMaxWidth(),
                delayMillis = 80,
                offsetY = 20.dp,
                scaleFrom = 0.97f
            ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(185.dp)
                    .revealShadow(12.dp, RoundedCornerShape(22.dp), clip = false)
                    .clip(RoundedCornerShape(22.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.treino_full_body),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0x0D082132), Color(0xD9082132))
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 20.dp, bottom = 18.dp)
                ) {
                    Text(
                        text = "FOCO DA SEMANA",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        letterSpacing = 1.2.sp
                    )
                    Text(
                        text = "Força e definição",
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "${treinos.size} treinos · ${minutosTotais / 60}h ${minutosTotais % 60}min",
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
            }

            Spacer(modifier = Modifier.height(13.dp))

            RevealOnAppear(
                modifier = Modifier.fillMaxWidth(),
                delayMillis = 160,
                offsetY = 16.dp
            ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ShortcutButton(
                    texto = "Biblioteca",
                    modifier = Modifier.weight(1f)
                ) { navController.navigate(Rotas.BIBLIOTECA_EXERCICIOS) }
                ShortcutButton(
                    texto = "Histórico",
                    modifier = Modifier.weight(1f)
                ) { navController.navigate(Rotas.HISTORICO_TREINOS) }
            }
            }

            Spacer(modifier = Modifier.height(16.dp))

            RevealOnAppear(
                modifier = Modifier.fillMaxWidth(),
                delayMillis = 210,
                offsetY = 16.dp
            ) {
            SegmentedControl(
                opcoes = listOf("Semanal", "Calendário"),
                selecionado = aba,
                onSelecionar = { indice ->
                    aba = indice
                    if (indice == 1) navController.navigate(Rotas.CALENDARIO_TREINO)
                }
            )
            }

            Spacer(modifier = Modifier.height(14.dp))

            treinos.forEachIndexed { index, treino ->
                val ativo = index == selecionado
                RevealOnAppear(
                    modifier = Modifier.fillMaxWidth(),
                    delayMillis = (250 + index * 60).coerceAtMost(500),
                    offsetY = 14.dp
                ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                        .revealShadow(5.dp, RoundedCornerShape(16.dp), clip = false)
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (ativo) Color(0xFFF1FAFF) else SurfaceWhite)
                        .clickable { selecionado = index }
                        .padding(11.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.width(48.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "${18 + index}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = BrandDark
                        )
                        Text(
                            text = treino.diaSemana.take(3).uppercase(),
                            fontSize = 11.sp,
                            color = Muted
                        )
                    }
                    Spacer(modifier = Modifier.width(13.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = treino.titulo, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Ink)
                        Text(text = treino.descricao, fontSize = 12.sp, color = Muted)
                    }
                    Box(
                        modifier = Modifier.size(18.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (!ativo) ChevronRight()
                        AnimatedCheck(
                            visivel = ativo,
                            tint = BrandBlue,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
                }
            }

            RevealOnAppear(
                modifier = Modifier.fillMaxWidth(),
                delayMillis = 320,
                offsetY = 16.dp
            ) {
            PrimaryActionButton(
                texto = "Ver treino selecionado",
                onClick = { treinoSelecionado?.let { navController.navigate(Rotas.treinoDetalhe(it.id)) } }
            )
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
private fun ShortcutButton(
    texto: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(11.dp))
            .background(SoftBlueBackground)
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = texto, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BrandDark)
    }
}
