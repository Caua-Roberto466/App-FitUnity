package com.example.fitunity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.fitunity.ui.FitUnityOnboardingScreen
import com.example.fitunity.ui.theme.FitUnityTheme
import com.example.fitunity.ui.TreinosScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FitUnityTheme {
                FitUnityOnboardingScreen()
            }
        }
    }
}
