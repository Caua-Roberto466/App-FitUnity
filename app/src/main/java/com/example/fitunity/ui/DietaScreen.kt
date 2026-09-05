package com.example.fitunity.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitunity.R

// ---------- Modelo de dados ----------

data class Dieta(
    val id: String,
    val titulo: String,
    val descricao: String,
    val categoria: String, // "Perder peso", "Ganhar massa", "Vegana", "Zero Glúten"
    val pessoasFizeram: Int,
    val imagemRes: Int
)

// Lista de exemplo — troque/complemente com suas dietas reais
val dietasExemplo = listOf(
    Dieta(
        id = "1",
        titulo = "Perder peso com frutas e legumes",
        descricao = "Uma dieta que te faz perder peso apenas comendo frutas e legumes, junto de alimentos base de uma refeição, como arroz e feijão",
        categoria = "Perder peso",
        pessoasFizeram = 98756,
        imagemRes = R.drawable.ic_fitunity_logo
    ),
    Dieta(
        id = "2",
        titulo = "Perder peso com carnes suculentas",
        descricao = "Uma dieta para aqueles que gostam de saborear uma boa carne, mas que querem perder peso. E tudo isso essa dieta oferece",
        categoria = "Perder peso",
        pessoasFizeram = 106457,
        imagemRes = R.drawable.ic_fitunity_logo
    ),
    Dieta(
        id = "3",
        titulo = "Perder peso com dietas populares",
        descricao = "Uma dieta que te faz perder peso apenas comendo frutas e legumes, junto de alimentos base de uma refeição, como arroz e feijão",
        categoria = "Perder peso",
        pessoasFizeram = 506246,
        imagemRes = R.drawable.ic_fitunity_logo
    ),
    Dieta(
        id = "4",
        titulo = "Ganho de massa com proteínas",
        descricao = "Uma dieta rica em proteínas magras para ajudar no ganho de massa muscular de forma saudável",
        categoria = "Ganhar massa",
        pessoasFizeram = 45210,
        imagemRes = R.drawable.ic_fitunity_logo
    ),
    Dieta(
        id = "5",
        titulo = "Vegana equilibrada",
        descricao = "Uma dieta 100% vegana, com todos os nutrientes necessários para o dia a dia",
        categoria = "Vegana",
        pessoasFizeram = 32890,
        imagemRes = R.drawable.ic_fitunity_logo
    ),
    Dieta(
        id = "6",
        titulo = "Zero glúten para o dia a dia",
        descricao = "Uma dieta livre de glúten, ideal para quem tem intolerância ou sensibilidade",
        categoria = "Zero Glúten",
        pessoasFizeram = 18430,
        imagemRes = R.drawable.ic_fitunity_logo
    )
)

private val categorias = listOf("Perder peso", "Ganhar massa", "Vegana", "Zero Glúten")

// ---------- Tela principal ----------

// onVerDietaClick(dietaId) -> deve navegar para a tela de detalhes daquela dieta
// onNavItemClick(rota) -> deve navegar para a tela correspondente do bottom nav
@Composable
fun DietaScreen(
    dietas: List<Dieta> = dietasExemplo,
    onVerDietaClick: (String) -> Unit = {},
    onInfoClick: (String) -> Unit = {},
    onNavItemClick: (String) -> Unit = {},
    navController: Unit
) {
    var searchText by remember { mutableStateOf("") }
    var categoriaSelecionada by remember { mutableStateOf(categorias.first()) }

    val dietasFiltradas = remember(searchText, categoriaSelecionada, dietas) {
        dietas.filter { dieta ->
            val bateCategoria = if (searchText.isBlank()) dieta.categoria == categoriaSelecionada else true
            val bateBusca = if (searchText.isBlank()) true else
                dieta.titulo.contains(searchText, ignoreCase = true) ||
                        dieta.descricao.contains(searchText, ignoreCase = true)
            bateCategoria && bateBusca
        }
    }

    Scaffold(
        topBar = { DietaTopBar() },
        bottomBar = {
            DietaBottomNav(
                itemSelecionado = "Dieta",
                onItemClick = onNavItemClick
            )
        },
        containerColor = Color.White
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp)
        ) {
            // Barra de busca
            item {
                SearchBar(
                    value = searchText,
                    onValueChange = { searchText = it }
                )
                Spacer(modifier = Modifier.height(20.dp))
            }

            // Filtros
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(categorias) { categoria ->
                        FiltroChip(
                            texto = categoria,
                            selecionado = categoria == categoriaSelecionada && searchText.isBlank(),
                            onClick = {
                                categoriaSelecionada = categoria
                                searchText = ""
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            // Lista de dietas (ou mensagem de vazio)
            if (dietasFiltradas.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Nenhuma dieta encontrada",
                            color = Color.Gray,
                            fontSize = 15.sp
                        )
                    }
                }
            } else {
                items(dietasFiltradas, key = { it.id }) { dieta ->
                    DietaCard(
                        dieta = dieta,
                        onVerClick = { onVerDietaClick(dieta.id) },
                        onInfoClick = { onInfoClick(dieta.id) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

// ---------- Componentes ----------

@Composable
private fun DietaTopBar() {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp)
                .padding(top = 32.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_fitunity_logo),
                    contentDescription = null,
                    modifier = Modifier.size(26.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "FitUnity", color = FitUnityBlue, fontSize = 15.sp, fontWeight = FontWeight.Medium)
            }

            Text(
                text = "Dieta",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.weight(1f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box {
                    Icon(
                        imageVector = Icons.Filled.Notifications,
                        contentDescription = "Notificações",
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(14.dp)
                            .clip(CircleShape)
                            .background(FitUnityBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "1+", color = Color.White, fontSize = 8.sp)
                    }
                }
                Spacer(modifier = Modifier.width(14.dp))
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Configurações",
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        HorizontalDivider(color = FitUnityBlue.copy(alpha = 0.3f), thickness = 1.dp)
    }
}

@Composable
private fun SearchBar(value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text("Buscar dieta...", color = Color.Gray) },
        leadingIcon = {
            Icon(imageVector = Icons.Filled.Search, contentDescription = null, tint = Color.Gray)
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = FitUnityBlue,
            unfocusedBorderColor = Color.LightGray,
            focusedContainerColor = Color(0xFFF2F2F2),
            unfocusedContainerColor = Color(0xFFF2F2F2)
        )
    )
}

@Composable
private fun FiltroChip(texto: String, selecionado: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(24.dp))
            .background(if (selecionado) FitUnityBlue else Color.White)
            .border(
                width = 1.dp,
                color = if (selecionado) FitUnityBlue else Color.LightGray,
                shape = RoundedCornerShape(24.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 18.dp, vertical = 10.dp)
    ) {
        Text(
            text = texto,
            color = if (selecionado) Color.White else Color.Black,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun DietaCard(
    dieta: Dieta,
    onVerClick: () -> Unit,
    onInfoClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, FitUnityBlue.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Image(
                painter = painterResource(id = dieta.imagemRes),
                contentDescription = dieta.titulo,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, FitUnityBlue, RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = dieta.titulo,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = dieta.descricao,
                    fontSize = 12.sp,
                    color = Color.DarkGray,
                    maxLines = 2
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onVerClick,
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = FitUnityBlue),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Text(text = "Ver", color = Color.White, fontSize = 14.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Filled.ChevronRight,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = "${formatarNumero(dieta.pessoasFizeram)} pessoas já fizeram essa dieta",
                fontSize = 11.sp,
                color = Color.DarkGray,
                modifier = Modifier.weight(1f)
            )

            IconButton(onClick = onInfoClick, modifier = Modifier.size(24.dp)) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = "Mais informações",
                    tint = FitUnityBlue
                )
            }
        }
    }
}

fun formatarNumero(numero: Int): String {
    return "%,d".format(numero).replace(",", ".")
}

// ---------- Bottom Navigation ----------

@Composable
private fun DietaBottomNav(itemSelecionado: String, onItemClick: (String) -> Unit) {
    data class NavItem(val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)

    val itens = listOf(
        NavItem("Inicio", Icons.Filled.Home),
        NavItem("Treino", Icons.Filled.DirectionsRun),
        NavItem("Dieta", Icons.Filled.Favorite),
        NavItem("Perfil", Icons.Filled.Person),
        NavItem("Mais", Icons.Filled.MoreHoriz)
    )

    Column {
        HorizontalDivider(color = FitUnityBlue.copy(alpha = 0.3f), thickness = 1.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            itens.forEach { item ->
                val selecionado = item.label == itemSelecionado
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { onItemClick(item.label) }
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (selecionado) FitUnityBlue else Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = item.label,
                        fontSize = 11.sp,
                        color = if (selecionado) FitUnityBlue else Color.Black
                    )
                }
            }
        }
    } // fecha Column externa
} // fecha fun DietaBottomNav