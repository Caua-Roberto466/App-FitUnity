package com.example.fitunity.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TreinosScreen(){
    var busca by remember { mutableStateOf("") }
    var nivelSelecionado by remember { mutableStateOf(Nivel.INICIANTE) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { FitUnityTopBar() },
        bottomBar = { BottomNavBar() }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            OutlinedTextField(
                value = busca,
                onValueChange = { busca = it },
                placeholder = { Text("Buscar Treino...") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            val treinosFiltrados = treinosMock.filter { it.nivel == nivelSelecionado }

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(Nivel.values().toList()) { nivel ->
                    FilterChip(
                        selected = nivel == nivelSelecionado,
                        onClick = { nivelSelecionado = nivel },
                        label = { Text(nivel.label) }
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(treinosFiltrados) { treino ->
                    TreinoCard(
                        treino = treino,
                        onVerClick = { /* por enquanto vazio */ }
                    )
                }
            }
        }
    }
}

@Composable
fun TreinoCard(treino: Treino, onVerClick: () -> Unit){
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color.LightGray, RoundedCornerShape(12.dp))
            )
            // <- Box fecha aqui, sem chaves { } abrindo nada dentro

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = treino.titulo,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF29B6F6)
                )
                Text(
                    text = "${treino.categoria} • ${treino.duracaoMin} min • ${treino.nivel.label}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(color = Color(0xFFE0E0E0))
                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onVerClick,
                    modifier = Modifier.align(Alignment.End),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF29B6F6))
                ) {
                    Text("Ver", color = Color.White)
                }
            }
        }
    }
}
@Composable
fun BottomNavBar() {
    var selecionado by remember { mutableStateOf(1) }

    NavigationBar {
        navItems.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selecionado == index,
                onClick = { selecionado = index },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}

data class Treino(
    val id: Int,
    val titulo: String,
    val categoria: String,
    val duracaoMin: Int,
    val nivel: Nivel
)

val treinosMock = listOf(
    Treino(1, "Treino Full body", "Força/resistência", 40, Nivel.INICIANTE),
    Treino(2, "Treino pernas e Glúteos", "Força/resistência", 50, Nivel.INICIANTE),
    Treino(3, "Treino HIIT Queima Gordura", "Cardio/resistência", 30, Nivel.INICIANTE),
    Treino(4, "Treino de Flexões", "Cardio/resistência", 30, Nivel.INTERMEDIARIO)
)

data class NavItem(val label: String, val icon: ImageVector)

enum class Nivel(val label: String) {
    INICIANTE("Iniciante"),
    INTERMEDIARIO("Intermediario"),
    AVANCADO("Avancado"),
    EM_CASA("Em casa")
}

val navItems = listOf(
    NavItem("Inicio", Icons.Filled.Home),
    NavItem("Treino", Icons.Filled.Search),
    NavItem("Dieta", Icons.Filled.Favorite),
    NavItem("Perfil", Icons.Filled.Person),
    NavItem("Mais", Icons.Filled.MoreVert)
)