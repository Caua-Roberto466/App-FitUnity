package com.example.fitunity.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitunity.R

/**
 * TopBar padrão usada em todas as telas principais (Início, Treino, Dieta, Perfil),
 * para manter a mesma identidade visual em todo o app: logo + "FitUnity" à esquerda,
 * título da tela ao centro, e ícones de notificação/configurações à direita.
 */
@Composable
fun FitUnityTopBar(
    titulo: String,
    onNotificationClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp)
            .padding(top = 42.dp, bottom = 12.dp)
    ) {
        Row(
            modifier = Modifier.align(Alignment.CenterStart),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_fitunity_logo),
                contentDescription = null,
                modifier = Modifier.size(30.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "FitUnity", color = FitUnityBlue, fontSize = 17.sp, fontWeight = FontWeight.Medium)
        }

        Text(
            text = titulo,
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.align(Alignment.Center)
        )

        Row(
            modifier = Modifier.align(Alignment.CenterEnd),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Notifications,
                contentDescription = "Notificações",
                tint = FitUnityBlue,
                modifier = Modifier.size(24.dp)
            )
            Icon(
                imageVector = Icons.Filled.Settings,
                contentDescription = "Configurações",
                tint = FitUnityBlue,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
