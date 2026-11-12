package com.example.fitunity.ui.screens.progress

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
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.WaterDrop
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fitunity.data.FitUnityDbHelper
import com.example.fitunity.data.RecursoPlano
import com.example.fitunity.data.SessionManager
import com.example.fitunity.data.possuiAcesso
import com.example.fitunity.ui.components.ChevronRight
import com.example.fitunity.ui.components.FitUnityBottomBar
import com.example.fitunity.ui.components.MenuAction
import com.example.fitunity.ui.components.MoreMenu
import com.example.fitunity.ui.components.PageHeader
import com.example.fitunity.ui.components.PrimaryActionButton
import com.example.fitunity.ui.components.ProgressRing
import com.example.fitunity.ui.components.RevealOnAppear
import com.example.fitunity.ui.components.SectionHeading
import com.example.fitunity.ui.components.revealShadow
import com.example.fitunity.ui.components.StatBlock
import com.example.fitunity.ui.components.rememberAnimatedDp
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
import com.example.fitunity.ui.theme.SurfaceWhite
import com.example.fitunity.ui.theme.Track

@Composable
fun ProgressoScreen(navController: NavController) {
    val context = LocalContext.current
    val db = remember { FitUnityDbHelper(context) }
    val dias = listOf("S", "T", "Q", "Q", "S", "S", "D")
    val perfil = SessionManager.usuarioAtual.value
    val usuarioId = perfil?.id ?: 0L
    val acessoRelatorio = possuiAcesso(RecursoPlano.RELATORIO_MENSAL)
    val treinos = perfil?.treinosRealizados ?: 0
    val totalTreinos = treinos + (perfil?.treinosPendentes ?: 0)
    val consistencia = if (totalTreinos > 0) (treinos * 100 / totalTreinos).coerceIn(0, 100) else 0
    val peso = perfil?.pesoAtual ?: 68.4
    val altura = perfil?.altura?.takeIf { it > 0 } ?: 1.75
    val imc = peso / (altura * altura)
    val imcFormatado = String.format("%.1f", imc).replace('.', ',')

    val refeicoes = remember(usuarioId) { db.listarRefeicoes(usuarioId) }
    val historico = remember(usuarioId) { db.listarHistorico(usuarioId) }
    val caloriasHoje = refeicoes.filter { it.concluida }.sumOf { refeicao ->
        refeicao.calorias.filter { it.isDigit() }.toIntOrNull() ?: 0
    }
    val aguaMl = perfil?.aguaMl ?: 0
    val ultimoTreino = historico.firstOrNull()

    val diasComDados = historico.take(7).reversed()
    val valoresSemana = List(7) { indice ->
        val posicao = indice - (7 - diasComDados.size)
        if (posicao in diasComDados.indices) {
            diasComDados[posicao].calorias.filter { it.isDigit() }.toIntOrNull()?.toFloat() ?: 0f
        } else {
            0f
        }
    }
    val maxSemana = valoresSemana.maxOrNull()?.coerceAtLeast(1f) ?: 1f
    val totalSemana = valoresSemana.sum().toInt()
    val treinosNaSemana = valoresSemana.count { it > 0f }
    val mediaSemana = if (treinosNaSemana > 0) totalSemana / treinosNaSemana else 0

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
                titulo = "Progresso",
                subtitulo = "Seus resultados até aqui"
            ) {
                MoreMenu(
                    itens = listOf(
                        MenuAction(
                            "Relatório mensal",
                            if (acessoRelatorio) "Ver evolução detalhada" else "Disponível no plano Profissional"
                        ) {
                            navController.navigate(Rotas.RELATORIO_MENSAL)
                        },
                        MenuAction("Histórico de treinos") {
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
                offsetY = 20.dp
            ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .background(
                        Brush.linearGradient(listOf(Color(0xFF3AA6EA), Color(0xFF117FC0)))
                    )
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(82.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    ProgressRing(valor = consistencia, rotulo = "", cor = Color.White, tamanho = 74.dp)
                }
                Spacer(modifier = Modifier.width(18.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Consistência",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                    Text(
                        text = "Você está no caminho certo!",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "$treinos treinos concluídos",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.padding(top = 3.dp)
                    )
                }
            }
            }

            RevealOnAppear(
                modifier = Modifier.fillMaxWidth(),
                delayMillis = 160,
                offsetY = 18.dp
            ) {
            Column {
            SectionHeading(titulo = "Evolução semanal", acao = "Últimos 7 dias")

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .revealShadow(8.dp, RoundedCornerShape(20.dp), clip = false)
                    .clip(RoundedCornerShape(20.dp))
                    .background(SurfaceWhite)
                    .padding(horizontal = 14.dp, vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(132.dp),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    valoresSemana.forEachIndexed { index, valor ->
                        val hoje = index == 6
                        val ratio = (valor / maxSemana).coerceIn(0f, 1f)
                        val alturaBarra = rememberAnimatedDp(
                            target = (12f + ratio * 66f).dp,
                            delayMillis = index * 70
                        )
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom
                        ) {
                            if (valor > 0f) {
                                Text(
                                    text = "${valor.toInt()}",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (hoje) BrandDark else Muted,
                                    maxLines = 1
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(alturaBarra)
                                    .clip(RoundedCornerShape(7.dp))
                                    .background(
                                        if (hoje) {
                                            Brush.verticalGradient(listOf(BrandBlue, Color(0xFF6FC6F2)))
                                        } else {
                                            Brush.verticalGradient(listOf(Color(0xFFBCE8FF), Color(0xFFD8F0FF)))
                                        }
                                    )
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = dias[index],
                                fontSize = 11.sp,
                                fontWeight = if (hoje) FontWeight.SemiBold else FontWeight.Normal,
                                color = if (hoje) BrandDark else Muted
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Track)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    Column {
                        Text(text = "Total da semana", fontSize = 11.sp, color = Muted)
                        Text(
                            text = "$totalSemana kcal",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Ink
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = "Média por treino", fontSize = 11.sp, color = Muted)
                        Text(
                            text = "$mediaSemana kcal",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = BrandDark
                        )
                    }
                }
            }
            }
            }

            RevealOnAppear(
                modifier = Modifier.fillMaxWidth(),
                delayMillis = 200,
                offsetY = 18.dp
            ) {
            Column {
            SectionHeading(titulo = "Resumo de hoje", subtitulo = "Atualizado com seus registros do dia")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(9.dp)
            ) {
                ResumoHojeCard(
                    modifier = Modifier.weight(1f),
                    icone = Icons.Filled.LocalFireDepartment,
                    cor = Coral,
                    fundo = CoralBackground,
                    valor = "$caloriasHoje",
                    rotulo = "kcal"
                )
                ResumoHojeCard(
                    modifier = Modifier.weight(1f),
                    icone = Icons.Filled.WaterDrop,
                    cor = BrandBlue,
                    fundo = SoftBlue,
                    valor = "${aguaMl / 1000},${(aguaMl % 1000) / 100}",
                    rotulo = "litros"
                )
                ResumoHojeCard(
                    modifier = Modifier.weight(1f),
                    icone = Icons.Filled.Timer,
                    cor = Mint,
                    fundo = MintBackground,
                    valor = ultimoTreino?.duracao ?: "0 min",
                    rotulo = "Atividade"
                )
            }
            }
            }

            RevealOnAppear(
                modifier = Modifier.fillMaxWidth(),
                delayMillis = 240,
                offsetY = 18.dp
            ) {
            Column {
            SectionHeading(titulo = "Indicadores")

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .revealShadow(6.dp, RoundedCornerShape(18.dp), clip = false)
                    .clip(RoundedCornerShape(18.dp))
                    .background(SurfaceWhite)
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatBlock(valor = String.format("%.1f", peso).replace('.', ','), rotulo = "Peso (kg)", corValor = BrandBlue)
                StatBlock(valor = imcFormatado, rotulo = "IMC", corValor = Mint)
                StatBlock(valor = "18%", rotulo = "Gordura", corValor = Coral)
                StatBlock(valor = "$treinos", rotulo = "Treinos", corValor = BrandDark)
            }
            }
            }

            RevealOnAppear(
                modifier = Modifier.fillMaxWidth(),
                delayMillis = 300,
                offsetY = 18.dp
            ) {
            Column {
            SectionHeading(titulo = "Medidas corporais", acao = "Atualizar")

            listOf(
                Triple("Cintura", "78 cm", "-2 cm no mês"),
                Triple("Peito", "98 cm", "+1 cm no mês"),
                Triple("Braço", "34 cm", "+0,5 cm no mês")
            ).forEachIndexed { index, (nome, valor, variacao) ->
                RevealOnAppear(
                    modifier = Modifier.fillMaxWidth(),
                    delayMillis = (340 + index * 60).coerceAtMost(520),
                    offsetY = 14.dp
                ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 9.dp)
                        .revealShadow(4.dp, RoundedCornerShape(16.dp), clip = false)
                        .clip(RoundedCornerShape(16.dp))
                        .background(SurfaceWhite)
                        .padding(horizontal = 15.dp, vertical = 13.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = nome, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Ink)
                        Text(text = variacao, fontSize = 11.sp, color = Muted)
                    }
                    Text(text = valor, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = BrandDark)
                }
                }
            }
            }
            }

            RevealOnAppear(
                modifier = Modifier.fillMaxWidth(),
                delayMillis = 360,
                offsetY = 16.dp
            ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(SoftBlueBackground)
                    .clickable { navController.navigate(Rotas.RELATORIO_MENSAL) }
                    .padding(15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "Relatório mensal", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Ink)
                    Text(
                        text = if (acessoRelatorio) {
                            "Veja sua evolução completa de abril"
                        } else {
                            "Disponível no plano Profissional"
                        },
                        fontSize = 12.sp,
                        color = Muted
                    )
                }
                if (acessoRelatorio) {
                    ChevronRight(tint = BrandBlue)
                } else {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = "Disponível no plano Profissional",
                        tint = Muted,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            }

            RevealOnAppear(
                modifier = Modifier.fillMaxWidth(),
                delayMillis = 420,
                offsetY = 16.dp
            ) {
            PrimaryActionButton(
                texto = "Ver histórico de treinos",
                onClick = { navController.navigate(Rotas.HISTORICO_TREINOS) }
            )
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
private fun ResumoHojeCard(
    modifier: Modifier,
    icone: ImageVector,
    cor: Color,
    fundo: Color,
    valor: String,
    rotulo: String
) {
    Row(
        modifier = modifier
            .revealShadow(5.dp, RoundedCornerShape(15.dp), clip = false)
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
