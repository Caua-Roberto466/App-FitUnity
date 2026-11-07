package com.example.fitunity.ui.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.fitunity.ui.screens.auth.CadastroScreen
import com.example.fitunity.ui.screens.auth.LoginScreen
import com.example.fitunity.ui.screens.auth.OnboardingScreen
import com.example.fitunity.ui.screens.auth.ReadyScreen
import com.example.fitunity.ui.screens.auth.RecuperacaoScreen
import com.example.fitunity.ui.screens.auth.SetupScreen
import com.example.fitunity.ui.screens.auth.SplashScreen
import com.example.fitunity.ui.screens.home.HomeScreen
import com.example.fitunity.ui.screens.messages.MensagensScreen
import com.example.fitunity.ui.screens.notifications.NotificacoesScreen
import com.example.fitunity.ui.screens.nutrition.CalendarioDietaScreen
import com.example.fitunity.ui.screens.nutrition.DietaScreen
import com.example.fitunity.ui.screens.nutrition.EditarDietaScreen
import com.example.fitunity.ui.screens.nutrition.PlanoSemanalScreen
import com.example.fitunity.ui.screens.nutrition.RefeicaoDetalheScreen
import com.example.fitunity.ui.screens.profile.EditarDadosScreen
import com.example.fitunity.ui.screens.profile.PerfilScreen
import com.example.fitunity.ui.screens.profile.PersonalScreen
import com.example.fitunity.ui.screens.profile.PlanosScreen
import com.example.fitunity.ui.screens.progress.HistoricoTreinosScreen
import com.example.fitunity.ui.screens.progress.ProgressoScreen
import com.example.fitunity.ui.screens.progress.RelatorioMensalScreen
import com.example.fitunity.ui.screens.workouts.BibliotecaExerciciosScreen
import com.example.fitunity.ui.screens.workouts.CalendarioTreinoScreen
import com.example.fitunity.ui.screens.workouts.CriarTreinoScreen
import com.example.fitunity.ui.screens.workouts.EditarTreinoScreen
import com.example.fitunity.ui.screens.workouts.ExercicioDetalheScreen
import com.example.fitunity.ui.screens.workouts.TreinoAtivoScreen
import com.example.fitunity.ui.screens.workouts.TreinoConcluidoScreen
import com.example.fitunity.ui.screens.workouts.TreinoDetalheScreen
import com.example.fitunity.ui.screens.workouts.TreinosScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val db = remember { FitUnityDbHelper(context) }

    var erroLogin by remember { mutableStateOf<String?>(null) }
    var erroCadastro by remember { mutableStateOf<String?>(null) }

    // Se já existe uma sessão salva, o app abre direto na Home, sem passar
    // pelo Splash nem pelo Onboarding.
    val perfilSalvo = remember {
        SessionManager.usuarioAtual.value
            ?: SessionManager.usuarioIdSalvo(context)?.let { db.buscarPerfilPorId(it) }
    }
    LaunchedEffect(perfilSalvo?.id) {
        if (perfilSalvo != null && SessionManager.usuarioAtual.value == null) {
            SessionManager.login(perfilSalvo)
        }
    }
    val destinoInicial = remember(perfilSalvo?.id) {
        if (perfilSalvo != null) Rotas.HOME else Rotas.SPLASH
    }

    NavHost(
        navController = navController,
        startDestination = destinoInicial,
        enterTransition = {
            fadeIn(animationSpec = tween(280)) +
                slideInHorizontally(animationSpec = tween(320)) { it / 14 }
        },
        exitTransition = {
            fadeOut(animationSpec = tween(180))
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(280)) +
                slideInHorizontally(animationSpec = tween(320)) { -it / 14 }
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(180)) +
                slideOutHorizontally(animationSpec = tween(320)) { it / 14 }
        }
    ) {
        composable(Rotas.SPLASH) {
            SplashScreen(
                onComecar = {
                    val perfil = SessionManager.usuarioAtual.value
                        ?: SessionManager.usuarioIdSalvo(context)?.let { db.buscarPerfilPorId(it) }
                    if (perfil != null) {
                        SessionManager.login(perfil, context)
                        navController.navigate(Rotas.HOME) {
                            popUpTo(navController.graph.id) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Rotas.ONBOARDING) {
                            popUpTo(Rotas.SPLASH) { inclusive = true }
                        }
                    }
                }
            )
        }

        composable(Rotas.ONBOARDING) {
            OnboardingScreen(
                onFinish = {
                    navController.navigate(Rotas.LOGIN) {
                        popUpTo(Rotas.ONBOARDING) { inclusive = true }
                    }
                }
            )
        }

        composable(Rotas.LOGIN) {
            LoginScreen(
                erro = erroLogin,
                onEntrarClick = { email, senha ->
                    val perfil = db.autenticarUsuario(email, senha)
                    if (perfil == null) {
                        erroLogin = "E-mail ou senha inválidos. Tente novamente."
                    } else {
                        erroLogin = null
                        SessionManager.login(perfil, context)
                        navController.navigate(Rotas.HOME) {
                            popUpTo(navController.graph.id) { inclusive = true }
                        }
                    }
                },
                onEsqueceuSenhaClick = { navController.navigate(Rotas.RECUPERACAO) },
                onCriarContaClick = {
                    erroCadastro = null
                    navController.navigate(Rotas.CADASTRO)
                },
                onVoltarClick = { navController.popBackStack() }
            )
        }

        composable(Rotas.CADASTRO) {
            CadastroScreen(
                erro = erroCadastro,
                onCadastrarClick = { nome, email, senha ->
                    val id = db.cadastrarUsuario(nome = nome, email = email, senha = senha)
                    if (id == -1L) {
                        erroCadastro = "Este e-mail já está cadastrado."
                    } else {
                        erroCadastro = null
                        db.buscarPerfilPorId(id)?.let { SessionManager.login(it, context) }
                        navController.navigate(Rotas.SETUP_OBJETIVO) {
                            popUpTo(navController.graph.id) { inclusive = true }
                        }
                    }
                },
                onJaTenhoContaClick = { navController.popBackStack() },
                onVoltarClick = { navController.popBackStack() }
            )
        }

        composable(Rotas.RECUPERACAO) {
            RecuperacaoScreen(
                onVoltarClick = { navController.popBackStack() },
                onEnviarClick = { navController.popBackStack() }
            )
        }

        composable(Rotas.SETUP_OBJETIVO) {
            SetupScreen(
                passo = 1,
                onVoltar = { navController.popBackStack() },
                onContinuar = { navController.navigate(Rotas.SETUP_FREQUENCIA) }
            )
        }

        composable(Rotas.SETUP_FREQUENCIA) {
            SetupScreen(
                passo = 2,
                onVoltar = { navController.popBackStack() },
                onContinuar = { navController.navigate(Rotas.SETUP_DIETA) }
            )
        }

        composable(Rotas.SETUP_DIETA) {
            SetupScreen(
                passo = 3,
                onVoltar = { navController.popBackStack() },
                onContinuar = { navController.navigate(Rotas.PRONTO) }
            )
        }

        composable(Rotas.PRONTO) {
            ReadyScreen(
                onIrParaInicio = {
                    navController.navigate(Rotas.HOME) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                },
                onReverPreferencias = {
                    navController.navigate(Rotas.SETUP_OBJETIVO) {
                        popUpTo(Rotas.SETUP_OBJETIVO) { inclusive = true }
                    }
                }
            )
        }

        composable(Rotas.HOME) { HomeScreen(navController) }
        composable(Rotas.TREINOS) { TreinosScreen(navController) }
        composable(Rotas.DIETA) { DietaScreen(navController) }
        composable(Rotas.PROGRESSO) { ProgressoScreen(navController) }
        composable(Rotas.PERFIL) { PerfilScreen(navController) }
        composable(Rotas.PLANOS) { PlanosScreen(navController) }
        composable(Rotas.NOTIFICACOES) { NotificacoesScreen(navController) }
        composable(Rotas.PERSONAL) { PersonalScreen(navController) }
        composable(Rotas.MENSAGENS) { MensagensScreen(navController) }
        composable(Rotas.EDITAR_DADOS) { EditarDadosScreen(navController) }

        composable(
            route = Rotas.TREINO_DETALHE,
            arguments = listOf(navArgument("treinoId") { type = NavType.IntType })
        ) { backStackEntry ->
            TreinoDetalheScreen(
                navController = navController,
                treinoId = backStackEntry.arguments?.getInt("treinoId") ?: 1
            )
        }

        composable(
            route = Rotas.EXERCICIO_DETALHE,
            arguments = listOf(navArgument("exercicioId") { type = NavType.IntType })
        ) { backStackEntry ->
            ExercicioDetalheScreen(
                navController = navController,
                exercicioId = backStackEntry.arguments?.getInt("exercicioId") ?: 1
            )
        }

        composable(
            route = Rotas.TREINO_ATIVO,
            arguments = listOf(navArgument("treinoId") { type = NavType.IntType })
        ) { backStackEntry ->
            TreinoAtivoScreen(
                navController = navController,
                treinoId = backStackEntry.arguments?.getInt("treinoId") ?: 1
            )
        }
        composable(Rotas.TREINO_CONCLUIDO) { TreinoConcluidoScreen(navController) }
        composable(Rotas.CALENDARIO_TREINO) { CalendarioTreinoScreen(navController) }
        composable(Rotas.CALENDARIO_DIETA) { CalendarioDietaScreen(navController) }
        composable(Rotas.HISTORICO_TREINOS) { HistoricoTreinosScreen(navController) }
        composable(Rotas.BIBLIOTECA_EXERCICIOS) { BibliotecaExerciciosScreen(navController) }
        composable(
            route = Rotas.EDITAR_TREINO,
            arguments = listOf(navArgument("treinoId") { type = NavType.IntType })
        ) { backStackEntry ->
            EditarTreinoScreen(
                navController = navController,
                treinoId = backStackEntry.arguments?.getInt("treinoId") ?: 1
            )
        }
        composable(Rotas.CRIAR_TREINO) { CriarTreinoScreen(navController) }

        composable(
            route = Rotas.REFEICAO_DETALHE,
            arguments = listOf(navArgument("refeicaoId") { type = NavType.LongType })
        ) { backStackEntry ->
            RefeicaoDetalheScreen(
                navController = navController,
                refeicaoId = backStackEntry.arguments?.getLong("refeicaoId") ?: 1L
            )
        }
        composable(Rotas.PLANO_SEMANAL) { PlanoSemanalScreen(navController) }
        composable(Rotas.EDITAR_DIETA) { EditarDietaScreen(navController) }

        composable(Rotas.RELATORIO_MENSAL) { RelatorioMensalScreen(navController) }
    }
}
