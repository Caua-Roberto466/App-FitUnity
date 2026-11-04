package com.example.fitunity.ui.screens.workouts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fitunity.R
import com.example.fitunity.data.FitUnityDbHelper
import com.example.fitunity.data.SessionManager
import com.example.fitunity.data.diasSemana
import com.example.fitunity.ui.components.AppTextField
import com.example.fitunity.ui.components.DetailHeader
import com.example.fitunity.ui.components.FilterChips
import com.example.fitunity.ui.components.PrimaryActionButton
import com.example.fitunity.ui.components.SectionHeading
import com.example.fitunity.ui.navigation.Rotas
import com.example.fitunity.ui.theme.BrandBlue
import com.example.fitunity.ui.theme.BrandDark
import com.example.fitunity.ui.theme.Ink
import com.example.fitunity.ui.theme.Muted
import com.example.fitunity.ui.theme.SurfaceWhite

@Composable
fun CriarTreinoScreen(navController: NavController) {
    val context = LocalContext.current
    val db = remember { FitUnityDbHelper(context) }
    val usuarioId = SessionManager.usuarioAtual.value?.id ?: 0L
    val exercicios = remember { db.listarExercicios() }
    val focos = listOf("Superior", "Inferior", "Full body", "Cardio")
    val imagens = listOf(
        R.drawable.treino_hoje,
        R.drawable.treino_pernas_gluteos,
        R.drawable.treino_full_body,
        R.drawable.corrida
    )

    var nome by remember { mutableStateOf("") }
    var foco by remember { mutableIntStateOf(0) }
    var dia by remember { mutableIntStateOf(0) }
    val selecionados = remember { mutableStateListOf<Int>() }

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
                titulo = "Criar treino",
                onBack = { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "Monte uma nova rotina", fontSize = 23.sp, fontWeight = FontWeight.Bold, color = Ink)
            Text(
                text = "Escolha o dia, o foco e os exercícios da sessão.",
                fontSize = 13.sp,
                color = Muted,
                modifier = Modifier.padding(top = 4.dp)
            )

            SectionHeading(titulo = "Nome do treino")

            AppTextField(
                value = nome,
                onValueChange = { nome = it },
                placeholder = "Ex.: Superior A",
                label = "Nome"
            )

            SectionHeading(titulo = "Dia da semana")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                diasSemana.forEachIndexed { index, diaSemana ->
                    val ativo = index == dia
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(11.dp))
                            .background(if (ativo) BrandBlue else SurfaceWhite)
                            .clickable { dia = index }
                            .padding(vertical = 11.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = diaSemana.take(1),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (ativo) Color.White else Muted
                        )
                    }
                }
            }

            SectionHeading(titulo = "Foco principal")

            FilterChips(
                opcoes = listOf("Superior", "Inferior", "Full body", "Cardio"),
                selecionado = foco,
                onSelecionar = { foco = it }
            )

            SectionHeading(titulo = "Exercícios (${selecionados.size})")

            exercicios.take(5).forEach { exercicio ->
                val ativo = selecionados.contains(exercicio.id)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 9.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .background(SurfaceWhite)
                        .clickable {
                            if (ativo) selecionados.remove(exercicio.id) else selecionados.add(exercicio.id)
                        }
                        .padding(13.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(if (ativo) BrandBlue else Color(0xFFEAF2F8)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (ativo) {
                            Text(text = "✓", fontSize = 13.sp, color = Color.White)
                        }
                    }
                    Spacer(modifier = Modifier.size(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = exercicio.nome, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Ink)
                        Text(text = exercicio.grupo, fontSize = 11.sp, color = Muted)
                    }
                    Text(text = exercicio.repeticoes + " reps", fontSize = 11.sp, color = BrandDark)
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFF1FAFF))
                    .clickable { navController.navigate(Rotas.BIBLIOTECA_EXERCICIOS) }
                    .padding(13.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                    tint = BrandBlue,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(text = "Buscar mais exercícios", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = BrandDark)
            }

            PrimaryActionButton(
                texto = "Criar treino",
                onClick = {
                    val titulo = nome.ifBlank { "Treino de ${diasSemana[dia]}" }
                    db.criarTreino(
                        usuarioId = usuarioId,
                        diaSemana = diasSemana[dia],
                        titulo = titulo,
                        descricao = focos[foco],
                        duracao = "45 min",
                        imagem = imagens[foco],
                        foco = focos[foco],
                        exercicioIds = selecionados.toList()
                    )
                    navController.popBackStack()
                }
            )

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}
