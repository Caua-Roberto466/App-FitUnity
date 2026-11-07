package com.example.fitunity.ui.screens.nutrition

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
import com.example.fitunity.ui.theme.Track

@Composable
fun PlanoSemanalScreen(navController: NavController) {
    val context = LocalContext.current
    val db = remember { FitUnityDbHelper(context) }
    val perfil = SessionManager.usuarioAtual.value
    val usuarioId = perfil?.id ?: 0L
    val refeicoes = remember(usuarioId) { db.listarRefeicoes(usuarioId) }
    var diaSelecionado by remember { mutableIntStateOf(0) }
    val totalCalorias = refeicoes.sumOf { refeicao ->
        refeicao.calorias.filter { it.isDigit() }.toIntOrNull() ?: 0
    }
    val meta = perfil?.metaCalorias?.takeIf { it > 0 } ?: 2200

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
                titulo = "Plano semanal",
                onBack = { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "Sua semana alimentar", fontSize = 23.sp, fontWeight = FontWeight.Bold, color = Ink)
            Text(
                text = "Um plano equilibrado, prático e ajustado à sua rotina.",
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
                diasSemana.forEachIndexed { index, dia ->
                    val ativo = index == diaSelecionado
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(13.dp))
                            .background(if (ativo) BrandBlue else SurfaceWhite)
                            .clickable { diaSelecionado = index }
                            .padding(horizontal = 10.dp, vertical = 9.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = dia,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (ativo) Color.White else Muted
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Box(
                            modifier = Modifier
                                .size(5.dp)
                                .clip(CircleShape)
                                .background(if (ativo) Color.White else Track)
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(SurfaceWhite)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "Calorias do dia", fontSize = 12.sp, color = Muted)
                    Text(text = "$totalCalorias kcal", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Ink)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "Meta", fontSize = 12.sp, color = Muted)
                    Text(text = "$meta kcal", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = BrandBlue)
                }
            }

            SectionHeading(titulo = "${diasSemana[diaSelecionado]} · refeições")

            refeicoes.forEach { refeicao ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(SurfaceWhite)
                        .clickable { navController.navigate(Rotas.refeicaoDetalhe(refeicao.id)) }
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(11.dp))
                            .background(Color(0xFFF1FAFF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = refeicao.periodo.take(1),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = BrandDark
                        )
                    }
                    Spacer(modifier = Modifier.width(11.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = refeicao.periodo, fontSize = 11.sp, color = Muted)
                        Text(
                            text = refeicao.titulo,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Ink
                        )
                    }
                    Text(text = refeicao.calorias, fontSize = 12.sp, color = BrandDark)
                    Spacer(modifier = Modifier.width(5.dp))
                    ChevronRight()
                }
            }

            PrimaryActionButton(
                texto = "Editar plano alimentar",
                onClick = { navController.navigate(Rotas.EDITAR_DIETA) }
            )

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}
