package com.example.fitunity.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fitunity.data.AlunoPersonal
import com.example.fitunity.data.FitUnityDbHelper
import com.example.fitunity.data.MensagemChat
import com.example.fitunity.data.SessionManager
import com.example.fitunity.data.StatusCandidatura
import com.example.fitunity.ui.components.ProfilePhoto
import com.example.fitunity.ui.components.PrimaryActionButton
import com.example.fitunity.ui.navigation.Rotas
import com.example.fitunity.ui.theme.BrandBlue
import com.example.fitunity.ui.theme.BrandDark
import com.example.fitunity.ui.theme.Ink
import com.example.fitunity.ui.theme.Mint
import com.example.fitunity.ui.theme.Muted
import com.example.fitunity.ui.theme.SoftBlueBorder
import com.example.fitunity.ui.theme.SurfaceWhite
import com.example.fitunity.ui.theme.Track

@Composable
fun ConversaAlunoScreen(navController: NavController, alunoId: Long) {
    val context = LocalContext.current
    val db = remember { FitUnityDbHelper(context.applicationContext) }
    val perfil = SessionManager.usuarioAtual.value
    val personalId = perfil?.id ?: 0L
    val candidatura = remember(personalId) { db.buscarCandidatura(personalId) }
    val aprovado = candidatura?.status == StatusCandidatura.APROVADO
    val aluno = remember(personalId, alunoId) { db.buscarAlunoElegivel(personalId, alunoId) }

    if (!aprovado || aluno == null) {
        AcessoConversaRestrito(
            onVoltar = { navController.popBackStack() },
            onCandidatar = { navController.navigate(Rotas.TRABALHE_CONOSCO) }
        )
        return
    }

    val mensagens = remember(personalId, alunoId) {
        mutableStateListOf<MensagemChat>().apply {
            addAll(db.listarMensagensAluno(personalId, alunoId))
        }
    }
    val listaState = rememberLazyListState()
    var texto by remember { mutableStateOf("") }

    LaunchedEffect(mensagens.size) {
        if (mensagens.isNotEmpty()) {
            listaState.animateScrollToItem(mensagens.lastIndex)
        }
    }

    Scaffold(containerColor = Color(0xFFF8FBFD)) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            ChatAlunoHeader(
                aluno = aluno,
                onBack = { navController.popBackStack() }
            )

            LazyColumn(
                state = listaState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFFF1F8FC), Color(0xFFF8FBFD))
                        )
                    ),
                contentPadding = PaddingValues(horizontal = 18.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (mensagens.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .size(58.dp)
                                        .clip(CircleShape)
                                        .background(SurfaceWhite),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.PersonAdd,
                                        contentDescription = null,
                                        tint = BrandBlue,
                                        modifier = Modifier.size(26.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "Comece a conversa com ${aluno.nome.split(" ").first()}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Ink,
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "Envie uma mensagem de boas-vindas e se apresente como personal trainer.",
                                    fontSize = 11.sp,
                                    color = Muted,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 16.sp,
                                    modifier = Modifier.padding(top = 4.dp, start = 18.dp, end = 18.dp)
                                )
                            }
                        }
                    }
                } else {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Hoje",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Muted,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(SurfaceWhite)
                                    .border(
                                        width = 1.dp,
                                        color = SoftBlueBorder.copy(alpha = 0.7f),
                                        shape = RoundedCornerShape(20.dp)
                                    )
                                    .padding(horizontal = 14.dp, vertical = 5.dp)
                            )
                        }
                    }
                    items(mensagens, key = { it.id }) { mensagem ->
                        MensagemAlunoBubble(
                            mensagem = mensagem,
                            aluno = aluno
                        )
                    }
                }
            }

            ChatAlunoInput(
                texto = texto,
                onTextoChange = { texto = it },
                onEnviar = {
                    if (personalId != 0L) {
                        db.enviarMensagemAluno(personalId, alunoId, texto)?.let { mensagens.add(it) }
                        texto = ""
                    }
                }
            )
        }
    }
}

@Composable
private fun ChatAlunoHeader(
    aluno: AlunoPersonal,
    onBack: () -> Unit
) {
    val iniciais = aluno.nome.split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercase() }
        .ifBlank { "AL" }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 3.dp, ambientColor = Ink.copy(alpha = 0.08f), spotColor = Ink.copy(alpha = 0.12f))
            .background(SurfaceWhite)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(11.dp))
                    .background(Track)
                    .clickable { onBack() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Voltar",
                    tint = Ink,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            ProfilePhoto(
                fotoUri = aluno.fotoUri,
                iniciais = iniciais,
                tamanho = 44.dp,
                corInicial = BrandDark,
                corFinal = BrandBlue
            )
            Spacer(modifier = Modifier.width(11.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = aluno.nome,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Ink
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(Mint)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "${aluno.plano} · ${aluno.foco}",
                        fontSize = 11.sp,
                        color = Muted
                    )
                }
            }
        }
        HorizontalDivider(thickness = 1.dp, color = SoftBlueBorder.copy(alpha = 0.6f))
    }
}

@Composable
private fun MensagemAlunoBubble(
    mensagem: MensagemChat,
    aluno: AlunoPersonal
) {
    val minha = mensagem.enviadaPorMim
    val forma = if (minha) {
        RoundedCornerShape(18.dp, 18.dp, 4.dp, 18.dp)
    } else {
        RoundedCornerShape(18.dp, 18.dp, 18.dp, 4.dp)
    }
    val iniciais = aluno.nome.split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercase() }
        .ifBlank { "AL" }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (minha) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        if (!minha) {
            ProfilePhoto(
                fotoUri = aluno.fotoUri,
                iniciais = iniciais,
                tamanho = 28.dp,
                corInicial = BrandDark,
                corFinal = BrandBlue
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Column(
            horizontalAlignment = if (minha) Alignment.End else Alignment.Start,
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Box(
                modifier = Modifier
                    .shadow(
                        elevation = if (minha) 0.dp else 1.dp,
                        shape = forma,
                        clip = false,
                        ambientColor = Ink.copy(alpha = 0.06f),
                        spotColor = Ink.copy(alpha = 0.10f)
                    )
                    .clip(forma)
                    .background(if (minha) BrandBlue else SurfaceWhite)
                    .border(
                        width = if (minha) 0.dp else 1.dp,
                        color = if (minha) Color.Transparent else SoftBlueBorder.copy(alpha = 0.7f),
                        shape = forma
                    )
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Text(
                    text = mensagem.texto,
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                    color = if (minha) Color.White else Ink
                )
            }
            Text(
                text = mensagem.horario,
                fontSize = 10.sp,
                color = Muted,
                modifier = Modifier.padding(top = 3.dp, start = 3.dp, end = 3.dp)
            )
        }
    }
}

@Composable
private fun ChatAlunoInput(
    texto: String,
    onTextoChange: (String) -> Unit,
    onEnviar: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 6.dp, ambientColor = Ink.copy(alpha = 0.05f), spotColor = Ink.copy(alpha = 0.10f))
            .background(SurfaceWhite)
            .imePadding()
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = texto,
            onValueChange = onTextoChange,
            placeholder = {
                Text(text = "Escreva para seu aluno...", fontSize = 13.sp, color = Muted)
            },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(22.dp),
            maxLines = 4,
            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 13.sp, color = Ink),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BrandBlue,
                unfocusedBorderColor = SoftBlueBorder,
                focusedContainerColor = Color(0xFFF8FBFD),
                unfocusedContainerColor = Color(0xFFF8FBFD),
                cursorColor = BrandBlue
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(
            onClick = onEnviar,
            enabled = texto.isNotBlank(),
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(if (texto.isNotBlank()) BrandBlue else Track)
        ) {
            Icon(
                imageVector = Icons.Filled.Send,
                contentDescription = "Enviar mensagem",
                tint = if (texto.isNotBlank()) Color.White else Muted,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun AcessoConversaRestrito(
    onVoltar: () -> Unit,
    onCandidatar: () -> Unit
) {
    Scaffold(containerColor = Color(0xFFF8FBFD)) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 22.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(11.dp))
                        .background(Track)
                        .clickable { onVoltar() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Voltar",
                        tint = Ink,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Conversa com aluno",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Ink
                )
            }
            Spacer(modifier = Modifier.height(28.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
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
                    text = "CONVERSA EXCLUSIVA",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Você precisa ser um personal aprovado",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Envie seu currículo e aguarde a análise da equipe para conversar com alunos.",
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.88f),
                    textAlign = TextAlign.Center,
                    lineHeight = 19.sp
                )
            }
            PrimaryActionButton(
                texto = "Ir para candidatura",
                onClick = onCandidatar
            )
        }
    }
}
