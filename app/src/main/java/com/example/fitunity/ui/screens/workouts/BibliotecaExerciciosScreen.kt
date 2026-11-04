package com.example.fitunity.ui.screens.workouts

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fitunity.data.FitUnityDbHelper
import com.example.fitunity.ui.components.ChevronRight
import com.example.fitunity.ui.components.DetailHeader
import com.example.fitunity.ui.components.FilterChips
import com.example.fitunity.ui.components.SearchField
import com.example.fitunity.ui.components.SectionHeading
import com.example.fitunity.ui.navigation.Rotas
import com.example.fitunity.ui.theme.BrandDark
import com.example.fitunity.ui.theme.Ink
import com.example.fitunity.ui.theme.Muted
import com.example.fitunity.ui.theme.SurfaceWhite

@Composable
fun BibliotecaExerciciosScreen(navController: NavController) {
    var busca by remember { mutableStateOf("") }
    var filtro by remember { mutableIntStateOf(0) }
    val grupos = listOf("Todos", "Peito", "Costas", "Ombros", "Bíceps", "Pernas", "Core")
    val context = LocalContext.current
    val db = remember { FitUnityDbHelper(context) }
    val todos = remember { db.listarExercicios() }

    val exercicios = todos.filter { exercicio ->
        val combinaBusca = busca.isBlank() ||
            exercicio.nome.contains(busca, ignoreCase = true) ||
            exercicio.grupo.contains(busca, ignoreCase = true)
        val combinaFiltro = filtro == 0 || exercicio.grupo == grupos[filtro]
        combinaBusca && combinaFiltro
    }

    Scaffold(containerColor = Color(0xFFF8FBFD)) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 22.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            DetailHeader(
                titulo = "Biblioteca de exercícios",
                onBack = { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "Encontre seu exercício", fontSize = 23.sp, fontWeight = FontWeight.Bold, color = Ink)
            Text(
                text = "Explore por grupo muscular e monte seu treino.",
                fontSize = 13.sp,
                color = Muted,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            SearchField(
                value = busca,
                onValueChange = { busca = it },
                placeholder = "Buscar exercício..."
            )

            Spacer(modifier = Modifier.height(12.dp))

            FilterChips(
                opcoes = grupos,
                selecionado = filtro,
                onSelecionar = { filtro = it }
            )

            SectionHeading(titulo = "${exercicios.size} exercícios encontrados")

            exercicios.forEach { exercicio ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 9.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(SurfaceWhite)
                        .clickable { navController.navigate(Rotas.exercicioDetalhe(exercicio.id)) }
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = exercicio.imagem),
                        contentDescription = null,
                        modifier = Modifier
                            .size(54.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = exercicio.nome, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Ink)
                        Text(
                            text = "${exercicio.grupo} · ${exercicio.series} × ${exercicio.repeticoes}",
                            fontSize = 12.sp,
                            color = Muted
                        )
                    }
                    Text(text = exercicio.carga, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = BrandDark)
                    Spacer(modifier = Modifier.width(4.dp))
                    ChevronRight()
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}
