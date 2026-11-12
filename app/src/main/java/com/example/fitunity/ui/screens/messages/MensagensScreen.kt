package com.example.fitunity.ui.screens.messages

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
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Verified
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fitunity.data.CandidaturaPersonal
import com.example.fitunity.data.FitUnityDbHelper
import com.example.fitunity.data.MensagemChat
import com.example.fitunity.data.RecursoPlano
import com.example.fitunity.data.SessionManager
import com.example.fitunity.data.possuiAcesso
import com.example.fitunity.ui.components.ProfilePhoto
import com.example.fitunity.ui.components.ConteudoBloqueadoPorPlano
import com.example.fitunity.ui.components.DetailHeader
import com.example.fitunity.ui.components.PrimaryActionButton
import com.example.fitunity.ui.components.SoftCard
import com.example.fitunity.ui.navigation.Rotas
import com.example.fitunity.ui.theme.BrandBlue
import com.example.fitunity.ui.theme.BrandDark
import com.example.fitunity.ui.theme.Ink
import com.example.fitunity.ui.theme.Mint
import com.example.fitunity.ui.theme.MintBackground
import com.example.fitunity.ui.theme.Muted
import com.example.fitunity.ui.theme.SoftBlueBorder
import com.example.fitunity.ui.theme.SurfaceWhite
import com.example.fitunity.ui.theme.Track

@Composable
fun MensagensScreen(navController: NavController) {
    if (!possuiAcesso(RecursoPlano.MENSAGENS_PERSONAL)) {
        ConteudoBloqueadoPorPlano(
            recurso = RecursoPlano.MENSAGENS_PERSONAL,
            onVoltar = { navController.popBackStack() },
            onVerPlanos = { navController.navigate(Rotas.PLANOS) }
        )
        return
    }

    val context = LocalContext.current
    val db = remember { FitUnityDbHelper(context.applicationContext) }
    val usuarioId = SessionManager.usuarioAtual.value?.id
    val personal = remember(usuarioId) {
        usuarioId?.let { db.buscarPersonalEscolhido(it) }
    }

    if (personal == null) {
        SemPersonalSelecionado(
            onVoltar = { navController.popBackStack() },
            onEscolher = { navController.navigate(Rotas.ESCOLHER_PERSONAL) }
        )
        return
    }

    val personalId = personal.usuarioId
    val mensagens = remember(usuarioId, personalId) {
        mutableStateListOf<MensagemChat>().apply {
            if (usuarioId != null) {
                addAll(db.listarMensagensDoAluno(usuarioId, personalId))
            }
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
            ChatHeader(
                personal = personal,
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
                if (mensagens.isEmpty()) {
                    item {
                        EmptyChatNote(nome = personal.nome.substringBefore(" "))
                    }
                }
                items(mensagens, key = { it.id }) { mensagem ->
                    MensagemBubble(mensagem = mensagem, personal = personal)
                }
            }

            ChatInput(
                texto = texto,
                onTextoChange = { texto = it },
                onEnviar = {
                    val id = usuarioId
                    if (id != null) {
                        db.enviarMensagemDoAluno(id, personalId, texto)?.let { mensagens.add(it) }
                        texto = ""
                    }
                }
            )
        }
    }
}

@Composable
private fun SemPersonalSelecionado(
    onVoltar: () -> Unit,
    onEscolher: () -> Unit
) {
    Scaffold(containerColor = Color(0xFFF8FBFD)) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 22.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            DetailHeader(
                titulo = "Mensagens",
                onBack = onVoltar
            )
            Spacer(modifier = Modifier.height(28.dp))
            SoftCard {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(58.dp)
                            .clip(CircleShape)
                            .background(BrandBlue.copy(alpha = 0.10f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Chat,
                            contentDescription = null,
                            tint = BrandBlue,
                            modifier = Modifier.size(27.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Escolha seu personal",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Ink
                    )
                    Text(
                        text = "Você precisa escolher um personal trainer antes de " +
                            "começar a conversar.",
                        fontSize = 13.sp,
                        color = Muted,
                        lineHeight = 19.sp,
                        modifier = Modifier.padding(top = 5.dp)
                    )
                    PrimaryActionButton(
                        texto = "Escolher personal",
                        onClick = onEscolher
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyChatNote(nome: String) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Envie uma mensagem para começar a conversa com $nome.",
            fontSize = 12.sp,
            color = Muted,
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(MintBackground)
                .padding(horizontal = 14.dp, vertical = 10.dp)
        )
    }
}

@Composable
private fun ChatHeader(
    personal: CandidaturaPersonal,
    onBack: () -> Unit
) {
    val iniciais = personal.iniciais()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 3.dp,
                ambientColor = Ink.copy(alpha = 0.08f),
                spotColor = Ink.copy(alpha = 0.12f)
            )
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
                fotoUri = personal.fotoUri,
                iniciais = iniciais,
                tamanho = 46.dp,
                corInicial = BrandDark,
                corFinal = BrandBlue
            )
            Spacer(modifier = Modifier.width(11.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = personal.nome,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Ink
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Filled.Verified,
                        contentDescription = "Personal aprovado",
                        tint = BrandBlue,
                        modifier = Modifier.size(14.dp)
                    )
                }
                Text(
                    text = "Personal trainer · CREF ${personal.cref}",
                    fontSize = 11.sp,
                    color = Muted
                )
            }
        }
        HorizontalDivider(thickness = 1.dp, color = SoftBlueBorder.copy(alpha = 0.6f))
    }
}

@Composable
private fun MensagemBubble(mensagem: MensagemChat, personal: CandidaturaPersonal) {
    val minha = mensagem.enviadaPorMim
    val iniciais = personal.iniciais()
    val forma = if (minha) {
        RoundedCornerShape(18.dp, 18.dp, 4.dp, 18.dp)
    } else {
        RoundedCornerShape(18.dp, 18.dp, 18.dp, 4.dp)
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (minha) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        if (!minha) {
            ProfilePhoto(
                fotoUri = personal.fotoUri,
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
private fun ChatInput(
    texto: String,
    onTextoChange: (String) -> Unit,
    onEnviar: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 6.dp,
                ambientColor = Ink.copy(alpha = 0.05f),
                spotColor = Ink.copy(alpha = 0.10f)
            )
            .background(SurfaceWhite)
            .imePadding()
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = texto,
            onValueChange = onTextoChange,
            placeholder = {
                Text(text = "Escreva uma mensagem...", fontSize = 13.sp, color = Muted)
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

private fun CandidaturaPersonal.iniciais(): String = nome.split(" ")
    .filter { it.isNotBlank() }
    .take(2)
    .joinToString("") { it.first().uppercase() }
