package com.example.fitunity.ui.screens.messages

import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fitunity.R
import com.example.fitunity.data.FitUnityDbHelper
import com.example.fitunity.data.MensagemChat
import com.example.fitunity.data.SessionManager
import com.example.fitunity.ui.theme.BrandBlue
import com.example.fitunity.ui.theme.Ink
import com.example.fitunity.ui.theme.Mint
import com.example.fitunity.ui.theme.Muted
import com.example.fitunity.ui.theme.SoftBlueBorder
import com.example.fitunity.ui.theme.SurfaceWhite
import com.example.fitunity.ui.theme.Track

@Composable
fun MensagensScreen(navController: NavController) {
    val context = LocalContext.current
    val db = remember { FitUnityDbHelper(context.applicationContext) }
    val usuarioId = SessionManager.usuarioAtual.value?.id
    val mensagens = remember(usuarioId) {
        mutableStateListOf<MensagemChat>().apply {
            if (usuarioId != null) addAll(db.listarMensagens(usuarioId))
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
            ChatHeader(onBack = { navController.popBackStack() })

            LazyColumn(
                state = listaState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
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
                                .background(Track)
                                .padding(horizontal = 12.dp, vertical = 5.dp)
                        )
                    }
                }
                items(mensagens, key = { it.id }) { mensagem ->
                    MensagemBubble(mensagem)
                }
            }

            ChatInput(
                texto = texto,
                onTextoChange = { texto = it },
                onEnviar = {
                    val id = usuarioId
                    if (id != null) {
                        db.enviarMensagem(id, texto)?.let { mensagens.add(it) }
                        texto = ""
                    }
                }
            )
        }
    }
}

@Composable
private fun ChatHeader(onBack: () -> Unit) {
    Column(modifier = Modifier.background(SurfaceWhite)) {
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
            Image(
                painter = painterResource(id = R.drawable.treino_personal),
                contentDescription = null,
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Rafael Martins",
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
                    Text(text = "Personal trainer · Online", fontSize = 11.sp, color = Muted)
                }
            }
        }
        HorizontalDivider(thickness = 1.dp, color = SoftBlueBorder.copy(alpha = 0.6f))
    }
}

@Composable
private fun MensagemBubble(mensagem: MensagemChat) {
    val minha = mensagem.enviadaPorMim
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
            Image(
                painter = painterResource(id = R.drawable.treino_personal),
                contentDescription = null,
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Column(
            horizontalAlignment = if (minha) Alignment.End else Alignment.Start,
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Box(
                modifier = Modifier
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
