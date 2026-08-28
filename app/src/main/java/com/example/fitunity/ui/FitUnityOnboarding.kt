package com.example.fitunity.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitunity.R

// Cor principal do app
val FitUnityBlue = Color(0xFF009DF8)

@Composable
fun FitUnityOnboardingScreen(
    onComecarClick: () -> Unit = {},
    onJaTenhoContaClick: () -> Unit = {}
) {
    Scaffold(
        topBar = { FitUnityTopBar() },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "Faça parte da FitUnity",
                color = FitUnityBlue,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Imagem principal com cartões sobrepostos
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.treino_personal),
                    contentDescription = "Personal trainer ajudando aluno",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )

                // Badge kcal
                InfoBadge(
                    text = "+320 kcal",
                    icon = "🔥",
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .offset(x = (-8).dp, y = 24.dp)
                )


                // Badge "Ótimo ritmo! Continue!"
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = 8.dp, y = (-16).dp)
                        .background(Color.White, RoundedCornerShape(24.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Ótimo ritmo! ", fontSize = 13.sp, color = Color.Black)
                    Text(text = "Continue!", fontSize = 13.sp, color = FitUnityBlue, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.width(6.dp))

                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Monte treinos e dietas personalizados de forma simples e facil para conseguir ter uma vida mais saudavel e equilibrada",
                fontSize = 16.sp,
                color = Color.Black,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.weight(1f))

            // Botão Começar
            Button(
                onClick = onComecarClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = FitUnityBlue)
            ) {
                Text(text = "Começar", fontSize = 18.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botão Já tenho uma conta
            OutlinedButton(
                onClick = onJaTenhoContaClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, FitUnityBlue)
            ) {
                Text(text = "Já tenho uma conta", fontSize = 18.sp, color = Color.Black)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Logo rodapé
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.ic_fitunity_logo),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "FitUnity", color = FitUnityBlue, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun FitUnityTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp)
            .padding(top = 32.dp, bottom = 12.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_fitunity_logo),
            contentDescription = null,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = "FitUnity", color = FitUnityBlue, fontSize = 18.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun InfoBadge(text: String, icon: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .background(Color.White, RoundedCornerShape(24.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = icon, fontSize = 16.sp)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, fontSize = 13.sp, color = Color.Black, fontWeight = FontWeight.Medium)
    }
}