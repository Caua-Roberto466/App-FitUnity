package com.example.fitunity.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Verified
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fitunity.R
import com.example.fitunity.data.CandidaturaPersonal
import com.example.fitunity.data.FitUnityDbHelper
import com.example.fitunity.data.FotoPerfil
import com.example.fitunity.data.PlanoNivel
import com.example.fitunity.data.RecursoPlano
import com.example.fitunity.data.SessionManager
import com.example.fitunity.data.diasSemana
import com.example.fitunity.data.nivelDoPlano
import com.example.fitunity.data.possuiAcesso
import com.example.fitunity.ui.components.Avatar
import com.example.fitunity.ui.components.ProfilePhoto
import com.example.fitunity.ui.components.FitUnityBottomBar
import com.example.fitunity.ui.components.PulsingDot
import com.example.fitunity.ui.components.RevealOnAppear
import com.example.fitunity.ui.components.SectionHeading
import com.example.fitunity.ui.components.revealShadow
import com.example.fitunity.ui.components.rememberAnimatedDp
import com.example.fitunity.ui.navigation.Rotas
import com.example.fitunity.ui.theme.BrandBlue
import com.example.fitunity.ui.theme.BrandDark
import com.example.fitunity.ui.theme.Coral
import com.example.fitunity.ui.theme.CoralBackground
import com.example.fitunity.ui.theme.Ink
import com.example.fitunity.ui.theme.Mint
import com.example.fitunity.ui.theme.MintBackground
import com.example.fitunity.ui.theme.Muted
import com.example.fitunity.ui.theme.SoftBlue
import com.example.fitunity.ui.theme.SoftBlueBackground
import com.example.fitunity.ui.theme.SoftBlueBorder
import com.example.fitunity.ui.theme.SurfaceWhite
import com.example.fitunity.ui.theme.Track

@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    val db = remember { FitUnityDbHelper(context) }
    val perfil = SessionManager.usuarioAtual.value
    val fotoPerfil = remember(perfil?.fotoUri) { FotoPerfil.carregar(perfil?.fotoUri) }
    val usuarioId = perfil?.id ?: 0L
    val treinos = remember(usuarioId) { db.listarTreinos(usuarioId) }
    val refeicoes = remember(usuarioId) { db.listarRefeicoes(usuarioId) }
    val historico = remember(usuarioId) { db.listarHistorico(usuarioId) }
    val notificacoes = remember(usuarioId) { db.listarNotificacoes(usuarioId) }
    val hojeIndex = (java.time.LocalDate.now().dayOfWeek.value - 1).coerceIn(0, 6)
    val treinoHoje = treinos.firstOrNull {
        it.diaSemana.equals(diasSemana[hojeIndex], ignoreCase = true)
    } ?: treinos.firstOrNull()
    val caloriasConsumidas = refeicoes.filter { it.concluida }.sumOf { refeicao ->
        refeicao.calorias.filter { it.isDigit() }.toIntOrNull() ?: 0
    }
    val ultimoTreino = historico.firstOrNull()
    val temNaoLidas = notificacoes.any { !it.lida }
    val acessoPersonal = possuiAcesso(RecursoPlano.PERSONAL_TRAINER)
    val personalEscolhido = remember(usuarioId) {
        if (usuarioId != 0L) db.buscarPersonalEscolhido(usuarioId) else null
    }
    val mensagensPersonal = remember(usuarioId, personalEscolhido?.usuarioId) {
        val personalId = personalEscolhido?.usuarioId
        if (usuarioId != 0L && personalId != null) {
            db.listarMensagensDoAluno(usuarioId, personalId)
        } else {
            emptyList()
        }
    }

    val nome = perfil?.nome?.takeIf { it.isNotBlank() } ?: "Usuário"
    val primeiroNome = nome.substringBefore(" ")
    val iniciais = nome.split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercase() }

    val treinosConcluidos = perfil?.treinosRealizados ?: 4
    val metaSemanal = (perfil?.let { it.treinosRealizados + it.treinosPendentes } ?: 5)
        .coerceAtLeast(5)
    val percentual = ((treinosConcluidos.toFloat() / metaSemanal) * 100).toInt().coerceIn(0, 100)

    Scaffold(
        bottomBar = { FitUnityBottomBar(navController) },
        containerColor = Color(0xFFF8FBFD)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 22.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            RevealOnAppear(
                modifier = Modifier.fillMaxWidth(),
                delayMillis = 0,
                offsetY = 14.dp
            ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(45.dp)
                        .clip(CircleShape)
                        .clickable { navController.navigate(Rotas.PERFIL) }
                        .background(Brush.linearGradient(listOf(Color(0xFF142F4A), BrandBlue))),
                    contentAlignment = Alignment.Center
                ) {
                    if (fotoPerfil != null) {
                        Image(
                            bitmap = fotoPerfil.asImageBitmap(),
                            contentDescription = "Foto de perfil",
                            modifier = Modifier
                                .size(45.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Text(
                            text = iniciais.ifBlank { "FU" },
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Olá, $primeiroNome!",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Ink
                    )
                    Text(text = "Vamos evoluir hoje?", fontSize = 13.sp, color = Muted)
                }
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .revealShadow(4.dp, RoundedCornerShape(14.dp), clip = false)
                        .clip(RoundedCornerShape(14.dp))
                        .background(SurfaceWhite)
                        .clickable { navController.navigate(Rotas.NOTIFICACOES) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Notifications,
                        contentDescription = "Notificações",
                        tint = Ink,
                        modifier = Modifier.size(20.dp)
                    )
                    if (temNaoLidas) {
                        PulsingDot(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(top = 7.dp, end = 8.dp)
                                .size(7.dp)
                        )
                    }
                }
            }
            }

            Spacer(modifier = Modifier.height(22.dp))

            RevealOnAppear(
                modifier = Modifier.fillMaxWidth(),
                delayMillis = 80,
                offsetY = 20.dp
            ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(216.dp)
                    .revealShadow(12.dp, RoundedCornerShape(24.dp), clip = false)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFF047FC7))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.treino_hoje),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer { scaleX = -1f },
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                0f to Color(0xFF047FC7),
                                0.38f to Color(0xFF047FC7),
                                0.68f to Color(0xFF047FC7).copy(alpha = 0.72f),
                                0.88f to Color(0xFF047FC7).copy(alpha = 0.28f),
                                1f to Color.Transparent
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .padding(start = 24.dp, top = 26.dp, end = 12.dp)
                ) {
                    Text(
                        text = "TREINO DE HOJE",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.8f),
                        letterSpacing = 1.2.sp
                    )
                    Spacer(modifier = Modifier.height(7.dp))
                    Text(
                        text = treinoHoje?.titulo ?: "Membros superiores",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        lineHeight = 28.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Timer,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(text = treinoHoje?.duracao ?: "45 min", fontSize = 13.sp, color = Color.White)
                        Spacer(modifier = Modifier.width(7.dp))
                        Box(
                            modifier = Modifier
                                .size(3.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                        )
                        Spacer(modifier = Modifier.width(7.dp))
                        Text(text = treinoHoje?.descricao ?: "Academia", fontSize = 13.sp, color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(18.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(SurfaceWhite)
                            .clickable {
                                treinoHoje?.let { navController.navigate(Rotas.treinoDetalhe(it.id)) }
                                    ?: navController.navigate(Rotas.TREINOS)
                            }
                            .padding(horizontal = 22.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = "Ver treino",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = BrandDark
                        )
                    }
                }
            }
            }

            RevealOnAppear(
                modifier = Modifier.fillMaxWidth(),
                delayMillis = 160,
                offsetY = 18.dp
            ) {
            Column {
            SectionHeading(
                titulo = "Seu progresso",
                subtitulo = "Meta semanal",
                acao = "$treinosConcluidos de $metaSemanal dias"
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .revealShadow(8.dp, RoundedCornerShape(20.dp), clip = false)
                    .clip(RoundedCornerShape(20.dp))
                    .background(SurfaceWhite)
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.width(88.dp)) {
                    Text(
                        text = "$percentual%",
                        fontSize = 27.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrandBlue
                    )
                    Text(
                        text = if (percentual >= 70) "Excelente ritmo!" else "Bom começo!",
                        fontSize = 12.sp,
                        color = Muted
                    )
                }
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    listOf("S", "T", "Q", "Q", "S", "S", "D").forEachIndexed { index, dia ->
                        val feito = index < treinosConcluidos.coerceAtMost(4)
                        val hoje = index == treinosConcluidos.coerceAtMost(4)
                        val alturaBarra = rememberAnimatedDp(
                            target = if (hoje) 31.dp else 43.dp,
                            delayMillis = index * 60
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .width(10.dp)
                                    .height(alturaBarra)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            feito -> BrandBlue
                                            hoje -> Color(0xFFBCE8FF)
                                            else -> Track
                                        }
                                    )
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(text = dia, fontSize = 11.sp, color = Muted)
                        }
                    }
                }
            }
            }
            }

            RevealOnAppear(
                modifier = Modifier.fillMaxWidth(),
                delayMillis = 240,
                offsetY = 18.dp
            ) {
            Column {
            SectionHeading(
                titulo = "Resumo de hoje",
                acao = "Ver detalhes",
                onAcao = { navController.navigate(Rotas.PROGRESSO) }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(9.dp)
            ) {
                MetricCard(
                    modifier = Modifier.weight(1f),
                    icone = Icons.Filled.DirectionsWalk,
                    cor = BrandBlue,
                    fundo = SoftBlue,
                    valor = "6.240",
                    rotulo = "Passos"
                )
                MetricCard(
                    modifier = Modifier.weight(1f),
                    icone = Icons.Filled.LocalFireDepartment,
                    cor = Coral,
                    fundo = CoralBackground,
                    valor = "$caloriasConsumidas",
                    rotulo = "Calorias"
                )
                MetricCard(
                    modifier = Modifier.weight(1f),
                    icone = Icons.Filled.Timer,
                    cor = Mint,
                    fundo = MintBackground,
                    valor = ultimoTreino?.duracao ?: "0 min",
                    rotulo = "Atividade"
                )
            }
            }
            }

            if (acessoPersonal) {
                RevealOnAppear(
                    modifier = Modifier.fillMaxWidth(),
                    delayMillis = 300,
                    offsetY = 18.dp
                ) {
                    Column {
                        SectionHeading(
                            titulo = "Seu personal",
                            subtitulo = "Acompanhamento do seu plano",
                            acao = "Ver tudo",
                            onAcao = { navController.navigate(Rotas.PERSONAL) }
                        )
                        PersonalResumidoCard(
                            personal = personalEscolhido,
                            ultimaMensagem = mensagensPersonal.lastOrNull()?.texto,
                            horario = mensagensPersonal.lastOrNull()?.horario,
                            onAbrirChat = {
                                navController.navigate(
                                    if (personalEscolhido == null) {
                                        Rotas.ESCOLHER_PERSONAL
                                    } else {
                                        Rotas.MENSAGENS
                                    }
                                )
                            }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        DicaPersonalCard(
                            onClick = { navController.navigate(Rotas.PERSONAL) }
                        )
                    }
                }
            }

            RevealOnAppear(
                modifier = Modifier.fillMaxWidth(),
                delayMillis = 320,
                offsetY = 18.dp
            ) {
                Column {
                    SectionHeading(
                        titulo = "Desbloqueie mais",
                        subtitulo = "Planos FitUnity",
                        acao = "Ver planos",
                        onAcao = { navController.navigate(Rotas.PLANOS) }
                    )
                    PlanoPromoCard(
                        nivel = nivelDoPlano(perfil?.tipoPlano),
                        onVerPlanos = { navController.navigate(Rotas.PLANOS) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
private fun PlanoPromoCard(
    nivel: PlanoNivel,
    onVerPlanos: () -> Unit
) {
    val profissional = nivel == PlanoNivel.PROFISSIONAL
    val titulo = when (nivel) {
        PlanoNivel.GRATUITO -> "Tenha um treino feito para você"
        PlanoNivel.ADAPTADO -> "Dê o próximo passo: Profissional"
        PlanoNivel.PROFISSIONAL -> "Seu plano Profissional está ativo"
    }
    val descricao = when (nivel) {
        PlanoNivel.GRATUITO ->
            "Personal dedicado, chat com o profissional e descontos exclusivos em sessões."
        PlanoNivel.ADAPTADO ->
            "Some consulta nutricional, avaliação física e relatórios revisados por um profissional."
        PlanoNivel.PROFISSIONAL ->
            "Aproveite o acompanhamento completo, descontos e relatórios exclusivos do seu plano."
    }
    val preco = when (nivel) {
        PlanoNivel.GRATUITO -> "A partir de R$29,90/mês"
        PlanoNivel.ADAPTADO -> "Profissional por R$59,90/mês"
        PlanoNivel.PROFISSIONAL -> "Plano ativo"
    }
    val beneficios = when (nivel) {
        PlanoNivel.GRATUITO -> listOf(
            "Personal trainer dedicado",
            "10% de desconto em sessões",
            "Conteúdo exclusivo de treino"
        )
        PlanoNivel.ADAPTADO -> listOf(
            "1 consulta nutricional por mês",
            "Avaliação física mensal",
            "Relatório com profissional"
        )
        PlanoNivel.PROFISSIONAL -> listOf(
            "Acompanhamento completo",
            "20% de desconto em sessões",
            "Relatório mensal exclusivo"
        )
    }
    val textoBotao = if (profissional) "Gerenciar plano" else "Conhecer planos"

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .revealShadow(12.dp, RoundedCornerShape(24.dp), clip = false)
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.linearGradient(listOf(Color(0xFF3AA6EA), Color(0xFF0E7BB8)))
            )
            .clickable { onVerPlanos() }
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 34.dp, y = (-46).dp)
                .size(140.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.06f))
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = (-44).dp, y = 52.dp)
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.05f))
        )
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White.copy(alpha = 0.18f))
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = if (profissional) "PROFISSIONAL" else "PREMIUM",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            color = Color.White
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = preco,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White.copy(alpha = 0.78f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = titulo,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                lineHeight = 25.sp
            )
            Text(
                text = descricao,
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.8f),
                lineHeight = 17.sp,
                modifier = Modifier.padding(top = 6.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            beneficios.forEach { beneficio ->
                Row(
                    modifier = Modifier.padding(top = 7.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null,
                        tint = Color(0xFFB8F0FF),
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = beneficio,
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White)
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = textoBotao,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandDark
                )
                Spacer(modifier = Modifier.width(5.dp))
                Icon(
                    imageVector = Icons.Filled.ChevronRight,
                    contentDescription = null,
                    tint = BrandDark,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun DicaPersonalCard(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(SoftBlueBackground)
            .clickable { onClick() }
            .padding(15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(31.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(SoftBlue),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Bolt,
                contentDescription = null,
                tint = BrandBlue,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Dica do personal",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Ink
            )
            Text(
                text = "Não esqueça de se hidratar durante o treino.",
                fontSize = 12.sp,
                color = Muted
            )
        }
        Icon(
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = null,
            tint = BrandBlue,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun PersonalResumidoCard(
    personal: CandidaturaPersonal?,
    ultimaMensagem: String?,
    horario: String?,
    onAbrirChat: () -> Unit
) {
    if (personal == null) {
        PersonalSemEscolhaResumo(onClick = onAbrirChat)
        return
    }

    val iniciais = personal.nome.split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercase() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .revealShadow(7.dp, RoundedCornerShape(20.dp), clip = false)
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.linearGradient(listOf(Color(0xFFEAF7FE), Color(0xFFD8EFFC))))
            .clickable { onAbrirChat() }
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(52.dp)) {
                ProfilePhoto(
                    fotoUri = personal.fotoUri,
                    iniciais = iniciais.ifBlank { "PT" },
                    tamanho = 52.dp,
                    corInicial = BrandDark,
                    corFinal = BrandBlue
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .padding(2.5.dp)
                        .clip(CircleShape)
                        .background(Mint)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = personal.nome,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Ink
                    )
                    Spacer(modifier = Modifier.width(5.dp))
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
                    color = Muted,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(SurfaceWhite),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Chat,
                    contentDescription = "Abrir conversa",
                    tint = BrandBlue,
                    modifier = Modifier.size(19.dp)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .clip(RoundedCornerShape(13.dp))
                .background(Color.White.copy(alpha = 0.72f))
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "ÚLTIMA MENSAGEM",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp,
                    color = Muted
                )
                Text(
                    text = ultimaMensagem ?: "Comece uma conversa com seu personal.",
                    fontSize = 12.sp,
                    color = Ink,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            if (horario != null) {
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = horario, fontSize = 10.sp, color = Muted)
            }
        }
    }
}

@Composable
private fun PersonalSemEscolhaResumo(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.linearGradient(listOf(Color(0xFFEAF7FE), Color(0xFFD8EFFC))))
            .clickable { onClick() }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(SurfaceWhite),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Groups,
                contentDescription = null,
                tint = BrandBlue,
                modifier = Modifier.size(23.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Escolha seu personal",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Ink
            )
            Text(
                text = "Compare os profissionais aprovados e escolha o ideal para você.",
                fontSize = 11.sp,
                color = Muted,
                lineHeight = 16.sp,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
        Icon(
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = null,
            tint = BrandBlue,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun MetricCard(
    modifier: Modifier,
    icone: ImageVector,
    cor: Color,
    fundo: Color,
    valor: String,
    rotulo: String
) {
    Row(
        modifier = modifier
            .revealShadow(5.dp, RoundedCornerShape(15.dp), clip = false)
            .clip(RoundedCornerShape(15.dp))
            .background(SurfaceWhite)
            .padding(horizontal = 8.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(31.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(fundo),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icone,
                contentDescription = null,
                tint = cor,
                modifier = Modifier.size(16.dp)
            )
        }
        Spacer(modifier = Modifier.width(7.dp))
        Column {
            Text(text = valor, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Ink)
            Text(text = rotulo, fontSize = 11.sp, color = Muted)
        }
    }
}
