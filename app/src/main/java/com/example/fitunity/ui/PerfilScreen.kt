package com.example.fitunity.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fitunity.data.PerfilCliente

// onEditarDadosClick / onAssinaturaClick / onNotificacoesClick / onAjudaClick -> ainda sem
// tela própria (TODO); onSairClick -> deve encerrar a sessão (SessionManager.logout())
// e voltar para o Login; onVerPlanoClick -> leva para a lista de treinos.
@Composable
fun PerfilScreen(
    navController: NavController,
    perfil: PerfilCliente,
    onEditarDadosClick: () -> Unit = {},
    onAssinaturaClick: () -> Unit = {},
    onNotificacoesClick: () -> Unit = {},
    onAjudaClick: () -> Unit = {},
    onSairClick: () -> Unit = {},
    onVerPlanoClick: () -> Unit = {}
) {
    Scaffold(
        topBar = { FitUnityTopBar(titulo = "Meu perfil") },
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

            // Cabeçalho: nome, tempo cadastrado, nível e plano
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xF5F5F5F5))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(FitUnityBlue.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = null,
                            tint = FitUnityBlue,
                            modifier = Modifier.size(58.dp)
                        )
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
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${perfil.nivel}  |  Plano: ${perfil.tipoPlano}",
                        fontSize = 13.sp,
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Números: treinos realizados, peso atual e meta
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xF5F5F5F5))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    PerfilNumero(valor = "${perfil.treinosRealizados}", label = "Treinos")
                    PerfilNumero(valor = "${perfil.pesoAtual}kg", label = "Peso Atual")
                    PerfilNumero(valor = "${perfil.objetivoPeso}kg", label = "Meta")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Progresso (por enquanto só o número; gráfico fica pra depois)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xF5F5F5F5))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Progresso",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = FitUnityBlue
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${perfil.progresso}% da meta alcançada",
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de opções
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xF5F5F5F5))
            ) {
                Column {
                    PerfilOpcao(icon = Icons.Filled.Person, texto = "Editar Dados", onClick = onEditarDadosClick)
                    HorizontalDivider(color = Color(0xFFE0E0E0))
                    PerfilOpcao(icon = Icons.Filled.Star, texto = "Assinatura", onClick = onAssinaturaClick)
                    HorizontalDivider(color = Color(0xFFE0E0E0))
                    PerfilOpcao(icon = Icons.Filled.Notifications, texto = "Notificações", onClick = onNotificacoesClick)
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

@Composable
private fun PerfilNumero(valor: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = valor, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = FitUnityBlue)
        Text(text = label, fontSize = 12.sp, color = Color.Gray)
    }
}

@Composable
private fun PerfilOpcao(icon: ImageVector, texto: String, onClick: () -> Unit) {
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
        Icon(imageVector = Icons.Filled.KeyboardArrowRight, contentDescription = null, tint = Color.Gray)
    }
}


