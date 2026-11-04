package com.example.fitunity.ui.screens.nutrition

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fitunity.data.FitUnityDbHelper
import com.example.fitunity.data.SessionManager
import com.example.fitunity.ui.components.AppTextField
import com.example.fitunity.ui.components.DetailHeader
import com.example.fitunity.ui.components.FilterChips
import com.example.fitunity.ui.components.PrimaryActionButton
import com.example.fitunity.ui.components.SectionHeading
import com.example.fitunity.ui.theme.BrandBlue
import com.example.fitunity.ui.theme.BrandDark
import com.example.fitunity.ui.theme.Ink
import com.example.fitunity.ui.theme.Muted
import com.example.fitunity.ui.theme.SurfaceWhite

@Composable
fun EditarDietaScreen(navController: NavController) {
    val context = LocalContext.current
    val db = remember { FitUnityDbHelper(context) }
    val perfil = SessionManager.usuarioAtual.value
    val usuarioId = perfil?.id ?: 0L
    val objetivos = listOf("Emagrecer", "Manter", "Ganhar massa")
    val restricoes = listOf("Nenhuma", "Sem lactose", "Vegetariana")

    var meta by remember { mutableStateOf((perfil?.metaCalorias ?: 2200).toString()) }
    var refeicoes by remember { mutableStateOf((perfil?.refeicoesPorDia ?: 5).toString()) }
    var objetivo by remember {
        mutableIntStateOf(objetivos.indexOf(perfil?.objetivoDieta).takeIf { it >= 0 } ?: 0)
    }
    var restricao by remember {
        mutableIntStateOf(restricoes.indexOf(perfil?.restricaoAlimentar).takeIf { it >= 0 } ?: 0)
    }
    var observacoes by remember { mutableStateOf(perfil?.observacoesDieta ?: "") }

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
                titulo = "Editar dieta",
                onBack = { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "Ajuste seu plano", fontSize = 23.sp, fontWeight = FontWeight.Bold, color = Ink)
            Text(
                text = "Defina metas realistas para manter a consistência.",
                fontSize = 13.sp,
                color = Muted,
                modifier = Modifier.padding(top = 4.dp)
            )

            SectionHeading(titulo = "Meta calórica")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                listOf("1800", "2200", "2500").forEach { valor ->
                    val ativo = meta == valor
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (ativo) BrandBlue else SurfaceWhite)
                            .clickable { meta = valor }
                            .padding(vertical = 14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$valor kcal",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (ativo) Color.White else Ink
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            AppTextField(
                value = meta,
                onValueChange = { meta = it.filter { ch -> ch.isDigit() } },
                placeholder = "2200",
                label = "Valor personalizado (kcal)",
                keyboardType = KeyboardType.Number
            )

            SectionHeading(titulo = "Objetivo")

            FilterChips(
                opcoes = objetivos,
                selecionado = objetivo,
                onSelecionar = { objetivo = it }
            )

            SectionHeading(titulo = "Refeições por dia")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                listOf("3", "4", "5", "6").forEach { valor ->
                    val ativo = refeicoes == valor
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(13.dp))
                            .background(if (ativo) Color(0xFFF1FAFF) else SurfaceWhite)
                            .clickable { refeicoes = valor }
                            .padding(vertical = 13.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = valor,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (ativo) BrandDark else Ink
                        )
                    }
                }
            }

            SectionHeading(titulo = "Restrição alimentar")

            FilterChips(
                opcoes = restricoes,
                selecionado = restricao,
                onSelecionar = { restricao = it }
            )

            SectionHeading(titulo = "Observações")

            AppTextField(
                value = observacoes,
                onValueChange = { observacoes = it },
                placeholder = "Ex.: evitar frituras à noite",
                label = "Preferências e restrições"
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFF1FAFF))
                    .padding(13.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Novo plano", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Ink)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "$meta kcal · $refeicoes refeições",
                    fontSize = 12.sp,
                    color = Muted,
                    modifier = Modifier.weight(1f)
                )
            }

            PrimaryActionButton(
                texto = "Salvar alterações",
                onClick = {
                    db.atualizarDieta(
                        usuarioId = usuarioId,
                        metaCalorias = meta.toIntOrNull() ?: 2200,
                        refeicoesPorDia = refeicoes.toIntOrNull() ?: 5,
                        objetivo = objetivos[objetivo],
                        restricao = restricoes[restricao],
                        observacoes = observacoes
                    )
                    perfil?.let {
                        SessionManager.login(
                            it.copy(
                                metaCalorias = meta.toIntOrNull() ?: 2200,
                                refeicoesPorDia = refeicoes.toIntOrNull() ?: 5,
                                objetivoDieta = objetivos[objetivo],
                                restricaoAlimentar = restricoes[restricao],
                                observacoesDieta = observacoes
                            )
                        )
                    }
                    navController.popBackStack()
                }
            )

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}
