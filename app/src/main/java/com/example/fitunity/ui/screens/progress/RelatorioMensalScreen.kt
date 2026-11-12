package com.example.fitunity.ui.screens.progress

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.example.fitunity.data.RecursoPlano
import com.example.fitunity.data.SessionManager
import com.example.fitunity.data.possuiAcesso
import com.example.fitunity.ui.components.CalorieRing
import com.example.fitunity.ui.components.ConteudoBloqueadoPorPlano
import com.example.fitunity.ui.components.DetailHeader
import com.example.fitunity.ui.components.MacroBar
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
fun RelatorioMensalScreen(navController: NavController) {
    if (!possuiAcesso(RecursoPlano.RELATORIO_MENSAL)) {
        ConteudoBloqueadoPorPlano(
            recurso = RecursoPlano.RELATORIO_MENSAL,
            onVoltar = { navController.popBackStack() },
            onVerPlanos = { navController.navigate(Rotas.PLANOS) }
        )
        return
    }

    val context = LocalContext.current
    val db = remember { FitUnityDbHelper(context) }
    val perfil = SessionManager.usuarioAtual.value
    val usuarioId = perfil?.id ?: 0L
    val historico = remember(usuarioId) { db.listarHistorico(usuarioId) }
    val treinos = historico.size
    val minutos = historico.sumOf { treino ->
        treino.duracao.substringBefore(":").filter { it.isDigit() }.toIntOrNull()
            ?: treino.duracao.filter { it.isDigit() }.toIntOrNull() ?: 0
    }
    val kcal = historico.sumOf { treino ->
        treino.calorias.filter { it.isDigit() }.toIntOrNull() ?: 0
    }
    val totalTreinos = (perfil?.treinosRealizados ?: 0) + (perfil?.treinosPendentes ?: 0)
    val consistencia = if (totalTreinos > 0) {
        ((perfil?.treinosRealizados ?: 0) * 100 / totalTreinos).coerceIn(0, 100)
    } else 0

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
                titulo = "Relatório mensal",
                onBack = { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Brush.linearGradient(listOf(Color(0xFF4CB6F2), Color(0xFF0E7BB8))))
                    .padding(20.dp)
            ) {
                Column {
                    Text(
                        text = "ABRIL DE 2026",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = "Seu mês em números",
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ResumoMensal("$treinos", "Treinos")
                        ResumoMensal("${minutos / 60}h", "Tempo ativo")
                        ResumoMensal("$kcal", "kcal")
                        ResumoMensal("$consistencia%", "Consistência")
                    }
                }
            }

            SectionHeading(titulo = "Distribuição de treinos")

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(SurfaceWhite)
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CalorieRing(
                    atual = "$treinos",
                    meta = "treinos no mês",
                    percentual = consistencia,
                    tamanho = 118.dp
                )
                Spacer(modifier = Modifier.size(18.dp))
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(11.dp)
                ) {
                    MacroBar(rotulo = "Superior", valor = "18 treinos", percentual = 0.72f, cor = BrandBlue)
                    MacroBar(rotulo = "Inferior", valor = "14 treinos", percentual = 0.58f, cor = Coral)
                    MacroBar(rotulo = "Cardio", valor = "10 treinos", percentual = 0.42f, cor = Mint)
                }
            }

            SectionHeading(titulo = "Destaques do mês")

            listOf(
                Triple("Maior sequência", "9 dias seguidos", "Seu recorde pessoal"),
                Triple("Melhor treino", "Inferior · 52 min", "410 kcal queimadas"),
                Triple("Evolução de carga", "+15% no supino", "De 35 kg para 40 kg")
            ).forEach { (titulo, valor, detalhe) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(SurfaceWhite)
                        .padding(15.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF1FAFF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = titulo.take(1),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = BrandDark
                        )
                    }
                    Spacer(modifier = Modifier.size(11.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = titulo, fontSize = 12.sp, color = Muted)
                        Text(text = valor, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Ink)
                    }
                    Text(text = detalhe, fontSize = 11.sp, color = Muted)
                }
            }

            SectionHeading(titulo = "Comparativo")

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(SurfaceWhite)
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatBlock(valor = "+8", rotulo = "Treinos vs. março", corValor = Mint)
                StatBlock(valor = "-1,2 kg", rotulo = "Peso", corValor = BrandBlue)
                StatBlock(valor = "+3%", rotulo = "Massa magra", corValor = Coral)
            }

            PrimaryActionButton(
                texto = "Compartilhar relatório",
                onClick = { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
private fun ResumoMensal(valor: String, rotulo: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = valor, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text(text = rotulo, fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f))
    }
}
