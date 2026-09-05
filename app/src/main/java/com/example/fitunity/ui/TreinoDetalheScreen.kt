package com.example.fitunity.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.TrendingUp
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

// onVoltarClick -> deve voltar para a tela de listagem de treinos
// onIniciarTreinoClick -> ainda sem lógica real (TODO)
@Composable
fun TreinoDetalheScreen(
    treino: Treino,
    onVoltarClick: () -> Unit = {},
    onIniciarTreinoClick: () -> Unit = {}
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TreinoTopBar() },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Imagem de topo com botão voltar
            Box(modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(id = treino.imagemRes),
                    contentDescription = treino.titulo,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                )

                IconButton(
                    onClick = onVoltarClick,
                    modifier = Modifier
                        .padding(top = 40.dp, start = 12.dp)
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Voltar",
                        tint = Color.Black
                    )
                }
            }

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = treino.categoria,
                    color = FitUnityBlue,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = treino.titulo,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Estatísticas rápidas (duração e nível)
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    InfoChip(
                        icone = Icons.Filled.Timer,
                        texto = "${treino.duracaoMin} min"
                    )
                    InfoChip(
                        icone = Icons.Filled.TrendingUp,
                        texto = treino.nivel.label
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Sobre este treino",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Treino de ${treino.categoria.lowercase()} com duração aproximada de " +
                            "${treino.duracaoMin} minutos, indicado para o nível ${treino.nivel.label.lowercase()}.",
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = onIniciarTreinoClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = FitUnityBlue)
                ) {
                    Text(text = "Iniciar treino", fontSize = 18.sp, color = Color.White)
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun InfoChip(icone: androidx.compose.ui.graphics.vector.ImageVector, texto: String) {
    Row(
        modifier = Modifier
            .background(Color(0xFFF2F2F2), RoundedCornerShape(20.dp))
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icone, contentDescription = null, tint = FitUnityBlue, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = texto, fontSize = 13.sp, color = Color.Black, fontWeight = FontWeight.Medium)
    }
}
