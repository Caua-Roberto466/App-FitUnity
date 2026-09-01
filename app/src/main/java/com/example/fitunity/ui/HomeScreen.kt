package com.example.fitunity.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fitunity.R
import com.example.fitunity.data.SessionManager
import com.example.fitunity.ui.Rotas

data class TreinoResumo(
    val nome: String,
    val detalhe: String,
    @DrawableRes val imagemResId: Int
)

// onIrPraticaClick / onTreinoClick -> devem levar para a tela de Treino
// onNotificationClick / onSettingsClick -> ainda sem tela própria (TODO)
// Nome, nível e progresso vêm do usuário logado (SessionManager); os parâmetros
// abaixo servem de fallback (ex.: preview) caso ninguém esteja logado.
@Composable
fun HomeScreen(
    navController: NavController,
    treinosHoje: List<TreinoResumo> = listOf(
        TreinoResumo("Supino inclinado", "3 séries • 10 repetições", R.drawable.supino_inclinado),
        TreinoResumo("Barra Fixa", "4 séries • 8 repetições", R.drawable.barra_fixa),
        TreinoResumo("Corrida", "20 km • 30 min", R.drawable.corrida)
    )
) {
    val perfil = SessionManager.usuarioAtual.value

    val nomeUsuario = perfil?.nome ?: "Usuário"
    val treinoDoDiaNome = "Full Body - ${perfil?.nivel ?: "Intermediário"}"
    val treinosConcluidosSemana = perfil?.treinosRealizados ?: 0
    val metaSemana = (perfil?.let { it.treinosRealizados + it.treinosPendentes } ?: 0)
        .let { if (it > 0) it else 7 }

    Scaffold(
        topBar = {
            HomeTopBar(
                onNotificationClick = { /* TODO: tela de notificações */ },
                onSettingsClick = { /* TODO: tela de configurações */ }
            )
        },
        bottomBar = { FitUnityBottomBar(navController) },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Olá $nomeUsuario!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Vamos treinar hoje?",
                fontSize = 15.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Card "Treino de hoje"
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(20.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.treino_hoje),
                    contentDescription = "Treino de hoje",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Treino de hoje",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = treinoDoDiaNome,
                        color = Color.White,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = { navController.navigate(Rotas.TREINOS) },
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = FitUnityBlue),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
                    ) {
                        Text(text = "Ir pratica", color = Color.White, fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Filled.ChevronRight,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CarouselDots(total = 4, selecionado = 0)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Seus treinos",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(12.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                treinosHoje.forEach { treino ->
                    TreinoResumoCard(
                        treino = treino,
                        onClick = { navController.navigate(Rotas.TREINOS) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Seus Progresso",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "treinos concluídos essa semana: ",
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                        Text(
                            text = "$treinosConcluidosSemana",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = FitUnityBlue
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    ProgressoSemanal(concluidos = treinosConcluidosSemana, total = metaSemana)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun HomeTopBar(
    onNotificationClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp)
            .padding(top = 32.dp, bottom = 12.dp)
    ) {
        Row(
            modifier = Modifier.align(Alignment.CenterStart),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_fitunity_logo),
                contentDescription = null,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "FitUnity", color = FitUnityBlue, fontSize = 13.sp, fontWeight = FontWeight.Medium)
        }

        Text(
            text = "Início",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.align(Alignment.Center)
        )

        Row(
            modifier = Modifier.align(Alignment.CenterEnd),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Notifications,
                contentDescription = "Notificações",
                tint = FitUnityBlue,
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
            )
            Icon(
                imageVector = Icons.Filled.Settings,
                contentDescription = "Configurações",
                tint = FitUnityBlue,
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
            )
        }
    }
}

@Composable
private fun TreinoResumoCard(treino: TreinoResumo, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = treino.imagemResId),
                contentDescription = treino.nome,
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = treino.nome, fontSize = 15.sp, fontWeight = FontWeight.Medium, color = Color.Black)
                Text(text = treino.detalhe, fontSize = 13.sp, color = Color.Gray)
            }
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = FitUnityBlue
            )
        }
    }
}

@Composable
private fun CarouselDots(total: Int, selecionado: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
        repeat(total) { i ->
            Box(
                modifier = Modifier
                    .size(if (i == selecionado) 8.dp else 6.dp)
                    .clip(CircleShape)
                    .background(if (i == selecionado) FitUnityBlue else Color(0xFFD0E8FB))
            )
        }
    }
}

@Composable
private fun ProgressoSemanal(concluidos: Int, total: Int) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        for (i in 0 until total) {
            val preenchido = i < concluidos
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .clip(CircleShape)
                    .background(if (preenchido) FitUnityBlue else Color.White)
            )
            if (i != total - 1) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(2.dp)
                        .background(if (i < concluidos - 1) FitUnityBlue else Color(0xFFD0E8FB))
                )
            }
        }
    }
}
