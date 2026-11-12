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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fitunity.data.FitUnityDbHelper
import com.example.fitunity.data.SessionManager
import com.example.fitunity.ui.components.AppTextField
import com.example.fitunity.ui.components.DetailHeader
import com.example.fitunity.ui.components.PopInOnAppear
import com.example.fitunity.ui.components.RevealOnAppear
import com.example.fitunity.ui.components.SectionHeading
import com.example.fitunity.ui.components.SegmentedControl
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val METODO_PIX = 0
private const val METODO_CARTAO = 1
private const val METODO_BOLETO = 2

@Composable
fun PagamentoScreen(
    navController: NavController,
    planoNome: String,
    periodo: String
) {
    val context = LocalContext.current
    val db = remember { FitUnityDbHelper(context) }
    val clipboard = LocalClipboardManager.current
    val escopo = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val plano = planos.firstOrNull { it.nome == planoNome && it.nome != "Gratuito" }
        ?: planos.first { it.nome != "Gratuito" }
    val usuarioId = SessionManager.usuarioAtual.value?.id ?: 0L

    var modoAnual by remember { mutableStateOf(periodo.equals("anual", ignoreCase = true)) }
    var metodo by remember { mutableIntStateOf(METODO_CARTAO) }
    var numeroCartao by remember { mutableStateOf("") }
    var nomeCartao by remember { mutableStateOf("") }
    var validade by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var erro by remember { mutableStateOf<String?>(null) }
    var processando by remember { mutableStateOf(false) }
    var sucesso by remember { mutableStateOf(false) }

    val preco = if (modoAnual) plano.precoAnual else plano.precoMensal
    val periodoTexto = if (modoAnual) "Anual" else "Mensal"
    val pixCode = remember(plano.nome) {
        "00020126580014BR.GOV.BCB.PIX0136fitunity-${plano.nome.lowercase()}-" +
            "5204000053039865406${plano.precoAnual.length}5802BR"
    }

    fun validarCartao(): String? {
        if (numeroCartao.length < 13) return "Informe um número de cartão válido."
        if (nomeCartao.trim().length < 3) return "Informe o nome impresso no cartão."
        if (validade.length != 4) return "Informe a validade no formato MM/AA."
        val mes = validade.take(2).toIntOrNull() ?: 0
        if (mes !in 1..12) return "Mês de validade inválido."
        if (cvv.length < 3) return "Informe o CVV do cartão."
        return null
    }

    fun confirmarPagamento() {
        if (processando) return
        if (metodo == METODO_CARTAO) {
            val problema = validarCartao()
            if (problema != null) {
                erro = problema
                return
            }
        }
        erro = null
        processando = true
        escopo.launch {
            delay(1500)
            if (db.atualizarTipoPlano(usuarioId, plano.nome)) {
                db.buscarPerfilPorId(usuarioId)?.let { SessionManager.login(it, context) }
                processando = false
                sucesso = true
            } else {
                processando = false
                snackbarHostState.showSnackbar("Não foi possível concluir o pagamento.")
            }
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
                titulo = if (sucesso) "Pagamento aprovado" else "Pagamento",
                onBack = { navController.popBackStack() }
            )

            if (sucesso) {
                PagamentoSucesso(
                    plano = plano,
                    modoAnual = modoAnual,
                    onVoltarPlanos = { navController.popBackStack() },
                    onIrParaInicio = {
                        navController.navigate(Rotas.HOME) {
                            popUpTo(navController.graph.id) { inclusive = true }
                        }
                    }
                )
                return@Column
            }

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
                                text = "Checkout seguro",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = BrandDark
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Finalize sua assinatura",
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold,
                        color = Ink
                    )
                    Text(
                        text = "Confirme o plano e escolha a forma de pagamento que preferir.",
                        fontSize = 13.sp,
                        color = Muted,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }
            }

            RevealOnAppear(
                modifier = Modifier.fillMaxWidth(),
                delayMillis = 80,
                offsetY = 18.dp,
                scaleFrom = 0.98f
            ) {
                ResumoPlanoCard(
                    plano = plano,
                    modoAnual = modoAnual,
                    preco = preco,
                    periodoTexto = periodoTexto
                )
            }

            RevealOnAppear(
                modifier = Modifier.fillMaxWidth(),
                delayMillis = 140,
                offsetY = 16.dp
            ) {
                Column {
                    SectionHeading(titulo = "Período de cobrança")
                    SegmentedControl(
                        opcoes = listOf("Mensal", "Anual -20%"),
                        selecionado = if (modoAnual) 1 else 0,
                        onSelecionar = { modoAnual = it == 1 }
                    )
                }
            }

            RevealOnAppear(
                modifier = Modifier.fillMaxWidth(),
                delayMillis = 200,
                offsetY = 16.dp
            ) {
                Column {
                    SectionHeading(titulo = "Forma de pagamento")
                    MetodoPagamentoCard(
                        icone = Icons.Filled.QrCode2,
                        titulo = "Pix",
                        subtitulo = "Aprovação na hora",
                        selecionado = metodo == METODO_PIX,
                        onClick = { metodo = METODO_PIX }
                    )
                    MetodoPagamentoCard(
                        icone = Icons.Filled.CreditCard,
                        titulo = "Cartão de crédito",
                        subtitulo = "Em até 12x sem juros",
                        selecionado = metodo == METODO_CARTAO,
                        onClick = { metodo = METODO_CARTAO }
                    )
                    MetodoPagamentoCard(
                        icone = Icons.Filled.ReceiptLong,
                        titulo = "Boleto bancário",
                        subtitulo = "Vence em 3 dias úteis",
                        selecionado = metodo == METODO_BOLETO,
                        onClick = { metodo = METODO_BOLETO }
                    )
                }
            }

            RevealOnAppear(
                modifier = Modifier.fillMaxWidth(),
                delayMillis = 260,
                offsetY = 16.dp
            ) {
                Column {
                    when (metodo) {
                        METODO_PIX -> ConteudoPix(
                            codigo = pixCode,
                            valor = preco,
                            onCopiar = {
                                clipboard.setText(AnnotatedString(pixCode))
                                escopo.launch {
                                    snackbarHostState.showSnackbar("Código Pix copiado.")
                                }
                            }
                        )

                        METODO_CARTAO -> ConteudoCartao(
                            numeroExibido = numeroCartao.chunked(4).joinToString(" "),
                            onNumeroChange = { numeroCartao = it.filter(Char::isDigit).take(16) },
                            nome = nomeCartao,
                            onNomeChange = { nomeCartao = it.take(40) },
                            validadeExibida = formatarValidade(validade),
                            onValidadeChange = { validade = it.filter(Char::isDigit).take(4) },
                            cvv = cvv,
                            onCvvChange = { cvv = it.filter(Char::isDigit).take(4) },
                            erro = erro
                        )

                        else -> ConteudoBoleto(valor = preco)
                    }
                }
            }

            RevealOnAppear(
                modifier = Modifier.fillMaxWidth(),
                delayMillis = 320,
                offsetY = 16.dp
            ) {
                Column {
                    SectionHeading(titulo = "Resumo do pedido")
                    ResumoPagamentoCard(
                        plano = plano,
                        modoAnual = modoAnual,
                        preco = preco
                    )
                }
            }

            RevealOnAppear(
                modifier = Modifier.fillMaxWidth(),
                delayMillis = 380,
                offsetY = 14.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 14.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(SoftBlueBackground)
                        .padding(13.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = null,
                        tint = BrandDark,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(9.dp))
                    Text(
                        text = "Pagamento 100% seguro. Seus dados não são armazenados.",
                        fontSize = 11.sp,
                        color = BrandDark
                    )
                }
            }

            RevealOnAppear(
                modifier = Modifier.fillMaxWidth(),
                delayMillis = 430,
                offsetY = 16.dp
            ) {
                BotaoConfirmar(
                    texto = "Confirmar pagamento • $preco",
                    processando = processando,
                    onClick = { confirmarPagamento() }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            RevealOnAppear(
                modifier = Modifier.fillMaxWidth(),
                delayMillis = 470,
                offsetY = 12.dp
            ) {
                Text(
                    text = "Ao confirmar, você concorda com os Termos de Uso e a Política de " +
                        "Privacidade da FitUnity. Você pode cancelar quando quiser.",
                    fontSize = 10.sp,
                    color = Muted,
                    lineHeight = 14.sp,
                    modifier = Modifier.padding(top = 6.dp, bottom = 28.dp)
                )
            }
        }
    }
}

@Composable
private fun ResumoPlanoCard(
    plano: PlanoInfo,
    modoAnual: Boolean,
    preco: String,
    periodoTexto: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 14.dp)
            .revealShadow(8.dp, RoundedCornerShape(20.dp), clip = false)
            .clip(RoundedCornerShape(20.dp))
            .background(SurfaceWhite)
            .border(1.dp, SoftBlueBorder, RoundedCornerShape(20.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(SoftBlueBackground),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = BrandBlue,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Plano ${plano.nome}",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Ink
            )
            Text(
                text = "$periodoTexto • renova automaticamente",
                fontSize = 11.sp,
                color = Muted,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = preco,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = BrandDark
            )
            Text(
                text = if (modoAnual) "/ano" else "/mês",
                fontSize = 11.sp,
                color = Muted
            )
        }
    }
}

@Composable
private fun MetodoPagamentoCard(
    icone: ImageVector,
    titulo: String,
    subtitulo: String,
    selecionado: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 9.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(if (selecionado) SoftBlueBackground else SurfaceWhite)
            .border(
                width = if (selecionado) 1.5.dp else 1.dp,
                color = if (selecionado) BrandBlue else SoftBlueBorder,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(11.dp))
                .background(if (selecionado) BrandBlue else SoftBlueBackground),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icone,
                contentDescription = null,
                tint = if (selecionado) Color.White else BrandDark,
                modifier = Modifier.size(19.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = titulo, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Ink)
            Text(text = subtitulo, fontSize = 11.sp, color = Muted)
        }
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(if (selecionado) BrandBlue else Color.Transparent)
                .border(
                    width = 1.5.dp,
                    color = if (selecionado) BrandBlue else SoftBlueBorder,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (selecionado) {
                Box(
                    modifier = Modifier
                        .size(7.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                )
            }
        }
    }
}

@Composable
private fun ConteudoPix(
    codigo: String,
    valor: String,
    onCopiar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(SurfaceWhite)
            .border(1.dp, SoftBlueBorder, RoundedCornerShape(18.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(104.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(SoftBlueBackground),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.QrCode2,
                contentDescription = null,
                tint = BrandDark,
                modifier = Modifier.size(64.dp)
            )
        }
        Text(
            text = "Escaneie o QR Code no app do seu banco",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Ink,
            modifier = Modifier.padding(top = 12.dp)
        )
        Text(
            text = "ou copie o código abaixo. O valor de $valor é cobrado uma única vez por ciclo.",
            fontSize = 11.sp,
            color = Muted,
            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(SoftBlueBackground)
                .padding(horizontal = 12.dp, vertical = 11.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = codigo,
                fontSize = 10.sp,
                color = Muted,
                maxLines = 1,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(9.dp))
                    .background(BrandBlue)
                    .clickable { onCopiar() }
                    .padding(horizontal = 10.dp, vertical = 7.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.ContentCopy,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Copiar",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun ConteudoCartao(
    numeroExibido: String,
    onNumeroChange: (String) -> Unit,
    nome: String,
    onNomeChange: (String) -> Unit,
    validadeExibida: String,
    onValidadeChange: (String) -> Unit,
    cvv: String,
    onCvvChange: (String) -> Unit,
    erro: String?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(SurfaceWhite)
            .border(1.dp, SoftBlueBorder, RoundedCornerShape(18.dp))
            .padding(16.dp)
    ) {
        AppTextField(
            value = numeroExibido,
            onValueChange = onNumeroChange,
            placeholder = "0000 0000 0000 0000",
            label = "Número do cartão",
            keyboardType = KeyboardType.Number
        )
        Spacer(modifier = Modifier.height(10.dp))
        AppTextField(
            value = nome,
            onValueChange = onNomeChange,
            placeholder = "Como está impresso no cartão",
            label = "Nome no cartão"
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            AppTextField(
                value = validadeExibida,
                onValueChange = onValidadeChange,
                placeholder = "MM/AA",
                label = "Validade",
                modifier = Modifier.weight(1f),
                keyboardType = KeyboardType.Number
            )
            AppTextField(
                value = cvv,
                onValueChange = onCvvChange,
                placeholder = "000",
                label = "CVV",
                modifier = Modifier.weight(1f),
                keyboardType = KeyboardType.Number
            )
        }
        if (erro != null) {
            Text(
                text = erro,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = com.example.fitunity.ui.theme.Danger,
                modifier = Modifier.padding(top = 10.dp)
            )
        }
        Text(
            text = "Você pode parcelar em até 12x sem juros.",
            fontSize = 11.sp,
            color = Muted,
            modifier = Modifier.padding(top = 10.dp)
        )
    }
}

@Composable
private fun ConteudoBoleto(valor: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(SurfaceWhite)
            .border(1.dp, SoftBlueBorder, RoundedCornerShape(18.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(11.dp))
                .background(SoftBlueBackground),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.ReceiptLong,
                contentDescription = null,
                tint = BrandDark,
                modifier = Modifier.size(19.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Boleto de $valor",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Ink
            )
            Text(
                text = "O boleto será gerado após a confirmação e vence em 3 dias úteis. " +
                    "A assinatura é ativada assim que o pagamento é identificado.",
                fontSize = 11.sp,
                color = Muted,
                lineHeight = 16.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
private fun ResumoPagamentoCard(
    plano: PlanoInfo,
    modoAnual: Boolean,
    preco: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 2.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(SurfaceWhite)
            .border(1.dp, SoftBlueBorder, RoundedCornerShape(18.dp))
            .padding(16.dp)
    ) {
        ResumoLinha(
            rotulo = "Plano ${plano.nome} (${if (modoAnual) "anual" else "mensal"})",
            valor = preco,
            destaque = false
        )
        if (modoAnual) {
            ResumoLinha(
                rotulo = "Desconto anual",
                valor = "20% aplicado",
                destaque = true
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(SoftBlueBorder)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Total hoje",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Ink
                )
                Text(
                    text = if (modoAnual) "Cobrança anual" else "Cobrança mensal",
                    fontSize = 10.sp,
                    color = Muted
                )
            }
            Text(
                text = preco,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = BrandDark
            )
        }
    }
}

@Composable
private fun ResumoLinha(
    rotulo: String,
    valor: String,
    destaque: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = rotulo,
            fontSize = 12.sp,
            color = Muted,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = valor,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (destaque) Mint else Ink
        )
    }
}

@Composable
private fun BotaoConfirmar(
    texto: String,
    processando: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 18.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(if (processando) BrandBlue.copy(alpha = 0.65f) else BrandBlue)
            .clickable(enabled = !processando) { onClick() }
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (processando) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(9.dp))
                Text(
                    text = "Processando...",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Lock,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = texto,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun PagamentoSucesso(
    plano: PlanoInfo,
    modoAnual: Boolean,
    onVoltarPlanos: () -> Unit,
    onIrParaInicio: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        PopInOnAppear {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(Mint.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = Mint,
                    modifier = Modifier.size(58.dp)
                )
            }
        }

        Text(
            text = "Pagamento confirmado!",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Ink,
            modifier = Modifier.padding(top = 20.dp)
        )
        Text(
            text = "Seu plano ${plano.nome} está ativo. Aproveite todos os benefícios " +
                "a partir de agora.",
            fontSize = 13.sp,
            color = Muted,
            lineHeight = 19.sp,
            modifier = Modifier.padding(top = 8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 22.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(SurfaceWhite)
                .border(1.dp, SoftBlueBorder, RoundedCornerShape(20.dp))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(SoftBlueBackground),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    tint = BrandBlue,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Plano ${plano.nome}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Ink
                )
                Text(
                    text = if (modoAnual) "Cobrança anual" else "Cobrança mensal",
                    fontSize = 11.sp,
                    color = Muted
                )
            }
            Text(
                text = if (modoAnual) plano.precoAnual else plano.precoMensal,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = BrandDark
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 22.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(BrandBlue)
                .clickable { onIrParaInicio() }
                .padding(vertical = 15.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Começar a usar",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(SurfaceWhite)
                .border(1.5.dp, BrandBlue, RoundedCornerShape(15.dp))
                .clickable { onVoltarPlanos() }
                .padding(vertical = 15.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Voltar aos planos",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = BrandDark
            )
        }

        Spacer(modifier = Modifier.height(28.dp))
    }
}

private fun formatarValidade(digitos: String): String {
    val limpo = digitos.filter(Char::isDigit).take(4)
    return if (limpo.length >= 3) {
        "${limpo.substring(0, 2)}/${limpo.substring(2)}"
    } else {
        limpo
    }
}
