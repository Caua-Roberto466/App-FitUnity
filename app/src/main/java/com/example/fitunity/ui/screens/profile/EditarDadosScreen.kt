package com.example.fitunity.ui.screens.profile

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fitunity.data.FitUnityDbHelper
import com.example.fitunity.data.FotoPerfil
import com.example.fitunity.data.SessionManager
import com.example.fitunity.ui.components.AppTextField
import com.example.fitunity.ui.components.Avatar
import com.example.fitunity.ui.components.DetailHeader
import com.example.fitunity.ui.components.PrimaryActionButton
import com.example.fitunity.ui.components.RevealOnAppear
import com.example.fitunity.ui.components.SectionHeading
import com.example.fitunity.ui.components.revealShadow
import com.example.fitunity.ui.theme.BrandBlue
import com.example.fitunity.ui.theme.BrandDark
import com.example.fitunity.ui.theme.Coral
import com.example.fitunity.ui.theme.CoralBackground
import com.example.fitunity.ui.theme.GoldBackground
import com.example.fitunity.ui.theme.GoldText
import com.example.fitunity.ui.theme.Ink
import com.example.fitunity.ui.theme.Mint
import com.example.fitunity.ui.theme.MintBackground
import com.example.fitunity.ui.theme.Muted
import com.example.fitunity.ui.theme.SoftBlueBackground
import com.example.fitunity.ui.theme.SoftBlueBorder
import com.example.fitunity.ui.theme.SurfaceWhite
import java.util.Locale
import kotlin.math.abs

@Composable
fun EditarDadosScreen(navController: NavController) {
    val context = LocalContext.current
    val db = remember { FitUnityDbHelper(context) }
    val perfil = SessionManager.usuarioAtual.value
    val usuarioId = perfil?.id ?: 0L
    val foto = remember(perfil?.fotoUri) { FotoPerfil.carregar(perfil?.fotoUri) }
    val seletorFoto = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        val atual = SessionManager.usuarioAtual.value
        if (uri != null && atual != null) {
            val caminho = FotoPerfil.salvar(context, uri, atual.id, atual.fotoUri)
            if (caminho != null) {
                db.atualizarFotoPerfil(atual.id, caminho)
                SessionManager.login(atual.copy(fotoUri = caminho))
            }
        }
    }
    val generos = listOf("Feminino", "Masculino", "Outro")
    var nome by remember { mutableStateOf(perfil?.nome ?: "") }
    var email by remember { mutableStateOf(perfil?.email ?: "") }
    var peso by remember { mutableStateOf(perfil?.pesoAtual?.toString()?.replace('.', ',') ?: "") }
    var altura by remember { mutableStateOf(perfil?.altura?.toString()?.replace('.', ',') ?: "1,75") }
    var genero by remember {
        mutableIntStateOf(generos.indexOf(perfil?.genero).takeIf { it >= 0 } ?: 0)
    }
    var objetivo by remember {
        mutableStateOf(perfil?.objetivoPeso?.toString()?.replace('.', ',') ?: "")
    }

    val iniciais = nome.split(" ").filter { it.isNotBlank() }.take(2)
        .joinToString("") { it.first().uppercase() }

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
                titulo = "Editar dados",
                onBack = { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.height(16.dp))

            RevealOnAppear(
                modifier = Modifier.fillMaxWidth(),
                delayMillis = 0,
                offsetY = 14.dp
            ) {
                FotoCabecalho(
                    foto = foto,
                    iniciais = iniciais.ifBlank { "FU" },
                    onAlterar = { seletorFoto.launch("image/*") }
                )
            }

            RevealOnAppear(
                modifier = Modifier.fillMaxWidth(),
                delayMillis = 60,
                offsetY = 16.dp
            ) {
                Column {
                    SectionHeading(
                        titulo = "Seus dados",
                        subtitulo = "Mantenha suas informações sempre atualizadas"
                    )
                    DadosCard {
                        SubsecaoHeader(
                            icone = Icons.Filled.Person,
                            titulo = "Informações pessoais"
                        )
                        AppTextField(
                            value = nome,
                            onValueChange = { nome = it },
                            placeholder = "Seu nome",
                            label = "Nome completo",
                            leadingIcon = Icons.Filled.Person
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        AppTextField(
                            value = email,
                            onValueChange = { email = it },
                            placeholder = "seu@email.com",
                            label = "E-mail",
                            keyboardType = KeyboardType.Email,
                            leadingIcon = Icons.Filled.Email
                        )

                        DivisorSecao()

                        SubsecaoHeader(
                            icone = Icons.Filled.MonitorWeight,
                            titulo = "Dados físicos"
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            AppTextField(
                                value = peso,
                                onValueChange = { peso = it.filter { ch -> ch.isDigit() || ch == ',' || ch == '.' } },
                                placeholder = "68,4",
                                label = "Peso (kg)",
                                modifier = Modifier.weight(1f),
                                keyboardType = KeyboardType.Decimal,
                                leadingIcon = Icons.Filled.MonitorWeight
                            )
                            AppTextField(
                                value = altura,
                                onValueChange = { altura = it.filter { ch -> ch.isDigit() || ch == ',' || ch == '.' } },
                                placeholder = "1,75",
                                label = "Altura (m)",
                                modifier = Modifier.weight(1f),
                                keyboardType = KeyboardType.Decimal,
                                leadingIcon = Icons.Filled.Height
                            )
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        ImcResumo(peso = peso, altura = altura)

                        DivisorSecao()

                        SubsecaoHeader(
                            icone = Icons.Filled.Flag,
                            titulo = "Sobre você"
                        )
                        Text(
                            text = "Gênero",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Ink
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        GeneroSelector(
                            opcoes = generos,
                            selecionado = genero,
                            onSelecionar = { genero = it }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        AppTextField(
                            value = objetivo,
                            onValueChange = { objetivo = it.filter { ch -> ch.isDigit() || ch == ',' || ch == '.' } },
                            placeholder = "65,0",
                            label = "Peso desejado (kg)",
                            keyboardType = KeyboardType.Decimal,
                            leadingIcon = Icons.Filled.Flag
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        MetaPesoResumo(peso = peso, objetivo = objetivo)

                        DivisorSecao()

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(RoundedCornerShape(11.dp))
                                    .background(MintBackground),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.CheckCircle,
                                    contentDescription = null,
                                    tint = Mint,
                                    modifier = Modifier.size(17.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = "Conta criada", fontSize = 11.sp, color = Muted)
                                Text(
                                    text = perfil?.tempoCadastrado ?: "Há 3 meses",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Ink
                                )
                            }
                        }
                    }
                }
            }

            RevealOnAppear(
                modifier = Modifier.fillMaxWidth(),
                delayMillis = 120,
                offsetY = 16.dp
            ) {
                PrimaryActionButton(
                    texto = "Salvar alterações",
                    onClick = {
                        val atual = SessionManager.usuarioAtual.value ?: perfil
                        val pesoAtual = peso.replace(',', '.').toDoubleOrNull() ?: (atual?.pesoAtual ?: 0.0)
                        val objetivoPeso = objetivo.replace(',', '.').toDoubleOrNull() ?: (atual?.objetivoPeso ?: 0.0)
                        val alturaAtual = altura.replace(',', '.').toDoubleOrNull() ?: (atual?.altura ?: 1.75)
                        db.atualizarPerfil(
                            id = atual?.id ?: usuarioId,
                            nome = nome,
                            dataNascimento = atual?.dataNascimento ?: "",
                            genero = generos[genero],
                            pesoAtual = pesoAtual,
                            objetivoPeso = objetivoPeso,
                            foco = atual?.foco ?: "Hipertrofia",
                            altura = alturaAtual,
                            email = email
                        )
                        atual?.let {
                            SessionManager.login(
                                it.copy(
                                    nome = nome,
                                    email = email,
                                    genero = generos[genero],
                                    pesoAtual = pesoAtual,
                                    objetivoPeso = objetivoPeso,
                                    altura = alturaAtual
                                )
                            )
                        }
                        navController.popBackStack()
                    }
                )
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
private fun FotoCabecalho(
    foto: Bitmap?,
    iniciais: String,
    onAlterar: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .revealShadow(12.dp, RoundedCornerShape(26.dp), clip = false)
            .clip(RoundedCornerShape(26.dp))
            .background(
                Brush.linearGradient(listOf(Color(0xFF3AA6EA), Color(0xFF0E7BB8)))
            )
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 46.dp, y = (-58).dp)
                .size(150.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.07f))
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = (-52).dp, y = 58.dp)
                .size(130.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.06f))
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clickable { onAlterar() },
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    if (foto != null) {
                        Image(
                            bitmap = foto.asImageBitmap(),
                            contentDescription = "Foto de perfil",
                            modifier = Modifier
                                .size(88.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Avatar(
                            iniciais = iniciais,
                            tamanho = 88.dp,
                            corInicial = Color(0xFF0A3F63),
                            corFinal = Color(0xFF20B7E8)
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = 2.dp, y = 2.dp)
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.CameraAlt,
                        contentDescription = "Alterar foto",
                        tint = BrandBlue,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Foto de perfil",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "Toque na câmera para atualizar",
                fontSize = 11.sp,
                color = Color.White.copy(alpha = 0.78f),
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

@Composable
private fun DadosCard(content: @Composable ColumnScope.() -> Unit) {
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
private fun GeneroSelector(
    opcoes: List<String>,
    selecionado: Int,
    onSelecionar: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        opcoes.forEachIndexed { index, opcao ->
            val ativo = index == selecionado
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(13.dp))
                    .background(if (ativo) BrandBlue else SoftBlueBackground)
                    .border(
                        width = 1.dp,
                        color = if (ativo) BrandBlue else SoftBlueBorder,
                        shape = RoundedCornerShape(13.dp)
                    )
                    .clickable { onSelecionar(index) }
                    .padding(vertical = 11.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (ativo) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
                Text(
                    text = opcao,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (ativo) Color.White else BrandDark,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
private fun ImcResumo(peso: String, altura: String) {
    val pesoValor = peso.replace(',', '.').toDoubleOrNull()
    val alturaValor = altura.replace(',', '.').toDoubleOrNull()
    val imc = if (pesoValor != null && alturaValor != null && alturaValor > 0) {
        pesoValor / (alturaValor * alturaValor)
    } else {
        null
    }

    val classificacao: String
    val cor: Color
    val fundo: Color
    when {
        imc == null -> {
            classificacao = "Preencha peso e altura"
            cor = Muted
            fundo = SoftBlueBackground
        }
        imc < 18.5 -> {
            classificacao = "Abaixo do peso"
            cor = Coral
            fundo = CoralBackground
        }
        imc < 25 -> {
            classificacao = "Peso saudável"
            cor = Mint
            fundo = MintBackground
        }
        imc < 30 -> {
            classificacao = "Sobrepeso"
            cor = GoldText
            fundo = GoldBackground
        }
        else -> {
            classificacao = "Obesidade"
            cor = Coral
            fundo = CoralBackground
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(fundo)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White.copy(alpha = 0.7f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.FitnessCenter,
                contentDescription = null,
                tint = cor,
                modifier = Modifier.size(19.dp)
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = "IMC estimado", fontSize = 11.sp, color = Muted)
            Text(
                text = imc?.let { String.format(Locale("pt", "BR"), "%.1f", it) } ?: "—",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Ink
            )
        }
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White.copy(alpha = 0.85f))
                .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Text(
                text = classificacao,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = cor
            )
        }
    }
}

@Composable
private fun MetaPesoResumo(peso: String, objetivo: String) {
    val pesoValor = peso.replace(',', '.').toDoubleOrNull()
    val objetivoValor = objetivo.replace(',', '.').toDoubleOrNull()
    if (pesoValor == null || objetivoValor == null || pesoValor <= 0) return

    val diferenca = pesoValor - objetivoValor
    val atingiu = abs(diferenca) < 0.05
    val mensagem = when {
        atingiu -> "Você atingiu sua meta!"
        diferenca > 0 -> "Faltam ${String.format(Locale("pt", "BR"), "%.1f", diferenca)} kg para sua meta"
        else -> "Você está ${String.format(Locale("pt", "BR"), "%.1f", -diferenca)} kg abaixo da meta"
    }
    val cor = if (atingiu) Mint else BrandBlue

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(SoftBlueBackground)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Flag,
                contentDescription = null,
                tint = cor,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = "Sua meta", fontSize = 11.sp, color = Muted)
            Text(
                text = mensagem,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Ink
            )
        }
        Text(
            text = "${String.format(Locale("pt", "BR"), "%.1f", objetivoValor)} kg",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = cor
        )
    }
}
