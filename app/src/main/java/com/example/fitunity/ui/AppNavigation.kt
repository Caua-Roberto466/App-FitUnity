package com.example.fitunity.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.fitunity.data.FitUnityDbHelper
import com.example.fitunity.data.SessionManager
import com.example.fitunity.ui.screens.CadastroScreen
import com.example.fitunity.ui.screens.DietaDetalheScreen
import com.example.fitunity.ui.screens.DietaScreen
import com.example.fitunity.ui.screens.FitUnityOnboardingScreen
import com.example.fitunity.ui.screens.HomeScreen
import com.example.fitunity.ui.screens.LoginScreen
import com.example.fitunity.ui.screens.SplashScreen
import com.example.fitunity.ui.screens.TreinoDetalheScreen
import com.example.fitunity.ui.screens.TreinosScreen
import com.example.fitunity.ui.screens.dietasExemplo
import com.example.fitunity.ui.screens.treinosMock

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
    const val DIETA_DETALHE = "dieta_detalhe/{dietaId}"
    const val TREINO_DETALHE = "treino_detalhe/{treinoId}"

    fun dietaDetalhe(dietaId: String) = "dieta_detalhe/$dietaId"
    fun treinoDetalhe(treinoId: Int) = "treino_detalhe/$treinoId"
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

        // Login -> valida contra o banco de verdade e vai para a Home
        composable(Rotas.LOGIN) {
            val context = LocalContext.current
            val dbHelper = remember { FitUnityDbHelper(context) }
            var erro by remember { mutableStateOf<String?>(null) }

            LoginScreen(
                erro = erro,
                onEntrarClick = { email, senha ->
                    if (email.isBlank() || senha.isBlank()) {
                        erro = "Preencha e-mail e senha"
                    } else {
                        val perfil = dbHelper.autenticarUsuario(email, senha)
                        if (perfil != null) {
                            erro = null
                            SessionManager.login(perfil)
                            navController.navigate(Rotas.HOME) {
                                popUpTo(Rotas.SPLASH) { inclusive = true }
                            }
                        } else {
                            erro = "E-mail ou senha incorretos"
                        }
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

        // Cadastro -> cria o usuário de verdade no banco e vai para a Home
        composable(Rotas.CADASTRO) {
            val context = LocalContext.current
            val dbHelper = remember { FitUnityDbHelper(context) }
            var erro by remember { mutableStateOf<String?>(null) }

            CadastroScreen(
                erro = erro,
                onCadastrarClick = { nome, email, senha ->
                    if (nome.isBlank() || email.isBlank() || senha.isBlank()) {
                        erro = "Preencha todos os campos"
                    } else {
                        val idGerado = dbHelper.cadastrarUsuario(
                            nome = nome,
                            email = email,
                            senha = senha
                        )
                        if (idGerado == -1L) {
                            erro = "Este e-mail já está cadastrado"
                        } else {
                            erro = null
                            val perfil = dbHelper.buscarPerfilPorId(idGerado)
                            perfil?.let { SessionManager.login(it) }
                            navController.navigate(Rotas.HOME) {
                                popUpTo(Rotas.SPLASH) { inclusive = true }
                            }
                        }
                    }
                },
                onJaTenhoContaClick = {
                    navController.navigate(Rotas.LOGIN) {
                        popUpTo(Rotas.CADASTRO) { inclusive = true }
                    }
                }
            )
        }

        // Painel principal — lê o usuário logado via SessionManager
        composable(Rotas.HOME) {
            HomeScreen(navController = navController)
        }

        // Lista de treinos (também acessível pela barra inferior)
        composable(Rotas.TREINOS) {
            TreinosScreen(
                navController = navController,
                onTreinoClick = { treinoId ->
                    navController.navigate(Rotas.treinoDetalhe(treinoId))
                }
            )
        }

        // Detalhe de um treino específico
        composable(
            route = Rotas.TREINO_DETALHE,
            arguments = listOf(navArgument("treinoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val treinoId = backStackEntry.arguments?.getInt("treinoId") ?: -1
            val treino = treinosMock.find { it.id == treinoId }

            if (treino != null) {
                TreinoDetalheScreen(
                    treino = treino,
                    onVoltarClick = { navController.popBackStack() },
                    onIniciarTreinoClick = {
                        // TODO: registrar o treino iniciado no perfil do usuário (ex.: via FitUnityDbHelper)
                        navController.popBackStack()
                    }
                )
            } else {
                // Treino não encontrado (id inválido) -> volta para a lista
                navController.popBackStack()
            }
        }

        // Dieta (também acessível pela barra inferior)
        composable(Rotas.DIETA) {
            DietaScreen(
                navController = navController,
                onVerDietaClick = { dietaId ->
                    navController.navigate(Rotas.dietaDetalhe(dietaId))
                }
            )
        }

        // Detalhe de uma dieta específica
        composable(
            route = Rotas.DIETA_DETALHE,
            arguments = listOf(navArgument("dietaId") { type = NavType.StringType })
        ) { backStackEntry ->
            val dietaId = backStackEntry.arguments?.getString("dietaId") ?: ""
            val dieta = dietasExemplo.find { it.id == dietaId }

            if (dieta != null) {
                DietaDetalheScreen(
                    dieta = dieta,
                    onVoltarClick = { navController.popBackStack() },
                    onIniciarDietaClick = {
                        // TODO: registrar a dieta escolhida no perfil do usuário (ex.: via FitUnityDbHelper)
                        navController.popBackStack()
                    }
                )
            } else {
                // Dieta não encontrada (id inválido) -> volta para a lista
                navController.popBackStack()
            }
        }
    }
}
