package com.example.fitunity.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fitunity.R

// ---------- Modelo de dados ----------

data class Treino(
    val id: Int,
    val titulo: String,
    val categoria: String,
    val duracaoMin: Int,
    val nivel: Nivel,
    val imagemRes: Int
)

enum class Nivel(val label: String) {
    INICIANTE("Iniciante"),
    INTERMEDIARIO("Intermediario"),
    AVANCADO("Avancado"),
    EM_CASA("Em casa")
}

// Lista de exemplo — troque/complemente com seus treinos reais
val treinosMock = listOf(
    Treino(1, "Treino Full body", "Força/resistência", 40, Nivel.INICIANTE, R.drawable.treino_full_body),
    Treino(2, "Treino pernas e Glúteos", "Força/resistência", 50, Nivel.INICIANTE, R.drawable.treino_pernas_gluteos),
    Treino(3, "Treino HIIT Queima Gordura", "Cardio/resistência", 30, Nivel.INICIANTE, R.drawable.treino_hiit),
    Treino(4, "Treino de Flexões", "Cardio/resistência", 30, Nivel.INTERMEDIARIO, R.drawable.treino_flexoes)
)

// ---------- Tela principal ----------

// onTreinoClick(treinoId) -> deve navegar para a tela de detalhes daquele treino
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TreinosScreen(
    navController: NavController,
    treinos: List<Treino> = treinosMock,
    onTreinoClick: (Int) -> Unit = {}
) {
    var busca by remember { mutableStateOf("") }
    var nivelSelecionado by remember { mutableStateOf(Nivel.INICIANTE) }

    val treinosFiltrados = treinos.filter { treino ->
        treino.nivel == nivelSelecionado &&
                (busca.isBlank() || treino.titulo.contains(busca, ignoreCase = true))
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TreinoTopBar() },
        bottomBar = { FitUnityBottomBar(navController) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            OutlinedTextField(
                value = busca,
                onValueChange = { busca = it },
                placeholder = { Text("Buscar Treino...") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = FitUnityBlue,
                    cursorColor = FitUnityBlue
                )
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(Nivel.values().toList()) { nivel ->
                    FilterChip(
                        selected = nivel == nivelSelecionado,
                        onClick = { nivelSelecionado = nivel },
                        label = { Text(nivel.label) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = FitUnityBlue,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (treinosFiltrados.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Nenhum treino encontrado para esse nível",
                        color = Color.Gray,
                        fontSize = 15.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(treinosFiltrados) { treino ->
                        TreinoCard(
                            treino = treino,
                            onVerClick = { onTreinoClick(treino.id) }
                        )
                    }
                }
            }
        }
    }
}

// ---------- Componentes ----------

@Composable
private fun TreinoCard(treino: Treino, onVerClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = treino.imagemRes),
                contentDescription = treino.titulo,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = treino.titulo,
                    fontWeight = FontWeight.Bold,
                    color = FitUnityBlue
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
                    colors = ButtonDefaults.buttonColors(containerColor = FitUnityBlue)
                ) {
                    Text("Ver", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun TreinoTopBar(
    onNotificationClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp)
                .padding(top = 42.dp, bottom = 8.dp)
        ) {
            Row(
                modifier = Modifier.align(Alignment.CenterStart),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_fitunity_logo),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "FitUnity",
                    color = FitUnityBlue,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Text(
                text = "Treino",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.align(Alignment.Center)
            )

            Row(
                modifier = Modifier.align(Alignment.CenterEnd),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = "Notificações",
                    tint = FitUnityBlue,
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                )
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Configurações",
                    tint = FitUnityBlue,
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                )
            }
        }
        HorizontalDivider(color = FitUnityBlue.copy(alpha = 0.3f), thickness = 1.dp)
    }
}
