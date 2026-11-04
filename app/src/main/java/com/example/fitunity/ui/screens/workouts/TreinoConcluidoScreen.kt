package com.example.fitunity.ui.screens.workouts

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.EmojiEvents
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fitunity.data.FitUnityDbHelper
import com.example.fitunity.data.SessionManager
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
fun TreinoConcluidoScreen(navController: NavController) {
    val context = LocalContext.current
    val db = remember { FitUnityDbHelper(context) }
    val usuarioId = SessionManager.usuarioAtual.value?.id ?: 0L
    val ultimoTreino = remember(usuarioId) { db.listarHistorico(usuarioId).firstOrNull() }
    val series = remember(usuarioId) {
        db.listarTreinos(usuarioId).firstOrNull()?.quantidadeSeries ?: 0
    }

    Scaffold(containerColor = Color(0xFFF8FBFD)) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(listOf(Color(0xFF0799ED), Color(0xFF06B6A4)))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.EmojiEvents,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(60.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Treino concluído!",
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold,
                color = Ink
            )
            Text(
                text = "Você completou mais uma sessão e está mais perto do seu objetivo.",
                fontSize = 13.sp,
                color = Muted,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 6.dp, start = 20.dp, end = 20.dp)
            )

            Spacer(modifier = Modifier.height(26.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(SurfaceWhite)
                    .padding(vertical = 18.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatBlock(valor = ultimoTreino?.duracao ?: "0 min", rotulo = "Duração", corValor = BrandBlue)
                StatBlock(
                    valor = ultimoTreino?.calorias?.filter { it.isDigit() } ?: "0",
                    rotulo = "Calorias",
                    corValor = Coral
                )
                StatBlock(valor = "$series", rotulo = "Séries", corValor = Mint)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFF1FAFF))
                    .padding(18.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            tint = BrandBlue,
                            modifier = Modifier.size(17.dp)
                        )
                    }
                    Spacer(modifier = Modifier.size(10.dp))
                    Text(
                        text = "Novo recorde pessoal",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Ink
                    )
                }
                Text(
                    text = "Você manteve a intensidade por mais tempo do que na última sessão.",
                    fontSize = 12.sp,
                    color = Muted,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(SurfaceWhite)
                        .clickable { navController.navigate(Rotas.HISTORICO_TREINOS) }
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Ver histórico",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrandDark
                    )
                }
            }

            PrimaryActionButton(
                texto = "Voltar ao início",
                onClick = {
                    navController.navigate(Rotas.HOME) {
                        popUpTo(Rotas.HOME) { inclusive = true }
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Compartilhar resultado",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = BrandDark,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceWhite)
                    .padding(horizontal = 20.dp, vertical = 11.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
