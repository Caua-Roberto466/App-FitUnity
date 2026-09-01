package com.example.fitunity.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.fitunity.ui.Rotas

/**
 * Itens da barra inferior. Início, Treino e Dieta já têm tela própria e navegam
 * de verdade; Perfil e Mais ainda não têm tela — os cliques ficam sem ação
 * (TODO) até essas telas serem criadas.
 */
enum class BottomNavItem(val rota: String, val label: String, val icon: ImageVector, val implementado: Boolean) {
    INICIO(Rotas.HOME, "Início", Icons.Filled.Home, true),
    TREINO(Rotas.TREINOS, "Treino", Icons.Filled.DirectionsRun, true),
    DIETA(Rotas.DIETA, "Dieta", Icons.Filled.Favorite, true),
    PERFIL("perfil", "Perfil", Icons.Filled.Person, false),
    MAIS("mais", "Mais", Icons.Filled.MoreHoriz, false)
}

/**
 * Barra de navegação inferior reutilizável. Basta colocar no parâmetro
 * `bottomBar` do Scaffold de qualquer tela, passando o NavController do app:
 *
 * Scaffold(bottomBar = { FitUnityBottomBar(navController) }) { ... }
 */
@Composable
fun FitUnityBottomBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val rotaAtual = navBackStackEntry?.destination?.route

    NavigationBar(containerColor = Color.White) {
        BottomNavItem.values().forEach { item ->
            NavigationBarItem(
                selected = rotaAtual == item.rota,
                onClick = {
                    if (item.implementado && rotaAtual != item.rota) {
                        navController.navigate(item.rota) {
                            popUpTo(Rotas.HOME)
                            launchSingleTop = true
                        }
                    }
                    // TODO: implementar telas de Perfil e "Mais" e navegar até elas
                },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = FitUnityBlue,
                    selectedTextColor = FitUnityBlue,
                    indicatorColor = Color.White,
                    unselectedIconColor = Color.Black,
                    unselectedTextColor = Color.Black
                )
            )
        }
    }
}
