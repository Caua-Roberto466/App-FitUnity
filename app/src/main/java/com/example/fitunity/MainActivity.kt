package com.example.fitunity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.fitunity.ui.AppNavigation
import com.example.fitunity.ui.theme.FitUnityTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FitUnityTheme {
                AppNavigation()
            }
        }
    }
}
