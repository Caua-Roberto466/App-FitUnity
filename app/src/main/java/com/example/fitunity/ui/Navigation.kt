package com.example.fitunity.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate("onboarding") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }
        composable("onboarding") {
            FitUnityOnboardingScreen(
                onComecarClick = { navController.navigate("cadastro") },
                onEntrarClick = { navController.navigate("login") }
            )
        }
        composable("cadastro") {
            CadastroScreen(
                onCadastrarClick = { nome, email, dataNascimento, genero, senha ->
                    // ex: FitUnityDbHelper(context).cadastrarUsuario(email, nome, dataNascimento, genero, senha)
                    navController.navigate("login")
                },
                onEntrarClick = { navController.navigate("login") }
            )
        }
        composable("login") {
            LoginScreen(
                onEntrarClick = { email, senha ->
                    // ex: validar/autenticar com FitUnityDbHelper e navegar para a tela principal
                    // navController.navigate("home")
                },
                onEsqueceuSenhaClick = {
                    // navController.navigate("recuperar_senha") // se você tiver essa tela
                },
                onCriarContaClick = {
                    navController.navigate("cadastro")
                }
            )
        }
    }
}