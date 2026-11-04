package com.example.fitunity.ui.screens.nutrition

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import com.example.fitunity.ui.components.DetailHeader
import com.example.fitunity.ui.components.PrimaryActionButton
import com.example.fitunity.ui.components.SectionHeading
import com.example.fitunity.ui.components.StatBlock
import com.example.fitunity.ui.navigation.Rotas
import com.example.fitunity.ui.theme.BrandBlue
import com.example.fitunity.ui.theme.BrandDark
import com.example.fitunity.ui.theme.Coral
import com.example.fitunity.ui.theme.Ink
import com.example.fitunity.ui.theme.Mint
import com.example.fitunity.ui.theme.Muted
import com.example.fitunity.ui.theme.SurfaceWhite

@Composable
fun CalendarioDietaScreen(navController: NavController) {
    val context = LocalContext.current
    val db = remember { FitUnityDbHelper(context) }
    val perfil = SessionManager.usuarioAtual.value
    val usuarioId = perfil?.id ?: 0L
    val refeicoes = remember(usuarioId) { db.listarRefeicoes(usuarioId) }
    val concluidas = refeicoes.filter { it.concluida }
    val totalCalorias = concluidas.sumOf { refeicao ->
        refeicao.calorias.filter { it.isDigit() }.toIntOrNull() ?: 0
    }
    val agua = perfil?.aguaMl ?: 0
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
                titulo = "Calendário alimentar",
                onBack = { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "Abril de 2026", fontSize = 23.sp, fontWeight = FontWeight.Bold, color = Ink)
            Text(
                text = "Acompanhe a consistência da sua alimentação.",
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
                            val registrado = dia % 2 == 0 || dia == 18
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(11.dp))
                                    .background(
                                        when {
                                            selecionado -> BrandBlue
                                            registrado -> Color(0xFFF1FAFF)
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
                                        registrado -> BrandDark
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

            SectionHeading(titulo = "Resumo do dia $diaSelecionado")

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(SurfaceWhite)
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatBlock(valor = "$totalCalorias", rotulo = "Calorias", corValor = BrandBlue)
                StatBlock(valor = "${agua / 1000},${(agua % 1000) / 100} L", rotulo = "Água", corValor = Mint)
                StatBlock(valor = "${concluidas.size}", rotulo = "Refeições", corValor = Coral)
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFF1FAFF))
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .height(7.dp)
                        .weight(1f)
                        .clip(CircleShape)
                        .background(BrandBlue)
                )
                Spacer(modifier = Modifier.padding(horizontal = 6.dp))
                Text(text = "Meta atingida em 90%", fontSize = 12.sp, color = BrandDark)
            }

            PrimaryActionButton(
                texto = "Registrar refeição",
                onClick = { navController.navigate(Rotas.PLANO_SEMANAL) }
            )

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}
