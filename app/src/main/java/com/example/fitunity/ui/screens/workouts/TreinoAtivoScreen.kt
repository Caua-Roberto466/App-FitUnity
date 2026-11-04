package com.example.fitunity.ui.screens.workouts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.fitunity.data.FitUnityDbHelper
import com.example.fitunity.data.SessionManager
import com.example.fitunity.ui.components.DetailHeader
import com.example.fitunity.ui.components.PrimaryActionButton
import com.example.fitunity.ui.components.ProgressRing
import com.example.fitunity.ui.components.SectionHeading
import com.example.fitunity.ui.navigation.Rotas
import com.example.fitunity.ui.theme.BrandBlue
import com.example.fitunity.ui.theme.BrandDark
import com.example.fitunity.ui.theme.Coral
import com.example.fitunity.ui.theme.Ink
import com.example.fitunity.ui.theme.Mint
import com.example.fitunity.ui.theme.Muted
import com.example.fitunity.ui.theme.SoftBlueBackground
import com.example.fitunity.ui.theme.SurfaceWhite
import kotlinx.coroutines.delay

@Composable
fun TreinoAtivoScreen(navController: NavController, treinoId: Int) {
    val context = LocalContext.current
    val db = remember { FitUnityDbHelper(context) }
    val usuarioId = SessionManager.usuarioAtual.value?.id ?: 0L
    val treino = remember(treinoId, usuarioId) {
        val lista = db.listarTreinos(usuarioId)
        lista.firstOrNull { it.id == treinoId } ?: lista.firstOrNull()
    } ?: return
    var segundos by remember { mutableIntStateOf(0) }
    var pausado by remember { androidx.compose.runtime.mutableStateOf(false) }
    val concluidos = remember { mutableStateListOf<Int>() }

    LaunchedEffect(pausado) {
        while (!pausado) {
            delay(1000)
            segundos += 1
        }
    }

    val minutos = segundos / 60
    val segundosRestantes = segundos % 60
    val tempo = "%02d:%02d".format(minutos, segundosRestantes)
    val percentual = if (treino.exercicios.isEmpty()) 0
    else ((concluidos.size.toFloat() / treino.exercicios.size) * 100).toInt()

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
                titulo = "Treino em andamento",
                onBack = { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(SurfaceWhite)
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = treino.titulo,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BrandBlue
                )
                Text(
                    text = tempo,
                    fontSize = 43.sp,
                    fontWeight = FontWeight.Bold,
                    color = Ink,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Text(
                    text = "Tempo de treino",
                    fontSize = 12.sp,
                    color = Muted
                )
                Row(
                    modifier = Modifier.padding(top = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(SoftBlueBackground)
                            .clickable { pausado = !pausado },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (pausado) Icons.Filled.PlayArrow else Icons.Filled.Pause,
                            contentDescription = if (pausado) "Continuar" else "Pausar",
                            tint = BrandBlue,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.size(14.dp))
                    ProgressRing(
                        valor = percentual,
                        rotulo = "${concluidos.size} de ${treino.exercicios.size}",
                        tamanho = 62.dp
                    )
                }
            }

            SectionHeading(titulo = "Exercícios", acao = "${treino.duracao} restantes")

            treino.exercicios.forEach { exercicio ->
                val concluido = concluidos.contains(exercicio.id)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 9.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (concluido) Color(0xFFF1FAFF) else SurfaceWhite)
                        .clickable {
                            if (concluido) concluidos.remove(exercicio.id) else concluidos.add(exercicio.id)
                        }
                        .padding(13.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(if (concluido) BrandBlue else Color(0xFFEAF2F8)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (concluido) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(15.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.size(11.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = exercicio.nome,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Ink
                        )
                        Text(
                            text = "${exercicio.series} × ${exercicio.repeticoes} · ${exercicio.carga}",
                            fontSize = 11.sp,
                            color = Muted
                        )
                    }
                    Text(text = exercicio.intervalo, fontSize = 11.sp, color = BrandDark)
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(SoftBlueBackground)
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "60", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BrandDark)
                }
                Spacer(modifier = Modifier.size(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "Descanso entre séries", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Ink)
                    Text(text = "Respire e prepare-se para a próxima", fontSize = 11.sp, color = Muted)
                }
                Text(text = "60s", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = BrandBlue)
            }

            PrimaryActionButton(
                texto = "Finalizar treino",
                onClick = {
                    db.registrarTreinoConcluido(
                        usuarioId = usuarioId,
                        titulo = treino.titulo,
                        duracao = tempo,
                        calorias = "${maxOf(1, segundos / 60 * 6)} kcal"
                    )
                    navController.navigate(Rotas.TREINO_CONCLUIDO)
                }
            )

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}
