package com.example.fitunity.ui.screens.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.example.fitunity.data.FitUnityDbHelper
import com.example.fitunity.data.SessionManager
import com.example.fitunity.ui.components.DetailHeader
import com.example.fitunity.ui.components.RevealOnAppear
import com.example.fitunity.ui.components.SectionHeading
import com.example.fitunity.ui.components.SegmentedControl
import com.example.fitunity.ui.components.revealShadow
import com.example.fitunity.ui.theme.BrandBlue
import com.example.fitunity.ui.theme.BrandDark
import com.example.fitunity.ui.theme.Ink
import com.example.fitunity.ui.theme.Mint
import com.example.fitunity.ui.theme.Muted
import com.example.fitunity.ui.theme.Navy
import com.example.fitunity.ui.theme.SoftBlueBackground
import com.example.fitunity.ui.theme.SoftBlueBorder
import com.example.fitunity.ui.theme.SurfaceWhite
import kotlinx.coroutines.launch

private data class PlanoInfo(
    val nome: String,
    val descricao: String,
    val precoMensal: String,
    val periodoMensal: String = "",
    val observacaoMensal: String = "",
    val precoAnual: String,
    val periodoAnual: String = "/ano",
    val observacaoAnual: String = "",
    val textoBotao: String,
    val destaque: Boolean = false,
    val botaoSecundario: Boolean = false,
    val botaoEscuro: Boolean = false,
    val beneficios: List<String>,
    val indisponiveis: List<String> = emptyList()
)

private val planos = listOf(
    PlanoInfo(
        nome = "Gratuito",
        descricao = "Pra quem quer começar sem compromisso.",
        precoMensal = "R$0",
        precoAnual = "R$0",
        periodoAnual = "",
        textoBotao = "Começar grátis",
        botaoSecundario = true,
        beneficios = listOf(
            "Busca ilimitada de profissionais",
            "Agendamento direto pela plataforma",
            "Acesso a todos os treinos gratuitos",
            "Acompanhamento básico de metas"
        ),
        indisponiveis = listOf(
            "Desconto em sessões",
            "Agendamento prioritário"
        )
    ),
    PlanoInfo(
        nome = "Adaptado",
        descricao = "Pra quem quer um treino pensado em suas condições.",
        precoMensal = "R$29,90",
        periodoMensal = "/mês",
        observacaoMensal = "Cobrado mensalmente",
        precoAnual = "R$287,04",
        periodoAnual = "/ano",
        observacaoAnual = "Equivale a R$23,92/mês • 20% de desconto",
        textoBotao = "Assinar Adaptado",
        destaque = true,
        beneficios = listOf(
            "Tudo do plano Gratuito",
            "10% de desconto em todas as sessões",
            "Contato frequente com profissionais",
            "Treinos montados para atender todas suas condições",
            "Conteúdo exclusivo de treino e nutrição"
        ),
        indisponiveis = listOf("Consulta nutricional mensal grátis")
    ),
    PlanoInfo(
        nome = "Profissional",
        descricao = "Pra quem quer acompanhamento completo.",
        precoMensal = "R$59,90",
        periodoMensal = "/mês",
        observacaoMensal = "Cobrado mensalmente",
        precoAnual = "R$575,04",
        periodoAnual = "/ano",
        observacaoAnual = "Equivale a R$47,92/mês • 20% de desconto",
        textoBotao = "Assinar Profissional",
        botaoEscuro = true,
        beneficios = listOf(
            "Tudo do plano Adaptado",
            "20% de desconto em todas as sessões",
            "1 consulta nutricional gratuita por mês",
            "Avaliação física mensal",
            "Relatório mensal de progresso com profissional"
        )
    )
)

private val perguntasFrequentes = listOf(
    "Posso cancelar quando quiser?" to
        "Sim. Não há fidelidade — você pode cancelar a qualquer momento direto nas configurações da sua conta, sem multa.",
    "O que acontece se eu trocar de plano no meio do mês?" to
        "O valor é ajustado proporcionalmente. Se você fizer upgrade, cobramos só a diferença; se fizer downgrade, o novo valor vale a partir do próximo ciclo.",
    "Os descontos em sessões valem para todos os profissionais?" to
        "Sim, o desconto do seu plano é aplicado automaticamente em qualquer profissional parceiro da FitUnity, sem cupom.",
    "Preciso de cartão de crédito pra usar o plano gratuito?" to
        "Não. O plano Gratuito é sempre gratuito, não pede forma de pagamento e não expira."
)

fun nomePlanoExibicao(tipoPlano: String?): String = when (tipoPlano) {
    null, "", "Gratuito", "Básico" -> "Gratuito"
    "Profissional", "Premium" -> "Profissional"
    else -> "Adaptado"
}

@Composable
fun PlanosScreen(navController: NavController) {
    val context = LocalContext.current
    val db = remember { FitUnityDbHelper(context) }
    val perfil = SessionManager.usuarioAtual.value
    val usuarioId = perfil?.id ?: 0L
    val planoAtual = nomePlanoExibicao(perfil?.tipoPlano)
    var modoAnual by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val escopo = rememberCoroutineScope()

    fun selecionarPlano(nome: String) {
        if (nome == planoAtual) {
            escopo.launch { snackbarHostState.showSnackbar("Você já está no plano $nome.") }
            return
        }
        if (db.atualizarTipoPlano(usuarioId, nome)) {
            db.buscarPerfilPorId(usuarioId)?.let { SessionManager.login(it, context) }
            escopo.launch { snackbarHostState.showSnackbar("Plano $nome ativado com sucesso!") }
        } else {
            escopo.launch { snackbarHostState.showSnackbar("Não foi possível atualizar o plano.") }
        }
    }

    Scaffold(
        containerColor = Color(0xFFF8FBFD),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 22.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            DetailHeader(
                titulo = "Planos",
                onBack = { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.height(20.dp))

            RevealOnAppear(
                modifier = Modifier.fillMaxWidth(),
                delayMillis = 0,
                offsetY = 14.dp
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(SoftBlueBackground)
                            .padding(horizontal = 11.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = null,
                                tint = BrandBlue,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "Planos",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = BrandDark
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Escolha o plano ideal pra sua rotina",
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold,
                        color = Ink
                    )
                    Text(
                        text = "Comece de graça. Evolua pra mais desconto, prioridade e acompanhamento quando fizer sentido pra você.",
                        fontSize = 13.sp,
                        color = Muted,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }
            }

            RevealOnAppear(
                modifier = Modifier.fillMaxWidth(),
                delayMillis = 80,
                offsetY = 16.dp
            ) {
                Column {
                    Spacer(modifier = Modifier.height(18.dp))
                    SegmentedControl(
                        opcoes = listOf("Mensal", "Anual -20%"),
                        selecionado = if (modoAnual) 1 else 0,
                        onSelecionar = { modoAnual = it == 1 }
                    )
                    if (modoAnual) {
                        Text(
                            text = "Economize 20% pagando anualmente.",
                            fontSize = 11.sp,
                            color = Mint,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }

            val planosExibidos = if (modoAnual) planos.filter { it.nome != "Gratuito" } else planos
            planosExibidos.forEachIndexed { index, plano ->
                RevealOnAppear(
                    modifier = Modifier.fillMaxWidth(),
                    delayMillis = 160 + index * 90,
                    offsetY = 20.dp
                ) {
                    PlanoCard(
                        plano = plano,
                        modoAnual = modoAnual,
                        atual = plano.nome == planoAtual,
                        onSelecionar = { selecionarPlano(plano.nome) }
                    )
                }
            }

            RevealOnAppear(
                modifier = Modifier.fillMaxWidth(),
                delayMillis = 460,
                offsetY = 16.dp
            ) {
                SectionHeading(titulo = "Perguntas frequentes")
            }

            perguntasFrequentes.forEachIndexed { index, (pergunta, resposta) ->
                RevealOnAppear(
                    modifier = Modifier.fillMaxWidth(),
                    delayMillis = 500 + index * 60,
                    offsetY = 14.dp
                ) {
                    FaqItem(
                        pergunta = pergunta,
                        resposta = resposta,
                        abertoInicial = index == 0
                    )
                }
            }

            RevealOnAppear(
                modifier = Modifier.fillMaxWidth(),
                delayMillis = 720,
                offsetY = 18.dp
            ) {
                CtaFinalPlanos(onComecar = { selecionarPlano("Gratuito") })
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
private fun PlanoCard(
    plano: PlanoInfo,
    modoAnual: Boolean,
    atual: Boolean,
    onSelecionar: () -> Unit
) {
    val destaque = plano.destaque
    val corTexto = if (destaque) Color.White else Ink
    val corSecundaria = if (destaque) Color.White.copy(alpha = 0.78f) else Muted
    val fundo = if (destaque) {
        Modifier.background(
            Brush.linearGradient(listOf(Color(0xFF0A8FE8), Color(0xFF075F96)))
        )
    } else {
        Modifier
            .background(SurfaceWhite)
            .border(1.dp, SoftBlueBorder, RoundedCornerShape(22.dp))
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 14.dp)
            .revealShadow(
                elevation = if (destaque) 14.dp else 6.dp,
                shape = RoundedCornerShape(22.dp),
                clip = false
            )
            .clip(RoundedCornerShape(22.dp))
            .then(fundo)
            .padding(18.dp)
    ) {
        Column {
            if (destaque) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White.copy(alpha = 0.18f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Mais popular",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            Text(
                text = plano.nome,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = corTexto
            )
            Text(
                text = plano.descricao,
                fontSize = 12.sp,
                color = corSecundaria,
                modifier = Modifier.padding(top = 3.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = if (modoAnual) plano.precoAnual else plano.precoMensal,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = corTexto
                )
                val periodo = if (modoAnual) plano.periodoAnual else plano.periodoMensal
                if (periodo.isNotBlank()) {
                    Text(
                        text = periodo,
                        fontSize = 13.sp,
                        color = corSecundaria,
                        modifier = Modifier.padding(bottom = 5.dp)
                    )
                }
            }

            val observacao = if (modoAnual) plano.observacaoAnual else plano.observacaoMensal
            if (observacao.isNotBlank()) {
                Text(
                    text = observacao,
                    fontSize = 11.sp,
                    color = corSecundaria,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            PlanoBotao(
                texto = plano.textoBotao,
                destaque = destaque,
                atual = atual,
                secundario = plano.botaoSecundario,
                escuro = plano.botaoEscuro,
                onClick = onSelecionar
            )

            Spacer(modifier = Modifier.height(16.dp))

            plano.beneficios.forEach { beneficio ->
                BeneficioRow(texto = beneficio, disponivel = true, destaque = destaque)
            }
            plano.indisponiveis.forEach { beneficio ->
                BeneficioRow(texto = beneficio, disponivel = false, destaque = destaque)
            }
        }
    }
}

@Composable
private fun PlanoBotao(
    texto: String,
    destaque: Boolean,
    atual: Boolean,
    secundario: Boolean,
    escuro: Boolean,
    onClick: () -> Unit
) {
    val fundo = when {
        atual -> if (destaque) Color.White.copy(alpha = 0.18f) else SoftBlueBackground
        destaque -> Color.White
        secundario -> SurfaceWhite
        escuro -> Navy
        else -> BrandBlue
    }
    val corTexto = when {
        atual -> if (destaque) Color.White.copy(alpha = 0.8f) else BrandDark
        destaque -> BrandDark
        secundario -> BrandDark
        else -> Color.White
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 14.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(fundo)
            .then(
                if (secundario && !destaque && !atual) {
                    Modifier.border(1.5.dp, BrandBlue, RoundedCornerShape(14.dp))
                } else {
                    Modifier
                }
            )
            .clickable(enabled = !atual) { onClick() }
            .padding(vertical = 13.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (atual) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = corTexto,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
            }
            Text(
                text = if (atual) "Plano atual" else texto,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = corTexto
            )
        }
    }
}

@Composable
private fun BeneficioRow(
    texto: String,
    disponivel: Boolean,
    destaque: Boolean
) {
    val corIcone = when {
        !disponivel -> if (destaque) Color.White.copy(alpha = 0.45f) else Muted.copy(alpha = 0.5f)
        destaque -> Color(0xFFB8F0FF)
        else -> Mint
    }
    val corTexto = when {
        !disponivel -> if (destaque) Color.White.copy(alpha = 0.55f) else Muted.copy(alpha = 0.7f)
        destaque -> Color.White
        else -> Ink
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = if (disponivel) Icons.Filled.Check else Icons.Filled.Close,
            contentDescription = null,
            tint = corIcone,
            modifier = Modifier.size(15.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = texto,
            fontSize = 12.sp,
            color = corTexto,
            lineHeight = 16.sp,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun FaqItem(
    pergunta: String,
    resposta: String,
    abertoInicial: Boolean
) {
    var aberto by remember { mutableStateOf(abertoInicial) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .revealShadow(4.dp, RoundedCornerShape(16.dp), clip = false)
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceWhite)
            .clickable { aberto = !aberto }
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = pergunta,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Ink,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = if (aberto) Icons.Filled.Remove else Icons.Filled.Add,
                contentDescription = null,
                tint = BrandBlue,
                modifier = Modifier.size(18.dp)
            )
        }
        AnimatedVisibility(
            visible = aberto,
            enter = fadeIn(animationSpec = tween(180)) +
                expandVertically(animationSpec = tween(200)),
            exit = fadeOut(animationSpec = tween(120)) +
                shrinkVertically(animationSpec = tween(160))
        ) {
            Text(
                text = resposta,
                fontSize = 12.sp,
                color = Muted,
                lineHeight = 17.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun CtaFinalPlanos(onComecar: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 22.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(
                Brush.linearGradient(listOf(Color(0xFF0799ED), Color(0xFF075F96)))
            )
            .padding(20.dp)
    ) {
        Column {
            Text(
                text = "Ainda com dúvida sobre qual plano escolher?",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "Comece no Gratuito — você pode fazer upgrade a qualquer momento.",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.82f),
                modifier = Modifier.padding(top = 6.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White)
                    .clickable { onComecar() }
                    .padding(vertical = 13.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Começar no Gratuito",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandDark
                )
            }
        }
    }
}
