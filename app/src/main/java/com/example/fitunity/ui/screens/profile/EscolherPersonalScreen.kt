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
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fitunity.data.CandidaturaPersonal
import com.example.fitunity.data.FitUnityDbHelper
import com.example.fitunity.data.RecursoPlano
import com.example.fitunity.data.SessionManager
import com.example.fitunity.data.possuiAcesso
import com.example.fitunity.ui.components.ProfilePhoto
import com.example.fitunity.ui.components.ConteudoBloqueadoPorPlano
import com.example.fitunity.ui.components.DetailHeader
import com.example.fitunity.ui.components.FilterChips
import com.example.fitunity.ui.components.SearchField
import com.example.fitunity.ui.components.SoftCard
import com.example.fitunity.ui.components.TagChip
import com.example.fitunity.ui.components.revealShadow
import com.example.fitunity.ui.navigation.Rotas
import com.example.fitunity.ui.theme.BrandBlue
import com.example.fitunity.ui.theme.BrandDark
import com.example.fitunity.ui.theme.GoldBackground
import com.example.fitunity.ui.theme.GoldText
import com.example.fitunity.ui.theme.Ink
import com.example.fitunity.ui.theme.Mint
import com.example.fitunity.ui.theme.MintBackground
import com.example.fitunity.ui.theme.Muted
import com.example.fitunity.ui.theme.SoftBlueBorder
import com.example.fitunity.ui.theme.SurfaceWhite

@Composable
fun EscolherPersonalScreen(navController: NavController) {
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
    val personais = remember { db.listarPersonaisDisponiveis() }
    val personalAtualId = remember(usuarioId) {
        usuarioId?.let { db.buscarPersonalEscolhido(it)?.usuarioId }
    }
    var selecionadoId by remember(personalAtualId) { mutableStateOf(personalAtualId) }
    var busca by remember { mutableStateOf("") }
    var filtro by remember { mutableStateOf(0) }
    var pendente by remember { mutableStateOf<CandidaturaPersonal?>(null) }

    val especialidades = remember(personais) {
        listOf("Todos") + personais.flatMap { it.especialidades }
            .distinct()
            .sortedBy { it.lowercase() }
    }
    val filtroAtual = especialidades.getOrElse(filtro) { "Todos" }
    val foco = perfil?.foco

    val filtrados = remember(personais, busca, filtroAtual, foco) {
        personais
            .filter { personal ->
                val termo = busca.trim().lowercase()
                val casaBusca = termo.isEmpty() ||
                    personal.nome.lowercase().contains(termo) ||
                    personal.cref.lowercase().contains(termo) ||
                    personal.experiencia.lowercase().contains(termo) ||
                    personal.apresentacao.lowercase().contains(termo) ||
                    personal.especialidades.any { it.lowercase().contains(termo) }
                val casaFiltro = filtroAtual == "Todos" ||
                    personal.especialidades.any { it.equals(filtroAtual, ignoreCase = true) }
                casaBusca && casaFiltro
            }
            .sortedWith(
                compareBy(
                    { if (combinaComFoco(it, foco)) 0 else 1 },
                    { it.nome.lowercase() }
                )
            )
    }

    val escolher: (CandidaturaPersonal) -> Unit = { personal ->
        val id = usuarioId
        if (id != null && db.escolherPersonal(id, personal.usuarioId)) {
            selecionadoId = personal.usuarioId
            if (!navController.popBackStack()) {
                navController.navigate(Rotas.PERSONAL)
            }
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
                titulo = "Escolher personal",
                onBack = { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Compare os profissionais aprovados e escolha quem combina mais " +
                    "com seus objetivos. Você pode trocar quando quiser.",
                fontSize = 13.sp,
                color = Muted,
                lineHeight = 19.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            SearchField(
                value = busca,
                onValueChange = { busca = it },
                placeholder = "Buscar por nome, especialidade ou CREF..."
            )

            Spacer(modifier = Modifier.height(12.dp))

            FilterChips(
                opcoes = especialidades,
                selecionado = filtro,
                onSelecionar = { filtro = it }
            )

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = if (filtrados.size == 1) {
                    "1 personal disponível"
                } else {
                    "${filtrados.size} personais disponíveis"
                },
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Muted
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (filtrados.isEmpty()) {
                SoftCard {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                                .background(BrandBlue.copy(alpha = 0.10f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Groups,
                                contentDescription = null,
                                tint = BrandBlue,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = if (personais.isEmpty()) {
                                "Nenhum personal disponível no momento"
                            } else {
                                "Nenhum personal encontrado"
                            },
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Ink
                        )
                        Text(
                            text = if (personais.isEmpty()) {
                                "Assim que novos profissionais forem aprovados, eles aparecerão aqui."
                            } else {
                                "Tente outra busca ou selecione outra especialidade."
                            },
                            fontSize = 12.sp,
                            color = Muted,
                            lineHeight = 18.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            } else {
                filtrados.forEach { personal ->
                    PersonalCard(
                        personal = personal,
                        selecionado = personal.usuarioId == selecionadoId,
                        combinaComFoco = combinaComFoco(personal, foco),
                        foco = foco,
                        onEscolher = { pendente = personal }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    pendente?.let { personal ->
        AlertDialog(
            onDismissRequest = { pendente = null },
            title = {
                Text(
                    text = "Escolher ${personal.nome}?",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Esse será seu personal trainer no FitUnity. " +
                        "Você poderá trocar de profissional depois.",
                    fontSize = 13.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        pendente = null
                        escolher(personal)
                    }
                ) {
                    Text(text = "Confirmar", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { pendente = null }) {
                    Text(text = "Cancelar")
                }
            }
        )
    }
}

private fun combinaComFoco(personal: CandidaturaPersonal, foco: String?): Boolean {
    val alvo = foco?.trim()?.lowercase().orEmpty()
    if (alvo.isEmpty()) return false
    return personal.especialidades.any { it.lowercase().contains(alvo) } ||
        personal.apresentacao.lowercase().contains(alvo)
}

@Composable
private fun PersonalCard(
    personal: CandidaturaPersonal,
    selecionado: Boolean,
    combinaComFoco: Boolean,
    foco: String?,
    onEscolher: () -> Unit
) {
    val iniciais = personal.nome.split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercase() }
    val borda = when {
        selecionado -> Mint
        combinaComFoco -> BrandBlue.copy(alpha = 0.55f)
        else -> SoftBlueBorder.copy(alpha = 0.55f)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .revealShadow(7.dp, RoundedCornerShape(22.dp), clip = false)
            .clip(RoundedCornerShape(22.dp))
            .background(SurfaceWhite)
            .border(if (selecionado) 2.dp else 1.dp, borda, RoundedCornerShape(22.dp))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            ProfilePhoto(
                fotoUri = personal.fotoUri,
                iniciais = iniciais.ifBlank { "PT" },
                tamanho = 52.dp,
                corInicial = BrandDark,
                corFinal = BrandBlue
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = personal.nome,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Ink,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Icon(
                        imageVector = Icons.Filled.Verified,
                        contentDescription = "Personal aprovado",
                        tint = BrandBlue,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Text(
                    text = "CREF ${personal.cref} · ${personal.experiencia} de experiência",
                    fontSize = 11.sp,
                    color = Muted,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            if (selecionado) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(MintBackground)
                        .padding(horizontal = 9.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = "Atual",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Mint
                    )
                }
            }
        }

        if (combinaComFoco) {
            Row(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(GoldBackground)
                    .padding(horizontal = 9.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = GoldText,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "Combina com seu foco: ${foco?.trim()}",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldText
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

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

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = personal.apresentacao,
            fontSize = 12.sp,
            lineHeight = 18.sp,
            color = Muted,
            maxLines = 4,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(14.dp))

        if (selecionado) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(13.dp))
                    .background(MintBackground)
                    .padding(horizontal = 12.dp, vertical = 11.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = Mint,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(7.dp))
                Text(
                    text = "Este é o seu personal trainer",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Mint
                )
            }
        } else {
            Button(
                onClick = onEscolher,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(13.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BrandBlue,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Escolher este personal",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
