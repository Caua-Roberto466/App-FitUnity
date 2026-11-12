package com.example.fitunity.ui.screens.nutrition

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.WaterDrop
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
import com.example.fitunity.ui.components.CalorieRing
import com.example.fitunity.ui.components.ChevronRight
import com.example.fitunity.ui.components.FitUnityBottomBar
import com.example.fitunity.ui.components.MacroBar
import com.example.fitunity.ui.components.MenuAction
import com.example.fitunity.ui.components.MoreMenu
import com.example.fitunity.ui.components.PageHeader
import com.example.fitunity.ui.components.PrimaryActionButton
import com.example.fitunity.ui.components.RevealOnAppear
import com.example.fitunity.ui.components.SectionHeading
import com.example.fitunity.ui.components.revealShadow
import com.example.fitunity.ui.components.rememberAnimatedInt
import com.example.fitunity.ui.components.rememberAnimatedProgress
import com.example.fitunity.ui.navigation.Rotas
import com.example.fitunity.ui.theme.BrandBlue
import com.example.fitunity.ui.theme.BrandDark
import com.example.fitunity.ui.theme.Coral
import com.example.fitunity.ui.theme.Ink
import com.example.fitunity.ui.theme.Mint
import com.example.fitunity.ui.theme.Muted
import com.example.fitunity.ui.theme.SoftBlueBackground
import com.example.fitunity.ui.theme.SurfaceWhite
import com.example.fitunity.ui.theme.Track

@Composable
fun DietaScreen(navController: NavController) {
    val context = LocalContext.current
    val db = remember { FitUnityDbHelper(context) }
    val perfil = SessionManager.usuarioAtual.value
    val usuarioId = perfil?.id ?: 0L
    val refeicoes = remember(usuarioId) { db.listarRefeicoes(usuarioId) }
    val meta = perfil?.metaCalorias?.takeIf { it > 0 } ?: 2200
    val consumido = refeicoes.filter { it.concluida }.sumOf { refeicao ->
        refeicao.calorias.filter { it.isDigit() }.toIntOrNull() ?: 0
    }
    val percentual = (consumido * 100 / meta).coerceIn(0, 100)
    var agua by remember(usuarioId) { mutableIntStateOf(perfil?.aguaMl ?: 0) }
    val metaAgua = 2500
    val aguaLimitada = agua.coerceIn(0, metaAgua)
    val progressoAgua = rememberAnimatedProgress(aguaLimitada.toFloat() / metaAgua)
    val percentualAgua = rememberAnimatedInt((aguaLimitada * 100 / metaAgua).coerceIn(0, 100))

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
                titulo = "Alimentação",
                subtitulo = "Plano de hoje"
            ) {
                MoreMenu(
                    itens = listOf(
                        MenuAction("Editar dieta", "Ajustar metas e refeições") {
                            navController.navigate(Rotas.EDITAR_DIETA)
                        },
                        MenuAction("Plano semanal", "Ver os 7 dias") {
                            navController.navigate(Rotas.PLANO_SEMANAL)
                        },
                        MenuAction("Calendário", "Acompanhar registros") {
                            navController.navigate(Rotas.CALENDARIO_DIETA)
                        }
                    )
                )
            }
            }

            Spacer(modifier = Modifier.height(20.dp))

            RevealOnAppear(
                modifier = Modifier.fillMaxWidth(),
                delayMillis = 80,
                offsetY = 20.dp
            ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .revealShadow(8.dp, RoundedCornerShape(22.dp), clip = false)
                    .clip(RoundedCornerShape(22.dp))
                    .background(SurfaceWhite)
                    .padding(18.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CalorieRing(
                        atual = "$consumido",
                        meta = "de $meta kcal",
                        percentual = percentual
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(11.dp)
                    ) {
                        MacroBar(
                            rotulo = "Proteínas",
                            valor = "110 / 140 g",
                            percentual = 0.78f,
                            cor = BrandBlue
                        )
                        MacroBar(
                            rotulo = "Carboidratos",
                            valor = "180 / 220 g",
                            percentual = 0.81f,
                            cor = Coral
                        )
                        MacroBar(
                            rotulo = "Gorduras",
                            valor = "45 / 70 g",
                            percentual = 0.64f,
                            cor = Mint
                        )
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = "Restam ${(meta - consumido).coerceAtLeast(0)} kcal para hoje",
                    fontSize = 12.sp,
                    color = Muted,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            }

            RevealOnAppear(
                modifier = Modifier.fillMaxWidth(),
                delayMillis = 160,
                offsetY = 18.dp
            ) {
            Column {
            SectionHeading(
                titulo = "Hidratação",
                acao = "${aguaLimitada / 1000},${(aguaLimitada % 1000) / 100} L de ${metaAgua / 1000},${(metaAgua % 1000) / 100} L"
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .revealShadow(6.dp, RoundedCornerShape(18.dp), clip = false)
                    .clip(RoundedCornerShape(18.dp))
                    .background(SurfaceWhite)
                    .padding(15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(SoftBlueBackground),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.WaterDrop,
                        contentDescription = null,
                        tint = BrandBlue,
                        modifier = Modifier.size(21.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(7.dp)
                            .clip(CircleShape)
                            .background(Track)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(progressoAgua)
                                .height(7.dp)
                                .clip(CircleShape)
                                .background(BrandBlue)
                        )
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = "$percentualAgua% da meta diária",
                        fontSize = 11.sp,
                        color = Muted
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                val metaAtingida = aguaLimitada >= metaAgua
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(if (metaAtingida) Mint else BrandBlue)
                        .clickable(enabled = !metaAtingida) {
                            agua = db.adicionarAgua(usuarioId, 250, limiteMl = metaAgua)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (metaAtingida) Icons.Filled.Check else Icons.Filled.Add,
                        contentDescription = if (metaAtingida) "Meta de hidratação atingida" else "Adicionar água",
                        tint = Color.White,
                        modifier = Modifier.size(19.dp)
                    )
                }
            }
            }
            }

            RevealOnAppear(
                modifier = Modifier.fillMaxWidth(),
                delayMillis = 220,
                offsetY = 16.dp
            ) {
            SectionHeading(
                titulo = "Refeições de hoje",
                acao = "${refeicoes.size} refeições"
            )
            }

            refeicoes.forEachIndexed { index, refeicao ->
                RevealOnAppear(
                    modifier = Modifier.fillMaxWidth(),
                    delayMillis = (260 + index * 60).coerceAtMost(600),
                    offsetY = 14.dp
                ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(SurfaceWhite)
                        .clickable { navController.navigate(Rotas.refeicaoDetalhe(refeicao.id)) }
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = refeicao.imagem),
                        contentDescription = null,
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(13.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = refeicao.periodo,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = BrandBlue
                        )
                        Text(
                            text = refeicao.titulo,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Ink
                        )
                        Text(
                            text = "${refeicao.calorias} · ${refeicao.horario}",
                            fontSize = 12.sp,
                            color = Muted
                        )
                    }
                    ChevronRight()
                }
                }
            }

            RevealOnAppear(
                modifier = Modifier.fillMaxWidth(),
                delayMillis = 320,
                offsetY = 16.dp
            ) {
            PrimaryActionButton(
                texto = "Ver plano semanal",
                onClick = { navController.navigate(Rotas.PLANO_SEMANAL) }
            )
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}
