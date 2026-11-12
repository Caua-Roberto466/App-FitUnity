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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
fun EditarTreinoScreen(navController: NavController, treinoId: Int) {
    val context = LocalContext.current
    val db = remember { FitUnityDbHelper(context) }
    val usuarioId = SessionManager.usuarioAtual.value?.id ?: 0L
    val exercicios = remember { db.listarExercicios() }
    val treino = remember(treinoId, usuarioId) {
        db.listarTreinos(usuarioId).firstOrNull { it.id == treinoId }
    }
    val focos = listOf("Superior", "Inferior", "Full body", "Cardio")
    val imagens = listOf(
        R.drawable.treino_hoje,
        R.drawable.treino_pernas_gluteos,
        R.drawable.treino_full_body,
        R.drawable.corrida
    )

    var nome by remember(treinoId) { androidx.compose.runtime.mutableStateOf(treino?.titulo ?: "") }
    var foco by remember(treinoId) {
        androidx.compose.runtime.mutableIntStateOf(
            focos.indexOf(treino?.descricao).takeIf { it >= 0 } ?: 0
        )
    }
    val selecionados = remember(treinoId) {
        mutableStateListOf<Int>().apply {
            addAll(treino?.exercicios?.map { it.id } ?: emptyList())
        }
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
                titulo = "Editar treino",
                onBack = { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "Ajuste seu treino", fontSize = 23.sp, fontWeight = FontWeight.Bold, color = Ink)
            Text(
                text = "Personalize os exercícios e a ordem da sessão.",
                fontSize = 13.sp,
                color = Muted,
                modifier = Modifier.padding(top = 4.dp)
            )

            SectionHeading(titulo = "Informações")

            AppTextField(
                value = nome,
                onValueChange = { nome = it },
                placeholder = "Nome do treino",
                label = "Nome"
            )

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
                        .clip(RoundedCornerShape(16.dp))
                        .background(SurfaceWhite)
                        .clickable {
                            if (ativo) selecionados.remove(exercicio.id) else selecionados.add(exercicio.id)
                        }
                        .padding(13.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(if (ativo) BrandBlue else Color(0xFFEAF2F8)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (ativo) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.size(11.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = exercicio.nome, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Ink)
                        Text(text = exercicio.grupo, fontSize = 11.sp, color = Muted)
                    }
                    Text(text = exercicio.series + " séries", fontSize = 11.sp, color = BrandDark)
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
                Text(text = "Adicionar exercício", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = BrandDark)
            }

            SectionHeading(titulo = "Ordem dos exercícios")

            selecionados.forEachIndexed { index, id ->
                val exercicio = exercicios.firstOrNull { it.id == id } ?: return@forEachIndexed
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(SurfaceWhite)
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "${index + 1}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = BrandBlue)
                    Spacer(modifier = Modifier.size(10.dp))
                    Text(
                        text = exercicio.nome,
                        fontSize = 13.sp,
                        color = Ink,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Remover",
                        tint = Muted,
                        modifier = Modifier
                            .size(16.dp)
                            .clickable { selecionados.remove(id) }
                    )
                }
            }

            PrimaryActionButton(
                texto = "Salvar treino",
                onClick = {
                    db.atualizarTreino(
                        usuarioId = usuarioId,
                        treinoId = treinoId,
                        diaSemana = treino?.diaSemana ?: diasSemana[0],
                        titulo = nome.ifBlank { treino?.titulo ?: "Treino" },
                        descricao = focos[foco],
                        duracao = treino?.duracao ?: "45 min",
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
