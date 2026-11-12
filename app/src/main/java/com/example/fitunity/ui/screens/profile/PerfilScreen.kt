package com.example.fitunity.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fitunity.data.CandidaturaPersonal
import com.example.fitunity.data.FitUnityDbHelper
import com.example.fitunity.data.FotoPerfil
import com.example.fitunity.data.HistoricoTreino
import com.example.fitunity.data.MensagemChat
import com.example.fitunity.data.PerfilCliente
import com.example.fitunity.data.RecursoPlano
import com.example.fitunity.data.SessionManager
import com.example.fitunity.data.StatusCandidatura
import com.example.fitunity.data.possuiAcesso
import com.example.fitunity.ui.components.Avatar
import com.example.fitunity.ui.components.ProfilePhoto
import com.example.fitunity.ui.components.FitUnityBottomBar
import com.example.fitunity.ui.components.MenuAction
import com.example.fitunity.ui.components.MoreMenu
import com.example.fitunity.ui.components.PageHeader
import com.example.fitunity.ui.components.RevealOnAppear
import com.example.fitunity.ui.components.SectionHeading
import com.example.fitunity.ui.components.revealShadow
import com.example.fitunity.ui.components.StatBlock
import com.example.fitunity.ui.navigation.Rotas
import com.example.fitunity.ui.theme.BrandBlue
import com.example.fitunity.ui.theme.BrandDark
import com.example.fitunity.ui.theme.Coral
import com.example.fitunity.ui.theme.Danger
import com.example.fitunity.ui.theme.GoldText
import com.example.fitunity.ui.theme.Ink
import com.example.fitunity.ui.theme.Mint
import com.example.fitunity.ui.theme.Muted
import com.example.fitunity.ui.theme.SoftBlueBackground
import com.example.fitunity.ui.theme.SurfaceWhite

@Composable
fun PerfilScreen(navController: NavController) {
    val context = LocalContext.current
    val perfil = SessionManager.usuarioAtual.value
    val acessoPersonal = possuiAcesso(RecursoPlano.PERSONAL_TRAINER)
    val acessoRelatorio = possuiAcesso(RecursoPlano.RELATORIO_MENSAL)
    val nome = perfil?.nome ?: "Usuário FitUnity"
    val iniciais = nome.split(" ").filter { it.isNotBlank() }.take(2)
        .joinToString("") { it.first().uppercase() }
    val foto = remember(perfil?.fotoUri) { FotoPerfil.carregar(perfil?.fotoUri) }
    val db = remember { FitUnityDbHelper(context.applicationContext) }
    val usuarioId = perfil?.id
    val personalEscolhido = remember(usuarioId) {
        if (usuarioId != null) db.buscarPersonalEscolhido(usuarioId) else null
    }
    val mensagens = remember(usuarioId, personalEscolhido?.usuarioId) {
        val personalId = personalEscolhido?.usuarioId
        if (usuarioId != null && personalId != null) {
            db.listarMensagensDoAluno(usuarioId, personalId)
        } else {
            emptyList()
        }
    }
    val notificacoesNaoLidas = remember(usuarioId) {
        if (usuarioId != null) db.listarNotificacoes(usuarioId).count { !it.lida } else 0
    }
    val historico = remember(usuarioId) {
        if (usuarioId != null) db.listarHistorico(usuarioId) else emptyList()
    }
    val candidatura = remember(usuarioId) {
        if (usuarioId != null) db.buscarCandidatura(usuarioId) else null
    }

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
            PageHeader(
                titulo = "Meu perfil",
                subtitulo = "Gerencie sua conta e preferências"
            ) {
                MoreMenu(
                    itens = listOf(
                        MenuAction("Editar dados", "Nome, e-mail e medidas") {
                            navController.navigate(Rotas.EDITAR_DADOS)
                        },
                        MenuAction("Notificações", "Lembretes e avisos") {
                            navController.navigate(Rotas.NOTIFICACOES)
                        },
                        MenuAction(
                            "Ver planos",
                            "Plano atual: ${nomePlanoExibicao(perfil?.tipoPlano)}"
                        ) {
                            navController.navigate(Rotas.PLANOS)
                        }
                    )
                )
            }
            }

            Spacer(modifier = Modifier.height(20.dp))

            RevealOnAppear(
                modifier = Modifier.fillMaxWidth(),
                delayMillis = 80,
                offsetY = 20.dp,
                scaleFrom = 0.97f
            ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(listOf(Color(0xFF3AA6EA), Color(0xFF0E7BB8)))
                    )
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .clip(CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    if (foto != null) {
                        Image(
                            bitmap = foto.asImageBitmap(),
                            contentDescription = "Foto de perfil",
                            modifier = Modifier
                                .size(82.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Avatar(
                            iniciais = iniciais.ifBlank { "FU" },
                            tamanho = 82.dp,
                            corInicial = Color(0xFF0A3F63),
                            corFinal = Color(0xFF20B7E8)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = nome, fontSize = 19.sp, fontWeight = FontWeight.Bold, color = Color.White, textAlign = TextAlign.Center)
                Text(
                    text = perfil?.email ?: "usuario@fitunity.com",
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
                Row(
                    modifier = Modifier.padding(top = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.White.copy(alpha = 0.18f))
                            .padding(horizontal = 11.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = nomePlanoExibicao(perfil?.tipoPlano),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.White.copy(alpha = 0.18f))
                            .padding(horizontal = 11.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = perfil?.nivel ?: "Intermediário",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp)
                    .revealShadow(6.dp, RoundedCornerShape(18.dp), clip = false)
                    .clip(RoundedCornerShape(18.dp))
                    .background(SurfaceWhite)
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatBlock(valor = "${perfil?.treinosRealizados ?: 0}", rotulo = "Treinos", corValor = BrandBlue)
                StatBlock(valor = "${perfil?.pesoAtual ?: 0.0} kg", rotulo = "Peso atual", corValor = Mint)
                StatBlock(valor = "${perfil?.progresso?.toInt() ?: 0}%", rotulo = "Progresso", corValor = Coral)
            }
            }

            RevealOnAppear(
                modifier = Modifier.fillMaxWidth(),
                delayMillis = 220,
                offsetY = 16.dp
            ) {
            SectionHeading(
                titulo = "Seu acompanhamento",
                subtitulo = "Recursos exclusivos do seu plano"
            )
            }

            val opcoesAcompanhamento = listOf(
                OpcaoAcompanhamento(
                    tipo = TipoAcompanhamento.PERSONAL,
                    titulo = "Meu personal",
                    disponivel = acessoPersonal,
                    planoNecessario = "Adaptado",
                    icone = Icons.Filled.Person,
                    onClick = {
                        navController.navigate(
                            if (personalEscolhido == null) Rotas.ESCOLHER_PERSONAL else Rotas.PERSONAL
                        )
                    }
                ),
                OpcaoAcompanhamento(
                    tipo = TipoAcompanhamento.RELATORIO,
                    titulo = "Relatório mensal",
                    disponivel = acessoRelatorio,
                    planoNecessario = "Profissional",
                    icone = Icons.Filled.BarChart,
                    onClick = { navController.navigate(Rotas.RELATORIO_MENSAL) }
                )
            )
            val opcoesOrdenadas = if (opcoesAcompanhamento.count { it.disponivel } == 1) {
                opcoesAcompanhamento.sortedBy { if (it.disponivel) 1 else 0 }
            } else {
                opcoesAcompanhamento
            }
            opcoesOrdenadas.forEachIndexed { index, opcao ->
                RevealOnAppear(
                    modifier = Modifier.fillMaxWidth(),
                    delayMillis = 260 + index * 70,
                    offsetY = 14.dp
                ) {
                    if (opcao.disponivel) {
                        when (opcao.tipo) {
                            TipoAcompanhamento.PERSONAL -> PersonalDestaqueCard(
                                personal = personalEscolhido,
                                mensagens = mensagens,
                                notificacoesNaoLidas = notificacoesNaoLidas,
                                onClick = opcao.onClick
                            )
                            TipoAcompanhamento.RELATORIO -> RelatorioDestaqueCard(
                                historico = historico,
                                perfil = perfil,
                                onClick = opcao.onClick
                            )
                        }
                    } else {
                        MenuRow(
                            icone = opcao.icone,
                            titulo = opcao.titulo,
                            subtitulo = "Disponível no plano ${opcao.planoNecessario}",
                            bloqueado = true,
                            onClick = opcao.onClick
                        )
                    }
                }
            }

            RevealOnAppear(
                modifier = Modifier.fillMaxWidth(),
                delayMillis = 400,
                offsetY = 16.dp
            ) {
            SectionHeading(
                titulo = "Faça parte do time",
                subtitulo = "Trabalhe como personal trainer"
            )
            }

            RevealOnAppear(
                modifier = Modifier.fillMaxWidth(),
                delayMillis = 440,
                offsetY = 14.dp
            ) {
                JunteSeCard(
                    candidatura = candidatura,
                    onClick = {
                        if (candidatura?.status == StatusCandidatura.APROVADO) {
                            navController.navigate(Rotas.PAINEL_PERSONAL)
                        } else {
                            navController.navigate(Rotas.TRABALHE_CONOSCO)
                        }
                    }
                )
            }

            RevealOnAppear(
                modifier = Modifier.fillMaxWidth(),
                delayMillis = 460,
                offsetY = 16.dp
            ) {
            SectionHeading(titulo = "Conta")
            }

            Spacer(modifier = Modifier.height(10.dp))

            RevealOnAppear(
                modifier = Modifier.fillMaxWidth(),
                delayMillis = 520,
                offsetY = 16.dp
            ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFFDECEE))
                    .clickable {
                        SessionManager.logout(context)
                        navController.navigate(Rotas.LOGIN) {
                            popUpTo(Rotas.HOME) { inclusive = true }
                        }
                    }
                    .padding(15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Logout,
                    contentDescription = null,
                    tint = Danger,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "Sair da conta", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Danger)
            }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
private fun JunteSeCard(
    candidatura: CandidaturaPersonal?,
    onClick: () -> Unit
) {
    val titulo = when (candidatura?.status) {
        StatusCandidatura.EM_ANALISE -> "Candidatura em análise"
        StatusCandidatura.APROVADO -> "Você é personal FitUnity"
        StatusCandidatura.REPROVADO -> "Candidatura não aprovada"
        null -> "Trabalhe como personal trainer"
    }
    val subtitulo = when (candidatura?.status) {
        StatusCandidatura.EM_ANALISE -> "Estamos avaliando seu currículo"
        StatusCandidatura.APROVADO -> "Acesse seu painel e converse com alunos"
        StatusCandidatura.REPROVADO -> "Revise seus dados e envie novamente"
        null -> "Envie seu currículo e faça parte do time"
    }
    val icone = when (candidatura?.status) {
        StatusCandidatura.EM_ANALISE -> Icons.Filled.HourglassTop
        StatusCandidatura.APROVADO -> Icons.Filled.Verified
        StatusCandidatura.REPROVADO -> Icons.Filled.Groups
        null -> Icons.Filled.Groups
    }
    val corIcone = when (candidatura?.status) {
        StatusCandidatura.EM_ANALISE -> GoldText
        StatusCandidatura.APROVADO -> Mint
        StatusCandidatura.REPROVADO -> Danger
        null -> BrandBlue
    }
    val rotuloStatus = candidatura?.status?.rotulo

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .revealShadow(6.dp, RoundedCornerShape(20.dp), clip = false)
            .clip(RoundedCornerShape(20.dp))
            .background(SurfaceWhite)
            .clickable { onClick() }
            .padding(15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(13.dp))
                .background(SoftBlueBackground),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icone,
                contentDescription = null,
                tint = corIcone,
                modifier = Modifier.size(21.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = titulo,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Ink
            )
            Text(
                text = subtitulo,
                fontSize = 11.sp,
                color = Muted,
                modifier = Modifier.padding(top = 1.dp)
            )
        }
        if (rotuloStatus != null) {
            Box(
                modifier = Modifier
                    .padding(end = 6.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(SoftBlueBackground)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = rotuloStatus,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = corIcone
                )
            }
        }
        Icon(
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = null,
            tint = BrandBlue,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
private fun MenuRow(
    icone: ImageVector,
    titulo: String,
    subtitulo: String,
    bloqueado: Boolean = false,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 9.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceWhite)
            .clickable { onClick() }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
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
                tint = BrandDark,
                modifier = Modifier.size(19.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = titulo, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Ink)
            Text(text = subtitulo, fontSize = 11.sp, color = Muted)
        }
        Icon(
            imageVector = if (bloqueado) Icons.Filled.Lock else Icons.Filled.ChevronRight,
            contentDescription = if (bloqueado) "Disponível em outro plano" else null,
            tint = Muted,
            modifier = Modifier.size(18.dp)
        )
    }
}

private enum class TipoAcompanhamento { PERSONAL, RELATORIO }

private data class OpcaoAcompanhamento(
    val tipo: TipoAcompanhamento,
    val titulo: String,
    val disponivel: Boolean,
    val planoNecessario: String,
    val icone: ImageVector,
    val onClick: () -> Unit
)

@Composable
private fun PersonalDestaqueCard(
    personal: CandidaturaPersonal?,
    mensagens: List<MensagemChat>,
    notificacoesNaoLidas: Int,
    onClick: () -> Unit
) {
    if (personal == null) {
        PersonalSemEscolhaCard(onClick = onClick)
        return
    }

    val ultima = mensagens.lastOrNull()
    val naoLidas = mensagens.takeLastWhile { !it.enviadaPorMim }.size
    val primeiroNome = personal.nome.substringBefore(" ")
    val iniciais = personal.nome.split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercase() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .revealShadow(7.dp, RoundedCornerShape(22.dp), clip = false)
            .clip(RoundedCornerShape(22.dp))
            .background(SurfaceWhite)
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(58.dp)) {
                ProfilePhoto(
                    fotoUri = personal.fotoUri,
                    iniciais = iniciais.ifBlank { "PT" },
                    tamanho = 58.dp,
                    corInicial = BrandDark,
                    corFinal = BrandBlue
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(15.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .padding(2.5.dp)
                        .clip(CircleShape)
                        .background(Mint)
                )
                if (naoLidas > 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(Danger),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (naoLidas > 9) "9+" else "$naoLidas",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
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
                Row(
                    modifier = Modifier.padding(top = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    personal.especialidades.take(2).forEach { MiniChip(it) }
                }
            }
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = BrandBlue,
                modifier = Modifier.size(20.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 14.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(SoftBlueBackground)
                .padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Chat,
                    contentDescription = null,
                    tint = BrandBlue,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "ÚLTIMA MENSAGEM",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp,
                    color = Muted
                )
                Text(
                    text = ultima?.texto
                        ?: "Nenhuma mensagem ainda. Envie um oi para $primeiroNome!",
                    fontSize = 12.sp,
                    color = Ink,
                    lineHeight = 17.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            if (ultima != null) {
                Text(
                    text = ultima.horario,
                    fontSize = 10.sp,
                    color = Muted,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(7.dp)
                    .clip(CircleShape)
                    .background(Mint)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Acompanhamento ativo",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = Mint
            )
            Spacer(modifier = Modifier.weight(1f))
            if (notificacoesNaoLidas > 0) {
                Icon(
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = null,
                    tint = BrandBlue,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "$notificacoesNaoLidas notificações",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BrandBlue
                )
            } else {
                Text(
                    text = "Sem novas notificações",
                    fontSize = 11.sp,
                    color = Muted
                )
            }
        }
    }
}

@Composable
private fun PersonalSemEscolhaCard(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .revealShadow(7.dp, RoundedCornerShape(22.dp), clip = false)
            .clip(RoundedCornerShape(22.dp))
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFFEAF7FE), Color(0xFFD8EFFC))
                )
            )
            .clickable { onClick() }
            .padding(16.dp),
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
                text = "Compare os profissionais aprovados e encontre o ideal para você.",
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
private fun RelatorioDestaqueCard(
    historico: List<HistoricoTreino>,
    perfil: PerfilCliente?,
    onClick: () -> Unit
) {
    val treinos = historico.size
    val kcal = historico.sumOf { treino ->
        treino.calorias.filter { it.isDigit() }.toIntOrNull() ?: 0
    }
    val totalTreinos = (perfil?.treinosRealizados ?: 0) + (perfil?.treinosPendentes ?: 0)
    val consistencia = if (totalTreinos > 0) {
        ((perfil?.treinosRealizados ?: 0) * 100 / totalTreinos).coerceIn(0, 100)
    } else 0
    val barras = historico.takeLast(6).map { treino ->
        (treino.calorias.filter { it.isDigit() }.toIntOrNull() ?: 1).toFloat()
    }.ifEmpty { listOf(30f, 48f, 36f, 52f, 40f, 44f) }
    val maxBarra = barras.maxOrNull()?.coerceAtLeast(1f) ?: 1f

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .revealShadow(7.dp, RoundedCornerShape(22.dp), clip = false)
            .clip(RoundedCornerShape(22.dp))
            .background(Brush.linearGradient(listOf(SurfaceWhite, Color(0xFFF1FAFF))))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(Brush.linearGradient(listOf(BrandBlue, BrandDark))),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.BarChart,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Relatório mensal",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Ink
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Mint.copy(alpha = 0.14f))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "Abril",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Mint
                        )
                    }
                }
                Text(
                    text = "Sua evolução revisada por um profissional",
                    fontSize = 11.sp,
                    color = Muted,
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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 14.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color.White.copy(alpha = 0.75f))
                .padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "ÚLTIMOS TREINOS",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp,
                    color = Muted
                )
                Row(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .height(46.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    barras.forEach { valor ->
                        Box(
                            modifier = Modifier
                                .width(16.dp)
                                .height((10 + (valor / maxBarra) * 36).dp)
                                .clip(RoundedCornerShape(5.dp))
                                .background(
                                    Brush.verticalGradient(listOf(BrandBlue, Color(0xFF7FD4F5)))
                                )
                        )
                    }
                }
            }
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                MiniMetrico(valor = "$treinos", rotulo = "treinos")
                MiniMetrico(valor = "$consistencia%", rotulo = "consistência")
                MiniMetrico(valor = "$kcal", rotulo = "kcal")
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.LocalFireDepartment,
                contentDescription = null,
                tint = Coral,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = "Seu mês em números",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = Coral
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Ver relatório completo",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = BrandBlue
            )
            Spacer(modifier = Modifier.width(3.dp))
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = BrandBlue,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

@Composable
private fun MiniChip(texto: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(SoftBlueBackground)
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = texto,
            fontSize = 9.sp,
            fontWeight = FontWeight.SemiBold,
            color = BrandDark
        )
    }
}

@Composable
private fun MiniMetrico(valor: String, rotulo: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = valor, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Ink)
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = rotulo, fontSize = 10.sp, color = Muted)
    }
}
