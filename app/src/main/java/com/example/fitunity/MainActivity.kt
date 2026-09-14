package com.example.fitunity

import android.graphics.Color as AndroidColor
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.fitunity.ui.AppNavigation
import com.example.fitunity.ui.theme.FitUnityTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Fundo do app é claro, então força ícones escuros (bateria, wi-fi, hora etc.)
        // na barra de status, independente do tema do sistema (claro ou escuro).
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                AndroidColor.TRANSPARENT,
                AndroidColor.TRANSPARENT
            )
        )
        setContent {
            FitUnityTheme {
                AppNavigation()
            }
        }
    }
}
