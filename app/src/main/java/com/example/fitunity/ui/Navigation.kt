package com.example.fitunity.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.fitunity.data.FitUnityDbHelper

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val dbHelper = remember { FitUnityDbHelper(context) }

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
                    // Envia as informações preenchidas para o banco de dados (SQLite)
                    dbHelper.cadastrarUsuario(
                        email = email,
                        nome = nome,
                        dataNascimento = dataNascimento,
                        genero = genero,
                        senha = senha
                    )
                    // Depois de salvar, vai para a página inicial (limpa a pilha até o splash)
                    navController.navigate("home") {
                        popUpTo("splash") { inclusive = true }
                    }
                },
                onEntrarClick = { navController.navigate("login") }
            )
        }
        composable("login") {
            LoginScreen(
                onEntrarClick = { email, senha ->
                    val usuarioId = dbHelper.realizarLogin(email, senha)
                    if (usuarioId != null) {
                        navController.navigate("home") {
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                    // Se usuarioId for null, e-mail ou senha estão incorretos
                },
                onEsqueceuSenhaClick = {
                    // navController.navigate("recuperar_senha") // se você tiver essa tela
                },
                onCriarContaClick = {
                    navController.navigate("cadastro")
                }
            )
        }
        // Página inicial (home) exibida após cadastro ou login
        composable("home") {
            TreinosScreen(
                onTreinoClick = { treinoId ->
                    navController.navigate("detalhe/$treinoId")
                }
            )
        }
        composable("detalhe/{treinoId}") { backStackEntry ->
            val treinoId = backStackEntry.arguments?.getString("treinoId")?.toIntOrNull()
            val treino = treinosMock.find { it.id == treinoId }
            if (treino != null) {
                TreinoDetalheScreen(treino = treino)
            }
        }
        // Busca os dados desse usuário no banco e exibe
        composable(
            route = "perfil/{usuarioId}",
            arguments = listOf(navArgument("usuarioId") { type = NavType.LongType })
        ) { backStackEntry ->
            val usuarioId = backStackEntry.arguments?.getLong("usuarioId") ?: -1L
            val perfil = remember(usuarioId) { dbHelper.obterPerfilCliente(usuarioId) }

            if (perfil != null) {
                PerfilScreen(
                    perfil = perfil,
                    onVoltarClick = { navController.navigate("home/$usuarioId") }
                )
            } else {
                Text("Perfil não encontrado")
            }
        }
    }
}