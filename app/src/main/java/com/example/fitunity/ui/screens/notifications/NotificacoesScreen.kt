package com.example.fitunity.ui.screens.notifications

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fitunity.data.FitUnityDbHelper
import com.example.fitunity.data.Notificacao
import com.example.fitunity.data.SessionManager
import com.example.fitunity.data.TipoNotificacao
import com.example.fitunity.ui.components.DetailHeader
import com.example.fitunity.ui.components.SectionHeading
import com.example.fitunity.ui.theme.BrandBlue
import com.example.fitunity.ui.theme.BrandDark
import com.example.fitunity.ui.theme.Coral
import com.example.fitunity.ui.theme.Ink
import com.example.fitunity.ui.theme.Mint
import com.example.fitunity.ui.theme.Muted
import com.example.fitunity.ui.theme.SoftBlueBackground
import com.example.fitunity.ui.theme.SurfaceWhite

@Composable
fun NotificacoesScreen(navController: NavController) {
    val context = LocalContext.current
    val db = remember { FitUnityDbHelper(context) }
    val usuarioId = SessionManager.usuarioAtual.value?.id ?: 0L
    val notificacoes = remember(usuarioId) { db.listarNotificacoes(usuarioId) }
    val lidas = remember(usuarioId) {
        mutableStateListOf<Long>().apply {
            addAll(notificacoes.filter { it.lida }.map { it.id })
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
                titulo = "Notificações",
                onBack = { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "Avisos e lembretes", fontSize = 23.sp, fontWeight = FontWeight.Bold, color = Ink)
            Text(
                text = "Fique por dentro de tudo que acontece no seu plano.",
                fontSize = 13.sp,
                color = Muted,
                modifier = Modifier.padding(top = 4.dp)
            )

            SectionHeading(
                titulo = "Recentes",
                acao = "${notificacoes.count { it.id !in lidas }} não lidas"
            )

            notificacoes.forEach { notificacao ->
                val lida = notificacao.id in lidas
                NotificacaoRow(
                    notificacao = notificacao,
                    lida = lida,
                    onClick = {
                        if (!lida) {
                            db.marcarNotificacaoLida(usuarioId, notificacao.id)
                            lidas.add(notificacao.id)
                        }
                    }
                )
            }

            SectionHeading(titulo = "Preferências")

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(SurfaceWhite)
                    .padding(15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(11.dp))
                        .background(SoftBlueBackground),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.WaterDrop,
                        contentDescription = null,
                        tint = BrandBlue,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(11.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "Lembretes de hidratação", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Ink)
                    Text(text = "A cada 2 horas durante o dia", fontSize = 11.sp, color = Muted)
                }
                Text(text = "Ativo", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BrandBlue)
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
private fun NotificacaoRow(
    notificacao: Notificacao,
    lida: Boolean,
    onClick: () -> Unit
) {
    val (icone, cor, fundo) = when (notificacao.tipo) {
        TipoNotificacao.TREINO -> Triple(Icons.Filled.FitnessCenter, BrandBlue, SoftBlueBackground)
        TipoNotificacao.AGUA -> Triple(Icons.Filled.WaterDrop, BrandBlue, SoftBlueBackground)
        TipoNotificacao.CONQUISTA -> Triple(Icons.Filled.EmojiEvents, Color(0xFFF2A33C), Color(0xFFFFF6E6))
        TipoNotificacao.MENSAGEM -> Triple(Icons.Filled.Chat, Mint, Color(0xFFEAF8F1))
        TipoNotificacao.RELATORIO -> Triple(Icons.Filled.BarChart, Coral, Color(0xFFFDECEE))
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 9.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(if (lida) SurfaceWhite else Color(0xFFF1FAFF))
            .clickable { onClick() }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(fundo),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icone,
                contentDescription = null,
                tint = cor,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = notificacao.titulo,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Ink
                )
                if (!lida) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(BrandBlue)
                    )
                }
            }
            Text(
                text = notificacao.mensagem,
                fontSize = 12.sp,
                color = Muted,
                lineHeight = 17.sp,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = notificacao.horario, fontSize = 11.sp, color = Muted)
    }
}
