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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
fun HistoricoTreinosScreen(navController: NavController) {
    val context = LocalContext.current
    val db = remember { FitUnityDbHelper(context) }
    val usuarioId = SessionManager.usuarioAtual.value?.id ?: 0L
    val historico = remember(usuarioId) { db.listarHistorico(usuarioId) }
    val treinos = remember(usuarioId) { db.listarTreinos(usuarioId) }
    val minutosTotais = historico.sumOf { treino ->
        treino.duracao.substringBefore(":").filter { it.isDigit() }.toIntOrNull()
            ?: treino.duracao.filter { it.isDigit() }.toIntOrNull() ?: 0
    }
    val kcalTotais = historico.sumOf { treino ->
        treino.calorias.filter { it.isDigit() }.toIntOrNull() ?: 0
    }

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
                titulo = "Histórico de treinos",
                onBack = { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "Seus treinos", fontSize = 23.sp, fontWeight = FontWeight.Bold, color = Ink)
            Text(
                text = "Cada sessão registrada é um passo na sua evolução.",
                fontSize = 13.sp,
                color = Muted,
                modifier = Modifier.padding(top = 4.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(SurfaceWhite)
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatBlock(valor = "${historico.size}", rotulo = "Treinos", corValor = BrandBlue)
                StatBlock(valor = "${minutosTotais / 60}h", rotulo = "Tempo total", corValor = Mint)
                StatBlock(valor = "$kcalTotais", rotulo = "kcal", corValor = Coral)
            }

            SectionHeading(titulo = "Últimas atividades")

            historico.forEach { treino ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 9.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(SurfaceWhite)
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEAF8F1)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            tint = Color(0xFF2FA36B),
                            modifier = Modifier.size(17.dp)
                        )
                    }
                    Spacer(modifier = Modifier.size(11.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = treino.titulo, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Ink)
                        Text(text = treino.data, fontSize = 11.sp, color = Muted)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = treino.duracao, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = BrandDark)
                        Text(text = treino.calorias, fontSize = 11.sp, color = Muted)
                    }
                }
            }

            PrimaryActionButton(
                texto = "Registrar novo treino",
                onClick = { treinos.firstOrNull()?.let { navController.navigate(Rotas.treinoAtivo(it.id)) } }
            )

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}
