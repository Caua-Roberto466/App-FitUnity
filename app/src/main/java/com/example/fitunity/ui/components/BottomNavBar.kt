package com.example.fitunity.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.fitunity.ui.navigation.Rotas
import com.example.fitunity.ui.theme.BrandBlue
import com.example.fitunity.ui.theme.Muted
import com.example.fitunity.ui.theme.SoftBlueBackground
import com.example.fitunity.ui.theme.SoftBlueBorder

enum class BottomNavItem(val rota: String, val label: String, val icon: ImageVector) {
    INICIO(Rotas.HOME, "Início", Icons.Filled.Home),
    TREINOS(Rotas.TREINOS, "Treinos", Icons.Filled.FitnessCenter),
    ALIMENTACAO(Rotas.DIETA, "Alimentação", Icons.Filled.Restaurant),
    PROGRESSO(Rotas.PROGRESSO, "Progresso", Icons.Filled.BarChart),
    PERFIL(Rotas.PERFIL, "Perfil", Icons.Filled.Person)
}

@Composable
fun FitUnityBottomBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val rotaAtual = navBackStackEntry?.destination?.route

    Column {
        HorizontalDivider(
            thickness = 1.dp,
            color = SoftBlueBorder.copy(alpha = 0.55f)
        )
        NavigationBar(containerColor = Color.White, tonalElevation = 0.dp) {
            BottomNavItem.entries.forEach { item ->
                val selecionado = rotaAtual == item.rota
                NavigationBarItem(
                    selected = selecionado,
                    onClick = {
                        if (rotaAtual != item.rota) {
                            navController.navigate(item.rota) {
                                popUpTo(Rotas.HOME) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    icon = { Icon(item.icon, contentDescription = item.label) },
                    label = {
                        Text(
                            text = item.label,
                            fontSize = 11.sp,
                            fontWeight = if (selecionado) FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BrandBlue,
                        selectedTextColor = BrandBlue,
                        indicatorColor = SoftBlueBackground,
                        unselectedIconColor = Muted,
                        unselectedTextColor = Muted
                    )
                )
            }
        }
    }
}
