package com.example.fitunity.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fitunity.ui.screens.CadastroScreen
import com.example.fitunity.ui.screens.DietaScreen
import com.example.fitunity.ui.screens.FitUnityOnboardingScreen
import com.example.fitunity.ui.screens.HomeScreen
import com.example.fitunity.ui.screens.LoginScreen
import com.example.fitunity.ui.screens.SplashScreen
import com.example.fitunity.ui.screens.TreinosScreen

/**
 * Nomes das rotas usadas no NavHost.
 * Centralizados aqui para evitar strings soltas espalhadas pelo código.
 */
object Rotas {
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val LOGIN = "login"
    const val CADASTRO = "cadastro"
    const val HOME = "home"
    const val TREINOS = "treinos"
    const val DIETA = "dieta"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Rotas.SPLASH
    ) {

        // Splash -> Onboarding (remove o splash da pilha para o botão "voltar" não abrir de novo)
        composable(Rotas.SPLASH) {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate(Rotas.ONBOARDING) {
                        popUpTo(Rotas.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        // Onboarding -> Cadastro (Começar) ou Login (Já tenho uma conta)
        composable(Rotas.ONBOARDING) {
            FitUnityOnboardingScreen(
                onComecarClick = {
                    navController.navigate(Rotas.CADASTRO)
                },
                onJaTenhoContaClick = {
                    navController.navigate(Rotas.LOGIN)
                }
            )
        }

        // Login -> Home (painel principal) após autenticar
        composable(Rotas.LOGIN) {
            LoginScreen(
                onEntrarClick = { _, _ ->
                    // TODO: validar credenciais (ex.: via FitUnityDbHelper) antes de navegar
                    navController.navigate(Rotas.HOME) {
                        popUpTo(Rotas.SPLASH) { inclusive = true }
                    }
                },
                onEsqueceuSenhaClick = {
                    // TODO: criar a tela de recuperação de senha e navegar até ela
                },
                onCriarContaClick = {
                    navController.navigate(Rotas.CADASTRO) {
                        popUpTo(Rotas.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        // Cadastro -> Home (painel principal) após criar a conta
        composable(Rotas.CADASTRO) {
            CadastroScreen(
                onCadastrarClick = { _, _, _ ->
                    // TODO: salvar o novo usuário (ex.: via FitUnityDbHelper) antes de navegar
                    navController.navigate(Rotas.HOME) {
                        popUpTo(Rotas.SPLASH) { inclusive = true }
                    }
                },
                onJaTenhoContaClick = {
                    navController.navigate(Rotas.LOGIN) {
                        popUpTo(Rotas.CADASTRO) { inclusive = true }
                    }
                }
            )
        }

        // Painel principal
        composable(Rotas.HOME) {
            HomeScreen(navController = navController)
        }

        // Lista de treinos (também acessível pela barra inferior)
        composable(Rotas.TREINOS) {
            TreinosScreen(navController = navController)
        }

        // Dieta (também acessível pela barra inferior)
        composable(Rotas.DIETA) {
            DietaScreen(navController = navController)
        }
    }
}
