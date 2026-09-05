package com.example.fitunity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.fitunity.ui.DietaScreen
import com.example.fitunity.ui.Navigation
import com.example.fitunity.ui.SplashScreen
import com.example.fitunity.ui.theme.FitUnityTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FitUnityTheme {
                SplashScreen()
//                val navController = rememberNavController()

//                NavHost(navController = navController, startDestination = "treinos") {
//                    composable("treinos") {
//                        TreinosScreen(
//                            onTreinoClick = { treinoId ->
//                                navController.navigate("detalhe/$treinoId")
//                            }
//                        )
//                    }
//                    composable("detalhe/{treinoId}") { backStackEntry ->
//                        val treinoId = backStackEntry.arguments?.getString("treinoId")?.toIntOrNull()
//                        val treino = treinosMock.find { it.id == treinoId }
//                        if (treino != null) {
//                            TreinoDetalheScreen(treino = treino)
//                        }
//                    }
//                }
//            }
            }
        }
    }
}