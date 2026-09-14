package com.example.fitunity.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fitunity.R
import com.example.fitunity.data.PerfilCliente

/**
 * Tela de Perfil do usuário logado.
 *
 * onEditarDadosClick   -> abre a tela de edição de dados (nome, foco, peso, etc.)
 * onAssinaturaClick    -> abre a tela com detalhes do plano/assinatura
 * onNotificacoesClick  -> abre a tela de notificações (o chamador deve limpar o indicador "não lida")
 * onAjudaClick         -> abre a tela de ajuda / perguntas frequentes
 * onSairClick          -> encerra a sessão (SessionManager.logout()) e volta para o Login
 * onVerPlanoClick      -> leva para a lista de treinos
 *
 * fotoResId            -> drawable da foto de perfil; se null, mostra um ícone padrão
 * historicoPeso        -> valores usados no gráfico "Progresso" (ex.: peso por semana/mês)
 * temNotificacaoNaoLida-> controla o pontinho azul ao lado de "Notificações"
 */
@Composable
fun PerfilScreen(
    navController: NavController,
    perfil: PerfilCliente,
    foco: String = perfil.foco,
    fotoResId: Int? = null,
    verificado: Boolean = true,
    historicoPeso: List<Float> = listOf(80f, 79.3f, 79.6f, 78.4f, 77.8f, 78.6f, 78f, 77.5f),
    temNotificacaoNaoLida: Boolean = true,
    onEditarDadosClick: () -> Unit = {},
    onAssinaturaClick: () -> Unit = {},
    onNotificacoesClick: () -> Unit = {},
    onAjudaClick: () -> Unit = {},
    onSairClick: () -> Unit = {},
    onVerPlanoClick: () -> Unit = {}
) {
    Scaffold(
        topBar = { PerfilTopBar(onEditClick = onEditarDadosClick) },
        bottomBar = { FitUnityBottomBar(navController) },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Cabeçalho: foto (com selo verificado), nome, tempo cadastrado, nível/foco e plano
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(contentAlignment = Alignment.BottomEnd) {
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape)
                                .background(FitUnityBlue.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (fotoResId != null) {
                                Image(
                                    painter = painterResource(id = fotoResId),
                                    contentDescription = "Foto de perfil",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape)
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Filled.Person,
                                    contentDescription = null,
                                    tint = FitUnityBlue,
                                    modifier = Modifier.size(58.dp)
                                )
                            }
                        }

                        if (verificado) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                                    .padding(3.dp)
                                    .clip(CircleShape)
                                    .background(FitUnityBlue),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = "Perfil verificado",
                                    tint = Color.White,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = perfil.nome,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = FitUnityBlue
                    )
                    Text(
                        text = "Aluno desde ${perfil.tempoCadastrado}",
                        fontSize = 13.sp,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${perfil.nivel}  |  Foco: $foco",
                        fontSize = 13.sp,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Selo do plano (ex.: "Plano Premium") — toca para ir para Assinatura
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(FitUnityBlue)
                            .clickable { onAssinaturaClick() }
                            .padding(horizontal = 18.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Plano ${perfil.tipoPlano}",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Números: treinos realizados, peso atual e meta
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFE0E0E0))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    PerfilNumero(valor = "${perfil.treinosRealizados}", label = "Treino")
                    VerticalDivider()
                    PerfilNumero(valor = "${perfil.pesoAtual}kg", label = "Peso Atual")
                    VerticalDivider()
                    PerfilNumero(valor = "${perfil.objetivoPeso}kg", label = "Meta")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Progresso: gráfico de peso ao longo do tempo (feito com Canvas, sem libs externas)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Progresso",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = FitUnityBlue
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "kg", fontSize = 11.sp, color = Color.Black)
                        Spacer(modifier = Modifier.width(6.dp))
                        PesoLineChart(
                            valores = historicoPeso,
                            modifier = Modifier
                                .weight(1f)
                                .height(110.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row {
                        Spacer(modifier = Modifier.width(24.dp))
                        Text(text = "Mês", fontSize = 11.sp, color = Color.Black)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de opções — todas navegam de verdade
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
            ) {
                Column {
                    PerfilOpcao(icon = Icons.Filled.Person, texto = "Editar Dados", onClick = onEditarDadosClick)
                    HorizontalDivider(color = Color(0xFFE0E0E0))
                    PerfilOpcao(icon = Icons.Filled.Star, texto = "Assinatura", onClick = onAssinaturaClick)
                    HorizontalDivider(color = Color(0xFFE0E0E0))
                    PerfilOpcao(
                        icon = Icons.Filled.Notifications,
                        texto = "Notificações",
                        onClick = onNotificacoesClick,
                        mostrarPontoNaoLido = temNotificacaoNaoLida
                    )
                    HorizontalDivider(color = Color(0xFFE0E0E0))
                    PerfilOpcao(icon = Icons.Filled.Info, texto = "Ajuda!", onClick = onAjudaClick)
                    HorizontalDivider(color = Color(0xFFE0E0E0))
                    PerfilOpcao(icon = Icons.Filled.ExitToApp, texto = "Sair", onClick = onSairClick)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onVerPlanoClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.buttonColors(containerColor = FitUnityBlue)
            ) {
                Text(text = "Ver meu plano de treino", color = Color.White, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

/**
 * TopBar específica do Perfil: logo à esquerda, título centralizado e o
 * botão de edição (lápis) à direita, que dispara [onEditClick].
 */
@Composable
private fun PerfilTopBar(onEditClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp)
            .padding(top = 42.dp, bottom = 12.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_fitunity_logo),
            contentDescription = null,
            modifier = Modifier
                .size(28.dp)
                .align(Alignment.CenterStart)
        )

        Text(
            text = "Meu perfil",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.align(Alignment.Center)
        )

        IconButton(
            onClick = onEditClick,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFE9F6FF))
        ) {
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = "Editar perfil",
                tint = FitUnityBlue,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun VerticalDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(36.dp)
            .background(Color(0xFFE0E0E0))
    )
}

@Composable
private fun PerfilNumero(valor: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = valor, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = FitUnityBlue)
        Text(text = label, fontSize = 12.sp, color = Color.Black)
    }
}

@Composable
private fun PerfilOpcao(
    icon: ImageVector,
    texto: String,
    onClick: () -> Unit,
    mostrarPontoNaoLido: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = FitUnityBlue)
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = texto, fontSize = 15.sp, color = Color.Black, modifier = Modifier.weight(1f))
        if (mostrarPontoNaoLido) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(FitUnityBlue)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Icon(imageVector = Icons.Filled.KeyboardArrowRight, contentDescription = null, tint = Color.Gray)
    }
}

/**
 * Gráfico de linha simples (sem dependências externas) usado no card "Progresso".
 * Desenha uma grade leve, a linha conectando os valores e um ponto para cada valor.
 */
@Composable
private fun PesoLineChart(valores: List<Float>, modifier: Modifier = Modifier) {
    if (valores.size < 2) return

    val minValor = valores.min()
    val maxValor = valores.max()
    val faixa = (maxValor - minValor).let { if (it == 0f) 1f else it }

    Canvas(modifier = modifier) {
        val larguraPasso = size.width / (valores.size - 1)
        val alturaUtil = size.height * 0.8f
        val offsetY = size.height * 0.1f

        val pontos = valores.mapIndexed { index, valor ->
            val x = index * larguraPasso
            val yNormalizado = (valor - minValor) / faixa
            val y = offsetY + alturaUtil - (yNormalizado * alturaUtil)
            Offset(x, y)
        }

        val linhasGrade = 3
        repeat(linhasGrade + 1) { i ->
            val y = offsetY + (alturaUtil / linhasGrade) * i
            drawLine(
                color = Color(0xFFE0E0E0),
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = 1f
            )
        }

        for (i in 0 until pontos.size - 1) {
            drawLine(
                color = FitUnityBlue,
                start = pontos[i],
                end = pontos[i + 1],
                strokeWidth = 5f,
                cap = StrokeCap.Round
            )
        }

        pontos.forEach { ponto ->
            drawCircle(color = Color.White, radius = 6f, center = ponto)
            drawCircle(color = FitUnityBlue, radius = 4f, center = ponto)
        }
    }
}