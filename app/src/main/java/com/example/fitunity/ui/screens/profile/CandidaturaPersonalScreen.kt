package com.example.fitunity.ui.screens.profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.WorkHistory
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fitunity.data.CandidaturaPersonal
import com.example.fitunity.data.CurriculoArquivo
import com.example.fitunity.data.FitUnityDbHelper
import com.example.fitunity.data.SessionManager
import com.example.fitunity.data.StatusCandidatura
import com.example.fitunity.ui.components.AppTextField
import com.example.fitunity.ui.components.DetailHeader
import com.example.fitunity.ui.components.PrimaryActionButton
import com.example.fitunity.ui.components.RevealOnAppear
import com.example.fitunity.ui.components.SectionHeading
import com.example.fitunity.ui.components.revealShadow
import com.example.fitunity.ui.navigation.Rotas
import com.example.fitunity.ui.theme.BrandBlue
import com.example.fitunity.ui.theme.BrandDark
import com.example.fitunity.ui.theme.Danger
import com.example.fitunity.ui.theme.DangerBackground
import com.example.fitunity.ui.theme.GoldBackground
import com.example.fitunity.ui.theme.GoldText
import com.example.fitunity.ui.theme.Ink
import com.example.fitunity.ui.theme.Mint
import com.example.fitunity.ui.theme.MintBackground
import com.example.fitunity.ui.theme.Muted
import com.example.fitunity.ui.theme.SoftBlueBackground
import com.example.fitunity.ui.theme.SoftBlueBorder
import com.example.fitunity.ui.theme.SurfaceWhite
import com.example.fitunity.ui.theme.Track
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val especialidadesDisponiveis = listOf(
    "Hipertrofia",
    "Emagrecimento",
    "Funcional",
    "Reabilitação",
    "Nutrição esportiva",
    "Treino para idosos"
)

@Composable
fun CandidaturaPersonalScreen(navController: NavController) {
    val context = LocalContext.current
    val db = remember { FitUnityDbHelper(context.applicationContext) }
    val perfil = SessionManager.usuarioAtual.value
    val usuarioId = perfil?.id ?: 0L

    var candidatura by remember(usuarioId) { mutableStateOf(db.buscarCandidatura(usuarioId)) }
    var mostrarFormulario by remember(candidatura?.id, candidatura?.status) {
        mutableStateOf(candidatura == null || candidatura?.status == StatusCandidatura.REPROVADO)
    }

    var nome by remember(candidatura?.id) {
        mutableStateOf(candidatura?.nome ?: perfil?.nome.orEmpty())
    }
    var email by remember(candidatura?.id) {
        mutableStateOf(candidatura?.email ?: perfil?.email.orEmpty())
    }
    var telefone by remember(candidatura?.id) { mutableStateOf(candidatura?.telefone.orEmpty()) }
    var cref by remember(candidatura?.id) { mutableStateOf(candidatura?.cref.orEmpty()) }
    var experiencia by remember(candidatura?.id) { mutableStateOf(candidatura?.experiencia.orEmpty()) }
    var apresentacao by remember(candidatura?.id) { mutableStateOf(candidatura?.apresentacao.orEmpty()) }
    var especialidades by remember(candidatura?.id) {
        mutableStateOf(candidatura?.especialidades?.toSet().orEmpty())
    }
    var curriculoUri by remember(candidatura?.id) { mutableStateOf(candidatura?.curriculoUri) }
    var curriculoNome by remember(candidatura?.id) { mutableStateOf(candidatura?.curriculoNome) }
    var erro by remember(candidatura?.id) { mutableStateOf<String?>(null) }

    val seletorCurriculo = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            val nomeOriginal = CurriculoArquivo.nomeExibicao(context, uri) ?: "curriculo.pdf"
            val caminho = CurriculoArquivo.salvar(
                context = context,
                uri = uri,
                usuarioId = usuarioId,
                nomeOriginal = nomeOriginal,
                caminhoAnterior = curriculoUri
            )
            if (caminho != null) {
                curriculoUri = caminho
                curriculoNome = nomeOriginal
                erro = null
            } else {
                erro = "Não foi possível anexar o currículo. Tente outro arquivo."
            }
        }
    }

    fun enviarCandidatura() {
        val validacao = when {
            nome.isBlank() -> "Informe seu nome completo."
            !email.contains("@") || email.length < 5 -> "Informe um e-mail válido."
            telefone.filter { it.isDigit() }.length < 10 -> "Informe um telefone válido com DDD."
            cref.isBlank() -> "Informe seu número de CREF."
            experiencia.isBlank() -> "Informe seu tempo de experiência."
            especialidades.isEmpty() -> "Selecione pelo menos uma especialidade."
            apresentacao.isBlank() -> "Conte um pouco sobre você e sua experiência."
            curriculoUri.isNullOrBlank() -> "Anexe seu currículo em PDF ou DOC."
            else -> null
        }
        erro = validacao
        if (validacao != null) return

        candidatura = db.salvarCandidatura(
            CandidaturaPersonal(
                usuarioId = usuarioId,
                nome = nome,
                email = email,
                telefone = telefone,
                cref = cref,
                especialidades = especialidades.toList(),
                experiencia = experiencia,
                apresentacao = apresentacao,
                curriculoUri = curriculoUri,
                curriculoNome = curriculoNome,
                status = StatusCandidatura.EM_ANALISE
            )
        )
        mostrarFormulario = false
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
                titulo = "Junte-se a nós",
                onBack = { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.height(16.dp))

            RevealOnAppear(
                modifier = Modifier.fillMaxWidth(),
                delayMillis = 0,
                offsetY = 14.dp
            ) {
                HeroJunteSe()
            }

            if (mostrarFormulario) {
                FormularioCandidatura(
                    nome = nome,
                    onNomeChange = { nome = it },
                    email = email,
                    onEmailChange = { email = it },
                    telefone = telefone,
                    onTelefoneChange = { telefone = it },
                    cref = cref,
                    onCrefChange = { cref = it },
                    experiencia = experiencia,
                    onExperienciaChange = { experiencia = it },
                    apresentacao = apresentacao,
                    onApresentacaoChange = { apresentacao = it },
                    especialidades = especialidades,
                    onToggleEspecialidade = { item ->
                        especialidades = if (item in especialidades) {
                            especialidades - item
                        } else {
                            especialidades + item
                        }
                    },
                    curriculoNome = curriculoNome,
                    onAnexarCurriculo = {
                        seletorCurriculo.launch(
                            arrayOf(
                                "application/pdf",
                                "application/msword",
                                "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                            )
                        )
                    },
                    onRemoverCurriculo = {
                        CurriculoArquivo.excluir(curriculoUri)
                        curriculoUri = null
                        curriculoNome = null
                    },
                    erro = erro,
                    onEnviar = { enviarCandidatura() }
                )
            } else {
                candidatura?.let { atual ->
                    DetalhesCandidatura(
                        candidatura = atual,
                        onAprovar = {
                            db.atualizarStatusCandidatura(usuarioId, StatusCandidatura.APROVADO)
                            candidatura = db.buscarCandidatura(usuarioId)
                        },
                        onReprovar = {
                            db.atualizarStatusCandidatura(usuarioId, StatusCandidatura.REPROVADO)
                            candidatura = db.buscarCandidatura(usuarioId)
                        },
                        onAbrirPainel = { navController.navigate(Rotas.PAINEL_PERSONAL) },
                        onNovaCandidatura = {
                            CurriculoArquivo.excluir(curriculoUri)
                            db.removerCandidatura(usuarioId)
                            candidatura = null
                            mostrarFormulario = true
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
private fun HeroJunteSe() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .revealShadow(12.dp, RoundedCornerShape(26.dp), clip = false)
            .clip(RoundedCornerShape(26.dp))
            .background(Brush.linearGradient(listOf(Color(0xFF3AA6EA), Color(0xFF0E7BB8))))
            .padding(22.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(Color.White.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.PersonAdd,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "TRABALHE COM A GENTE",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
                Text(
                    text = "Seja um personal FitUnity",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Envie seu currículo, passe pela análise da nossa equipe e acompanhe alunos que já têm direito ao personal trainer pelo plano.",
            fontSize = 13.sp,
            color = Color.White.copy(alpha = 0.88f),
            lineHeight = 19.sp
        )
        Spacer(modifier = Modifier.height(14.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
            HeroPill("Currículo online")
            HeroPill("Chat com alunos")
        }
    }
}

@Composable
private fun HeroPill(texto: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White.copy(alpha = 0.18f))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = texto,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
    }
}

@Composable
private fun FormularioCandidatura(
    nome: String,
    onNomeChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    telefone: String,
    onTelefoneChange: (String) -> Unit,
    cref: String,
    onCrefChange: (String) -> Unit,
    experiencia: String,
    onExperienciaChange: (String) -> Unit,
    apresentacao: String,
    onApresentacaoChange: (String) -> Unit,
    especialidades: Set<String>,
    onToggleEspecialidade: (String) -> Unit,
    curriculoNome: String?,
    onAnexarCurriculo: () -> Unit,
    onRemoverCurriculo: () -> Unit,
    erro: String?,
    onEnviar: () -> Unit
) {
    RevealOnAppear(
        modifier = Modifier.fillMaxWidth(),
        delayMillis = 70,
        offsetY = 16.dp
    ) {
        Column {
            SectionHeading(
                titulo = "Sua candidatura",
                subtitulo = "Preencha seus dados profissionais"
            )
            FormCard {
                SubsecaoHeader(
                    icone = Icons.Filled.PersonAdd,
                    titulo = "Informações pessoais"
                )
                AppTextField(
                    value = nome,
                    onValueChange = onNomeChange,
                    placeholder = "Seu nome completo",
                    label = "Nome completo"
                )
                Spacer(modifier = Modifier.height(12.dp))
                AppTextField(
                    value = email,
                    onValueChange = onEmailChange,
                    placeholder = "seu@email.com",
                    label = "E-mail",
                    keyboardType = KeyboardType.Email,
                    leadingIcon = Icons.Filled.Email
                )
                Spacer(modifier = Modifier.height(12.dp))
                AppTextField(
                    value = telefone,
                    onValueChange = { onTelefoneChange(it.filter { ch -> ch.isDigit() || ch in "+-() " }) },
                    placeholder = "(11) 99999-9999",
                    label = "Telefone",
                    keyboardType = KeyboardType.Phone,
                    leadingIcon = Icons.Filled.Phone
                )

                DivisorSecao()

                SubsecaoHeader(
                    icone = Icons.Filled.WorkHistory,
                    titulo = "Dados profissionais"
                )
                AppTextField(
                    value = cref,
                    onValueChange = onCrefChange,
                    placeholder = "CREF 12345-G/SP",
                    label = "CREF"
                )
                Spacer(modifier = Modifier.height(12.dp))
                AppTextField(
                    value = experiencia,
                    onValueChange = onExperienciaChange,
                    placeholder = "Ex.: 5 anos",
                    label = "Tempo de experiência",
                    leadingIcon = Icons.Filled.WorkHistory
                )
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = "Especialidades",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Ink
                )
                Spacer(modifier = Modifier.height(8.dp))
                EspecialidadeSelector(
                    selecionadas = especialidades,
                    onToggle = onToggleEspecialidade
                )
                Spacer(modifier = Modifier.height(14.dp))
                AppTextField(
                    value = apresentacao,
                    onValueChange = onApresentacaoChange,
                    placeholder = "Fale sobre sua trajetória, seus métodos e como você ajuda seus alunos...",
                    label = "Apresentação",
                    singleLine = false,
                    minLines = 3,
                    maxLines = 6
                )

                DivisorSecao()

                SubsecaoHeader(
                    icone = Icons.Filled.Description,
                    titulo = "Currículo"
                )
                AnexoCurriculo(
                    nome = curriculoNome,
                    onAnexar = onAnexarCurriculo,
                    onRemover = onRemoverCurriculo
                )

                if (erro != null) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(DangerBackground)
                            .border(1.dp, Danger.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = erro,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Danger
                        )
                    }
                }
            }

            PrimaryActionButton(
                texto = "Enviar candidatura",
                onClick = onEnviar
            )
        }
    }
}

@Composable
private fun EspecialidadeSelector(
    selecionadas: Set<String>,
    onToggle: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        especialidadesDisponiveis.chunked(2).forEach { linha ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                linha.forEach { especialidade ->
                    val ativo = especialidade in selecionadas
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (ativo) SoftBlueBackground else SurfaceWhite)
                            .border(
                                width = 1.dp,
                                color = if (ativo) BrandBlue else SoftBlueBorder,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { onToggle(especialidade) }
                            .padding(horizontal = 10.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(if (ativo) BrandBlue else Track),
                            contentAlignment = Alignment.Center
                        ) {
                            if (ativo) {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(11.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(7.dp))
                        Text(
                            text = especialidade,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (ativo) BrandDark else Ink,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                if (linha.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun AnexoCurriculo(
    nome: String?,
    onAnexar: () -> Unit,
    onRemover: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(SoftBlueBackground)
            .border(1.dp, SoftBlueBorder, RoundedCornerShape(14.dp))
            .clickable { onAnexar() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(SurfaceWhite),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.UploadFile,
                contentDescription = null,
                tint = BrandBlue,
                modifier = Modifier.size(19.dp)
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = nome ?: "Anexar currículo",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Ink,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = if (nome != null) "Toque para substituir" else "PDF ou DOC",
                fontSize = 10.sp,
                color = Muted
            )
        }
        if (nome != null) {
            IconButton(onClick = onRemover) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Remover currículo",
                    tint = Danger,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun DetalhesCandidatura(
    candidatura: CandidaturaPersonal,
    onAprovar: () -> Unit,
    onReprovar: () -> Unit,
    onAbrirPainel: () -> Unit,
    onNovaCandidatura: () -> Unit
) {
    val visual = visualDoStatus(candidatura.status)

    RevealOnAppear(
        modifier = Modifier.fillMaxWidth(),
        delayMillis = 70,
        offsetY = 16.dp
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 18.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(visual.fundo)
                    .border(1.dp, visual.cor.copy(alpha = 0.24f), RoundedCornerShape(22.dp))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(SurfaceWhite.copy(alpha = 0.8f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = visual.icone,
                        contentDescription = null,
                        tint = visual.cor,
                        modifier = Modifier.size(23.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = visual.titulo,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Ink
                    )
                    Text(
                        text = visual.descricao,
                        fontSize = 12.sp,
                        color = Muted,
                        lineHeight = 17.sp,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }

            SectionHeading(
                titulo = "Resumo da candidatura",
                subtitulo = "Enviada em ${formatarData(candidatura.criadaEm)}"
            )
            FormCard {
                InfoLinha(rotulo = "Nome", valor = candidatura.nome)
                InfoLinha(rotulo = "E-mail", valor = candidatura.email)
                InfoLinha(rotulo = "Telefone", valor = candidatura.telefone)
                InfoLinha(rotulo = "CREF", valor = candidatura.cref)
                InfoLinha(rotulo = "Experiência", valor = candidatura.experiencia)
                InfoLinha(rotulo = "Status", valor = candidatura.status.rotulo)

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Especialidades",
                    fontSize = 11.sp,
                    color = Muted
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = candidatura.especialidades.joinToString(" · "),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BrandDark
                )

                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = "Apresentação",
                    fontSize = 11.sp,
                    color = Muted
                )
                Text(
                    text = candidatura.apresentacao,
                    fontSize = 12.sp,
                    color = Ink,
                    lineHeight = 18.sp,
                    modifier = Modifier.padding(top = 3.dp)
                )

                if (!candidatura.curriculoNome.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(SoftBlueBackground)
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Description,
                            contentDescription = null,
                            tint = BrandBlue,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = candidatura.curriculoNome,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Ink,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(text = "Currículo anexado", fontSize = 10.sp, color = Muted)
                        }
                    }
                }
            }

            when (candidatura.status) {
                StatusCandidatura.EM_ANALISE -> AnaliseEquipe(
                    onAprovar = onAprovar,
                    onReprovar = onReprovar
                )
                StatusCandidatura.APROVADO -> {
                    PrimaryActionButton(
                        texto = "Acessar painel do personal",
                        onClick = onAbrirPainel,
                        cor = Mint
                    )
                }
                StatusCandidatura.REPROVADO -> {
                    PrimaryActionButton(
                        texto = "Enviar nova candidatura",
                        onClick = onNovaCandidatura
                    )
                }
            }
        }
    }
}

@Composable
private fun AnaliseEquipe(
    onAprovar: () -> Unit,
    onReprovar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 18.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(GoldBackground)
            .border(1.dp, GoldText.copy(alpha = 0.2f), RoundedCornerShape(18.dp))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Filled.Verified,
                contentDescription = null,
                tint = GoldText,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(7.dp))
            Text(
                text = "Análise da equipe FitUnity",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Ink
            )
        }
        Text(
            text = "Nesta versão de demonstração, nossa equipe pode aprovar ou recusar a candidatura diretamente por aqui.",
            fontSize = 11.sp,
            color = Muted,
            lineHeight = 16.sp,
            modifier = Modifier.padding(top = 6.dp)
        )
        PrimaryActionButton(
            texto = "Aprovar candidatura",
            onClick = onAprovar,
            cor = Mint
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(SurfaceWhite)
                .border(1.dp, Danger.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
                .clickable { onReprovar() }
                .padding(vertical = 13.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Recusar candidatura",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Danger
            )
        }
    }
}

@Composable
private fun FormCard(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .revealShadow(6.dp, RoundedCornerShape(20.dp), clip = false)
            .clip(RoundedCornerShape(20.dp))
            .background(SurfaceWhite)
            .border(1.dp, SoftBlueBorder.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
            .padding(16.dp),
        content = content
    )
}

@Composable
private fun SubsecaoHeader(icone: ImageVector, titulo: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(SoftBlueBackground),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icone,
                contentDescription = null,
                tint = BrandBlue,
                modifier = Modifier.size(16.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = titulo,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Ink
        )
    }
    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
private fun DivisorSecao() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .height(1.dp)
            .background(SoftBlueBorder.copy(alpha = 0.7f))
    )
}

@Composable
private fun InfoLinha(rotulo: String, valor: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp)
    ) {
        Text(
            text = rotulo,
            fontSize = 11.sp,
            color = Muted,
            modifier = Modifier.width(90.dp)
        )
        Text(
            text = valor,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Ink,
            modifier = Modifier.weight(1f)
        )
    }
}

private data class VisualStatus(
    val cor: Color,
    val fundo: Color,
    val icone: ImageVector,
    val titulo: String,
    val descricao: String
)

private fun visualDoStatus(status: StatusCandidatura): VisualStatus = when (status) {
    StatusCandidatura.EM_ANALISE -> VisualStatus(
        cor = GoldText,
        fundo = GoldBackground,
        icone = Icons.Filled.HourglassTop,
        titulo = "Candidatura em análise",
        descricao = "Nossa equipe está avaliando seu currículo. Você será avisado assim que houver uma resposta."
    )
    StatusCandidatura.APROVADO -> VisualStatus(
        cor = Mint,
        fundo = MintBackground,
        icone = Icons.Filled.Verified,
        titulo = "Parabéns, você foi aprovado!",
        descricao = "Agora você faz parte do time FitUnity e pode conversar com alunos que têm acompanhamento pessoal."
    )
    StatusCandidatura.REPROVADO -> VisualStatus(
        cor = Danger,
        fundo = DangerBackground,
        icone = Icons.Filled.Cancel,
        titulo = "Candidatura não aprovada",
        descricao = "Revise suas informações e envie uma nova candidatura quando quiser."
    )
}

private fun formatarData(timestamp: Long): String {
    if (timestamp <= 0L) return "agora"
    return SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR")).format(Date(timestamp))
}
