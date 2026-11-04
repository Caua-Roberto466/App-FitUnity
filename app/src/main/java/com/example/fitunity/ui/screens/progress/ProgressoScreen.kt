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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fitunity.data.SessionManager
import com.example.fitunity.ui.components.ChevronRight
import com.example.fitunity.ui.components.FitUnityBottomBar
import com.example.fitunity.ui.components.MenuAction
import com.example.fitunity.ui.components.MoreMenu
import com.example.fitunity.ui.components.PageHeader
import com.example.fitunity.ui.components.PrimaryActionButton
import com.example.fitunity.ui.components.ProgressRing
import com.example.fitunity.ui.components.SectionHeading
import com.example.fitunity.ui.components.StatBlock
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
fun ProgressoScreen(navController: NavController) {
    val semanas = listOf(35, 48, 62, 40, 75, 88, 70)
    val dias = listOf("S", "T", "Q", "Q", "S", "S", "D")
    val perfil = SessionManager.usuarioAtual.value
    val treinos = perfil?.treinosRealizados ?: 0
    val totalTreinos = treinos + (perfil?.treinosPendentes ?: 0)
    val consistencia = if (totalTreinos > 0) (treinos * 100 / totalTreinos).coerceIn(0, 100) else 0
    val peso = perfil?.pesoAtual ?: 68.4
    val altura = perfil?.altura?.takeIf { it > 0 } ?: 1.75
    val imc = peso / (altura * altura)
    val imcFormatado = String.format("%.1f", imc).replace('.', ',')

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

            PageHeader(
                titulo = "Progresso",
                subtitulo = "Seus resultados até aqui"
            ) {
                MoreMenu(
                    itens = listOf(
                        MenuAction("Relatório mensal", "Ver evolução detalhada") {
                            navController.navigate(Rotas.RELATORIO_MENSAL)
                        },
                        MenuAction("Histórico de treinos") {
                            navController.navigate(Rotas.HISTORICO_TREINOS)
                        }
                    )
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .background(
                        Brush.linearGradient(listOf(Color(0xFF0A8FE8), Color(0xFF0768A9)))
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

            SectionHeading(titulo = "Evolução semanal", acao = "Últimos 7 dias")

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .shadow(8.dp, RoundedCornerShape(20.dp), clip = false)
                    .clip(RoundedCornerShape(20.dp))
                    .background(SurfaceWhite)
                    .padding(horizontal = 14.dp, vertical = 16.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(9.dp)
            ) {
                semanas.forEachIndexed { index, valor ->
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height((valor * 0.75f).dp)
                                .clip(RoundedCornerShape(7.dp))
                                .background(if (index == 5) BrandBlue else Color(0xFFBCE8FF))
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = dias[index], fontSize = 11.sp, color = Muted)
                    }
                }
            }

            SectionHeading(titulo = "Indicadores")

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(6.dp, RoundedCornerShape(18.dp), clip = false)
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

            SectionHeading(titulo = "Medidas corporais", acao = "Atualizar")

            listOf(
                Triple("Cintura", "78 cm", "-2 cm no mês"),
                Triple("Peito", "98 cm", "+1 cm no mês"),
                Triple("Braço", "34 cm", "+0,5 cm no mês")
            ).forEach { (nome, valor, variacao) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 9.dp)
                        .shadow(4.dp, RoundedCornerShape(16.dp), clip = false)
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
                    Text(text = "Veja sua evolução completa de abril", fontSize = 12.sp, color = Muted)
                }
                ChevronRight(tint = BrandBlue)
            }

            PrimaryActionButton(
                texto = "Ver histórico de treinos",
                onClick = { navController.navigate(Rotas.HISTORICO_TREINOS) }
            )

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}
