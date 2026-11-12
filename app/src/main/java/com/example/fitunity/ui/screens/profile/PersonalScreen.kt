package com.example.fitunity.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fitunity.data.CandidaturaPersonal
import com.example.fitunity.data.FitUnityDbHelper
import com.example.fitunity.data.RecursoPlano
import com.example.fitunity.data.SessionManager
import com.example.fitunity.data.possuiAcesso
import com.example.fitunity.ui.components.ConteudoBloqueadoPorPlano
import com.example.fitunity.ui.components.DetailHeader
import com.example.fitunity.ui.components.InfoPill
import com.example.fitunity.ui.components.PrimaryActionButton
import com.example.fitunity.ui.components.ProfilePhoto
import com.example.fitunity.ui.components.SectionHeading
import com.example.fitunity.ui.components.SoftCard
import com.example.fitunity.ui.components.StatBlock
import com.example.fitunity.ui.components.TagChip
import com.example.fitunity.ui.components.revealShadow
import com.example.fitunity.ui.navigation.Rotas
import com.example.fitunity.ui.theme.BrandBlue
import com.example.fitunity.ui.theme.BrandDark
import com.example.fitunity.ui.theme.Ink
import com.example.fitunity.ui.theme.Mint
import com.example.fitunity.ui.theme.Muted
import com.example.fitunity.ui.theme.SoftBlueBackground
import com.example.fitunity.ui.theme.SoftBlueBorder
import com.example.fitunity.ui.theme.SurfaceWhite

@Composable
fun PersonalScreen(navController: NavController) {
    if (!possuiAcesso(RecursoPlano.PERSONAL_TRAINER)) {
        ConteudoBloqueadoPorPlano(
            recurso = RecursoPlano.PERSONAL_TRAINER,
            onVoltar = { navController.popBackStack() },
            onVerPlanos = { navController.navigate(Rotas.PLANOS) }
        )
        return
    }

    val context = LocalContext.current
    val db = remember { FitUnityDbHelper(context.applicationContext) }
    val perfil = SessionManager.usuarioAtual.value
    val usuarioId = perfil?.id
    val personal = usuarioId?.let { db.buscarPersonalEscolhido(it) }
    val alunos = personal?.let { db.contarAlunosDoPersonal(it.usuarioId) } ?: 0

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
                titulo = "Meu personal",
                onBack = { navController.popBackStack() },
                trailing = {
                    if (personal != null) {
                        TextButton(
                            onClick = { navController.navigate(Rotas.ESCOLHER_PERSONAL) }
                        ) {
                            Text(
                                text = "Trocar",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = BrandBlue,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (personal == null) {
                SemPersonalEscolhido(
                    onEscolher = { navController.navigate(Rotas.ESCOLHER_PERSONAL) }
                )
            } else {
                HeroPersonal(personal)

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .revealShadow(8.dp, RoundedCornerShape(20.dp), clip = false)
                        .clip(RoundedCornerShape(20.dp))
                        .background(SurfaceWhite)
                        .border(1.dp, SoftBlueBorder.copy(alpha = 0.45f), RoundedCornerShape(20.dp))
                        .padding(vertical = 18.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatBlock(
                        valor = personal.experiencia,
                        rotulo = "Experiência",
                        corValor = BrandBlue
                    )
                    StatDivider()
                    StatBlock(
                        valor = "$alunos",
                        rotulo = "Alunos",
                        corValor = BrandDark
                    )
                    StatDivider()
                    StatBlock(
                        valor = "${personal.especialidades.size}",
                        rotulo = "Especialidades",
                        corValor = Mint
                    )
                }

                SectionHeading(
                    titulo = "Especialidades",
                    subtitulo = "Áreas de atuação"
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                    personal.especialidades.forEach { especialidade ->
                        TagChip(texto = especialidade)
                    }
                }

                val primeiroNome = personal.nome.substringBefore(" ")

                SectionHeading(
                    titulo = "Sobre",
                    subtitulo = "Como $primeiroNome acompanha você"
                )
                SoftCard {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(RoundedCornerShape(9.dp))
                                    .background(SoftBlueBackground),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.CheckCircle,
                                    contentDescription = null,
                                    tint = BrandBlue,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Acompanhamento próximo e personalizado",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Ink
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = personal.apresentacao,
                            fontSize = 13.sp,
                            color = Muted,
                            lineHeight = 19.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(7.dp)
                        ) {
                            InfoPill(texto = "CREF validado")
                            InfoPill(texto = "Suporte no chat")
                            InfoPill(texto = "Plano Adaptado+")
                        }
                    }
                }

                SectionHeading(
                    titulo = "Contato",
                    subtitulo = "Fale com $primeiroNome"
                )
                SoftCard {
                    Column(modifier = Modifier.padding(16.dp)) {
                        ContatoLinha(
                            icone = Icons.Filled.Email,
                            rotulo = "E-mail",
                            valor = personal.email
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 11.dp)
                                .height(1.dp)
                                .background(SoftBlueBorder.copy(alpha = 0.5f))
                        )
                        ContatoLinha(
                            icone = Icons.Filled.Phone,
                            rotulo = "Telefone",
                            valor = personal.telefone
                        )
                    }
                }

                SectionHeading(
                    titulo = "Acompanhamento",
                    subtitulo = "O que está incluso"
                )
                SoftCard {
                    Column(modifier = Modifier.padding(16.dp)) {
                        AcompanhamentoLinha(
                            titulo = "Avaliação inicial",
                            descricao = "Objetivo, rotina e limitações alinhados pelo chat"
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        AcompanhamentoLinha(
                            titulo = "Ajustes de treino",
                            descricao = "Orientação para evoluir com segurança"
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        AcompanhamentoLinha(
                            titulo = "Suporte no chat",
                            descricao = "Tire dúvidas durante todo o seu plano"
                        )
                    }
                }

                PrimaryActionButton(
                    texto = "Enviar mensagem",
                    onClick = { navController.navigate(Rotas.MENSAGENS) }
                )

                TextButton(
                    onClick = { navController.navigate(Rotas.ESCOLHER_PERSONAL) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp)
                ) {
                    Text(
                        text = "Trocar personal",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrandBlue
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
private fun SemPersonalEscolhido(onEscolher: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp)
                .revealShadow(12.dp, RoundedCornerShape(26.dp), clip = false)
                .clip(RoundedCornerShape(26.dp))
                .background(
                    Brush.linearGradient(
                        listOf(Color(0xFF0E7BB8), Color(0xFF0A4F75))
                    )
                )
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(150.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.06f))
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(20.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White.copy(alpha = 0.17f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Groups,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(27.dp)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Escolha seu personal",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Compare os profissionais aprovados e encontre o que combina com você.",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.82f),
                    lineHeight = 18.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        SectionHeading(
            titulo = "Como escolher",
            subtitulo = "Você tem o controle"
        )
        SoftCard {
            Column(modifier = Modifier.padding(16.dp)) {
                AcompanhamentoLinha(
                    titulo = "Veja o perfil completo",
                    descricao = "Especialidades, experiência, CREF e apresentação"
                )
                Spacer(modifier = Modifier.height(12.dp))
                AcompanhamentoLinha(
                    titulo = "Compare com seu objetivo",
                    descricao = "Destacamos quem combina com o seu foco atual"
                )
                Spacer(modifier = Modifier.height(12.dp))
                AcompanhamentoLinha(
                    titulo = "Troque quando quiser",
                    descricao = "Sua escolha pode ser alterada a qualquer momento"
                )
            }
        }

        PrimaryActionButton(
            texto = "Escolher personal",
            onClick = onEscolher
        )
    }
}

@Composable
private fun HeroPersonal(personal: CandidaturaPersonal) {
    val iniciais = personal.nome.split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercase() }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(230.dp)
            .revealShadow(14.dp, RoundedCornerShape(26.dp), clip = false)
            .clip(RoundedCornerShape(26.dp))
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFF0E7BB8), Color(0xFF0A3F63))
                )
            )
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(170.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.06f))
        )
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White.copy(alpha = 0.16f))
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.24f),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(horizontal = 11.dp, vertical = 6.dp)
        ) {
            Text(
                text = "PERSONAL TRAINER",
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp,
                color = Color.White
            )
        }
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.18f))
                    .border(
                        width = 1.5.dp,
                        color = Color.White.copy(alpha = 0.55f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                ProfilePhoto(
                    fotoUri = personal.fotoUri,
                    iniciais = iniciais.ifBlank { "PT" },
                    tamanho = 58.dp,
                    corInicial = Color(0xFF0A3F63),
                    corFinal = Color(0xFF20B7E8)
                )
            }
            Spacer(modifier = Modifier.width(13.dp))
            Column {
                Text(
                    text = personal.nome,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 3.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Verified,
                        contentDescription = "Personal aprovado",
                        tint = Color(0xFF7FD4F5),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Aprovado pela equipe FitUnity",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
                Text(
                    text = "CREF ${personal.cref} · ${personal.experiencia} de experiência",
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.74f),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun ContatoLinha(
    icone: androidx.compose.ui.graphics.vector.ImageVector,
    rotulo: String,
    valor: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(11.dp))
                .background(SoftBlueBackground),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icone,
                contentDescription = null,
                tint = BrandBlue,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.width(11.dp))
        Column {
            Text(
                text = rotulo,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = Muted
            )
            Text(
                text = valor,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Ink,
                modifier = Modifier.padding(top = 1.dp)
            )
        }
    }
}

@Composable
private fun AcompanhamentoLinha(titulo: String, descricao: String) {
    Row(verticalAlignment = Alignment.Top) {
        Icon(
            imageVector = Icons.Filled.CheckCircle,
            contentDescription = null,
            tint = Mint,
            modifier = Modifier
                .padding(top = 1.dp)
                .size(16.dp)
        )
        Spacer(modifier = Modifier.width(9.dp))
        Column {
            Text(
                text = titulo,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Ink
            )
            Text(
                text = descricao,
                fontSize = 11.sp,
                color = Muted,
                lineHeight = 16.sp,
                modifier = Modifier.padding(top = 1.dp)
            )
        }
    }
}

@Composable
private fun StatDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(26.dp)
            .background(SoftBlueBorder)
    )
}
