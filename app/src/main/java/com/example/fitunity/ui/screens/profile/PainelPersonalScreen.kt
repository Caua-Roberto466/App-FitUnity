package com.example.fitunity.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fitunity.data.AlunoPersonal
import com.example.fitunity.data.FitUnityDbHelper
import com.example.fitunity.data.SessionManager
import com.example.fitunity.data.StatusCandidatura
import com.example.fitunity.ui.components.ProfilePhoto
import com.example.fitunity.ui.components.DetailHeader
import com.example.fitunity.ui.components.PrimaryActionButton
import com.example.fitunity.ui.components.RevealOnAppear
import com.example.fitunity.ui.components.SectionHeading
import com.example.fitunity.ui.components.revealShadow
import com.example.fitunity.ui.navigation.Rotas
import com.example.fitunity.ui.theme.BrandBlue
import com.example.fitunity.ui.theme.BrandDark
import com.example.fitunity.ui.theme.Ink
import com.example.fitunity.ui.theme.Mint
import com.example.fitunity.ui.theme.MintBackground
import com.example.fitunity.ui.theme.Muted
import com.example.fitunity.ui.theme.SoftBlueBackground
import com.example.fitunity.ui.theme.SoftBlueBorder
import com.example.fitunity.ui.theme.SurfaceWhite

@Composable
fun PainelPersonalScreen(navController: NavController) {
    val context = LocalContext.current
    val db = remember { FitUnityDbHelper(context.applicationContext) }
    val perfil = SessionManager.usuarioAtual.value
    val usuarioId = perfil?.id ?: 0L
    val candidatura = remember(usuarioId) { db.buscarCandidatura(usuarioId) }
    val aprovado = candidatura?.status == StatusCandidatura.APROVADO

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
                titulo = "Painel do personal",
                onBack = { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (!aprovado) {
                AcessoRestritoPersonal(
                    onVoltar = { navController.popBackStack() },
                    onCandidatar = { navController.navigate(Rotas.TRABALHE_CONOSCO) }
                )
            } else {
                val alunos = remember(usuarioId) { db.listarAlunosElegiveis(usuarioId) }

                RevealOnAppear(
                    modifier = Modifier.fillMaxWidth(),
                    delayMillis = 0,
                    offsetY = 14.dp
                ) {
                    HeroPainel(
                        nome = perfil?.nome ?: "Personal",
                        fotoUri = perfil?.fotoUri,
                        totalAlunos = alunos.size
                    )
                }

                RevealOnAppear(
                    modifier = Modifier.fillMaxWidth(),
                    delayMillis = 70,
                    offsetY = 16.dp
                ) {
                    Column {
                        SectionHeading(
                            titulo = "Alunos com acompanhamento",
                            subtitulo = "Usuários que escolheram você como personal trainer"
                        )
                        if (alunos.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(SurfaceWhite)
                                    .border(1.dp, SoftBlueBorder, RoundedCornerShape(20.dp))
                                    .padding(20.dp)
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        imageVector = Icons.Filled.Groups,
                                        contentDescription = null,
                                        tint = BrandBlue,
                                        modifier = Modifier.size(32.dp)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Nenhum aluno escolheu você ainda",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Ink
                                    )
                                    Text(
                                        text = "Quando um aluno escolher você no catálogo de personais, ele aparecerá aqui para conversar.",
                                        fontSize = 11.sp,
                                        color = Muted,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }
                            }
                        } else {
                            alunos.forEachIndexed { index, aluno ->
                                RevealOnAppear(
                                    modifier = Modifier.fillMaxWidth(),
                                    delayMillis = 120 + index * 60,
                                    offsetY = 12.dp
                                ) {
                                    AlunoCard(
                                        aluno = aluno,
                                        ultimaMensagem = db.ultimaMensagemAluno(usuarioId, aluno.id)?.texto,
                                        onClick = {
                                            navController.navigate(Rotas.conversaAluno(aluno.id))
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
private fun HeroPainel(nome: String, fotoUri: String?, totalAlunos: Int) {
    val iniciais = nome.split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercase() }
        .ifBlank { "PT" }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .revealShadow(12.dp, RoundedCornerShape(26.dp), clip = false)
            .clip(RoundedCornerShape(26.dp))
            .background(Brush.linearGradient(listOf(Color(0xFF0CAC91), Color(0xFF0879C2))))
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            ProfilePhoto(
                fotoUri = fotoUri,
                iniciais = iniciais,
                tamanho = 46.dp,
                corInicial = Color(0xFF0A3F63),
                corFinal = Color(0xFF20B7E8)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "PERSONAL APROVADO",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp,
                    color = Color.White.copy(alpha = 0.82f)
                )
                Text(
                    text = nome,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Spacer(modifier = Modifier.height(14.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White.copy(alpha = 0.15f))
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Groups,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (totalAlunos == 1) "1 aluno acompanhado" else "$totalAlunos alunos acompanhados",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Toque em um aluno para iniciar uma conversa",
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.82f)
                )
            }
        }
    }
}

@Composable
private fun AlunoCard(
    aluno: AlunoPersonal,
    ultimaMensagem: String?,
    onClick: () -> Unit
) {
    val iniciais = aluno.nome.split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercase() }
        .ifBlank { "AL" }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 9.dp)
            .revealShadow(5.dp, RoundedCornerShape(18.dp), clip = false)
            .clip(RoundedCornerShape(18.dp))
            .background(SurfaceWhite)
            .border(1.dp, SoftBlueBorder.copy(alpha = 0.45f), RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .padding(13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProfilePhoto(
            fotoUri = aluno.fotoUri,
            iniciais = iniciais,
            tamanho = 46.dp,
            corInicial = BrandDark,
            corFinal = BrandBlue
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = aluno.nome,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Ink,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(MintBackground)
                        .padding(horizontal = 7.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = aluno.plano,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Mint
                    )
                }
            }
            Text(
                text = "${aluno.foco} · ${aluno.nivel}",
                fontSize = 11.sp,
                color = Muted,
                modifier = Modifier.padding(top = 2.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 7.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(SoftBlueBackground)
                    .padding(horizontal = 9.dp, vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Chat,
                    contentDescription = null,
                    tint = BrandBlue,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = ultimaMensagem ?: "Envie a primeira mensagem para este aluno",
                    fontSize = 10.sp,
                    color = if (ultimaMensagem != null) Ink else Muted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        Spacer(modifier = Modifier.width(6.dp))
        Icon(
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = null,
            tint = BrandBlue,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun AcessoRestritoPersonal(
    onVoltar: () -> Unit,
    onCandidatar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 28.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Brush.linearGradient(listOf(Color(0xFF3AA6EA), Color(0xFF0E7BB8))))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.18f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.PersonAdd,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(34.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "ÁREA DO PERSONAL",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.2.sp,
            color = Color.White.copy(alpha = 0.8f)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Candidatura necessária",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "O painel com os alunos é liberado apenas para personal trainers aprovados pela equipe FitUnity.",
            fontSize = 13.sp,
            color = Color.White.copy(alpha = 0.88f),
            lineHeight = 19.sp
        )
    }

    PrimaryActionButton(
        texto = "Enviar meu currículo",
        onClick = onCandidatar
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(SurfaceWhite)
            .border(1.dp, SoftBlueBorder, RoundedCornerShape(14.dp))
            .clickable { onVoltar() }
            .padding(vertical = 13.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Voltar",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = BrandDark
        )
    }
}
