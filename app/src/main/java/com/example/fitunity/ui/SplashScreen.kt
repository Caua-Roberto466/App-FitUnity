package com.example.fitunity.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.fitunity.R
import kotlinx.coroutines.delay

// onSplashFinished -> chamado automaticamente depois do tempo de exibição,
// deve navegar para a tela de onboarding (ou login, se o usuário já estiver logado)
//
// Exemplo de uso com Navigation Compose:
//
// SplashScreen(
//     onSplashFinished = {
//         navController.navigate("onboarding") {
//             popUpTo("splash") { inclusive = true }
//         }
//     }
// )
@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    onSplashFinished: () -> Unit = {}
) {
    LaunchedEffect(Unit) {
        delay(2800)
        onSplashFinished()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.ic_fitunity_logo),
                contentDescription = "Logo do Splash",
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "FitUnity",
                style = MaterialTheme.typography.headlineSmall,
                color = FitUnityBlue
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Monte treinos e dietas personalizados",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(32.dp))

            CircularProgressIndicator(color = FitUnityBlue)
        }
    }
}