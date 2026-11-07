package com.example.fitunity.ui.screens.auth

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitunity.R
import com.example.fitunity.data.FitUnityDbHelper
import com.example.fitunity.data.SessionManager
import com.example.fitunity.ui.components.FitUnityBrand
import com.example.fitunity.ui.theme.BrandBlue
import com.example.fitunity.ui.theme.BrandDark
import com.example.fitunity.ui.theme.Danger
import com.example.fitunity.ui.theme.Ink
import com.example.fitunity.ui.theme.Muted
import com.example.fitunity.ui.theme.SoftBlueBackground
import com.example.fitunity.ui.theme.SoftBlueBorder
import com.example.fitunity.ui.theme.SurfaceWhite

private data class OnboardingPage(
    val eyebrow: String,
    val titulo: String,
    val texto: String,
    val imagem: Int
)

private val onboardingPages = listOf(
    OnboardingPage(
        "TREINE COM PROPÓSITO",
        "Seu treino, do seu jeito.",
        "Planos personalizados para seus objetivos, sua rotina e seu ritmo.",
        R.drawable.treino_personal
    ),
    OnboardingPage(
        "TREINOS SOB MEDIDA",
        "Evolua a cada repetição.",
        "Exercícios pensados para seu nível, com orientação clara e prática.",
        R.drawable.treino_hoje
    ),
    OnboardingPage(
        "ACOMPANHE SUA EVOLUÇÃO",
        "Resultados que motivam.",
        "Veja seu progresso, mantenha a consistência e celebre cada conquista.",
        R.drawable.corrida
    )
)

@Composable
fun SplashScreen(onComecar: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.treino_hoje),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0x22031929), Color(0xF2031929))
                    )
                )
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 42.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FitUnityBrand(textColor = Color.White, textSize = 25)
            Spacer(modifier = Modifier.height(28.dp))
            Text(
                text = "Seu melhor começa agora.",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                letterSpacing = (-0.6).sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Treino, alimentação e evolução em um só lugar.",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.75f),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(26.dp))
            Button(
                onClick = onComecar,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(13.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrandBlue)
            ) {
                Text(text = "Conhecer o FitUnity", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "Uma apresentação rápida antes de acessar sua conta.",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.6f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    var indice by remember { mutableIntStateOf(0) }
    val pagina = onboardingPages[indice]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceWhite)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.1f)
        ) {
            Image(
                painter = painterResource(id = pagina.imagem),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color.Transparent, SurfaceWhite)
                        )
                    )
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = pagina.eyebrow,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = BrandBlue,
                letterSpacing = 1.4.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = pagina.titulo,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Ink,
                textAlign = TextAlign.Center,
                letterSpacing = (-0.6).sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = pagina.texto,
                fontSize = 13.sp,
                color = Muted,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                onboardingPages.indices.forEach { i ->
                    Box(
                        modifier = Modifier
                            .height(6.dp)
                            .width(if (i == indice) 22.dp else 6.dp)
                            .clip(CircleShape)
                            .background(if (i == indice) BrandBlue else SoftBlueBorder)
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    if (indice < onboardingPages.lastIndex) indice++ else onFinish()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(13.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrandBlue)
            ) {
                Text(
                    text = if (indice == onboardingPages.lastIndex) "Acessar minha conta" else "Próximo",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun LoginScreen(
    erro: String? = null,
    onEntrarClick: (String, String) -> Unit = { _, _ -> },
    onEsqueceuSenhaClick: () -> Unit = {},
    onCriarContaClick: () -> Unit = {},
    onVoltarClick: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }

    AuthScaffold {
        BackButton(onClick = onVoltarClick)
        Spacer(modifier = Modifier.height(26.dp))
        FitUnityBrand()
        Spacer(modifier = Modifier.height(44.dp))
        AuthHeading(
            eyebrow = "BEM-VINDO DE VOLTA",
            titulo = "Entre na sua conta",
            texto = "Continue sua jornada de evolução."
        )
        Spacer(modifier = Modifier.height(24.dp))
        AuthTextField(
            value = email,
            onValueChange = { email = it },
            label = "E-mail",
            placeholder = "seuemail@exemplo.com",
            keyboardType = KeyboardType.Email
        )
        Spacer(modifier = Modifier.height(14.dp))
        AuthTextField(
            value = senha,
            onValueChange = { senha = it },
            label = "Senha",
            placeholder = "Mínimo de 8 caracteres",
            isPassword = true
        )
        Text(
            text = "Esqueci minha senha",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = BrandDark,
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 10.dp)
                .clickable { onEsqueceuSenhaClick() }
        )
        if (erro != null) {
            Text(
                text = erro,
                fontSize = 13.sp,
                color = Danger,
                modifier = Modifier.padding(top = 10.dp)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        EntryPrimaryButton(texto = "Entrar", onClick = { onEntrarClick(email, senha) })
        Spacer(modifier = Modifier.height(22.dp))
        AuthSwitch(
            pergunta = "Ainda não tem uma conta?",
            acao = "Cadastre-se",
            onClick = onCriarContaClick
        )
    }
}

@Composable
fun CadastroScreen(
    erro: String? = null,
    onCadastrarClick: (nome: String, email: String, senha: String) -> Unit = { _, _, _ -> },
    onJaTenhoContaClick: () -> Unit = {},
    onVoltarClick: () -> Unit = {}
) {
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var erroLocal by remember { mutableStateOf<String?>(null) }
    val mensagemErro = erroLocal ?: erro

    AuthScaffold {
        BackButton(onClick = onVoltarClick)
        Spacer(modifier = Modifier.height(26.dp))
        FitUnityBrand()
        Spacer(modifier = Modifier.height(44.dp))
        AuthHeading(
            eyebrow = "BEM-VINDO AO FITUNITY",
            titulo = "Crie sua conta",
            texto = "Leva menos de um minuto para começar."
        )
        Spacer(modifier = Modifier.height(24.dp))
        AuthTextField(
            value = nome,
            onValueChange = { nome = it },
            label = "Nome completo",
            placeholder = "Como podemos chamar você?"
        )
        Spacer(modifier = Modifier.height(14.dp))
        AuthTextField(
            value = email,
            onValueChange = { email = it },
            label = "E-mail",
            placeholder = "seuemail@exemplo.com",
            keyboardType = KeyboardType.Email
        )
        Spacer(modifier = Modifier.height(14.dp))
        AuthTextField(
            value = senha,
            onValueChange = { senha = it },
            label = "Senha",
            placeholder = "Mínimo de 8 caracteres",
            isPassword = true
        )
        if (mensagemErro != null) {
            Text(
                text = mensagemErro,
                fontSize = 13.sp,
                color = Danger,
                modifier = Modifier.padding(top = 10.dp)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        EntryPrimaryButton(
            texto = "Criar conta",
            onClick = {
                erroLocal = when {
                    nome.isBlank() || email.isBlank() || senha.isBlank() -> "Preencha todos os campos"
                    senha.length < 6 -> "A senha deve ter no mínimo 6 caracteres"
                    else -> null
                }
                if (erroLocal == null) onCadastrarClick(nome, email, senha)
            }
        )
        Spacer(modifier = Modifier.height(22.dp))
        AuthSwitch(
            pergunta = "Já tem uma conta?",
            acao = "Entrar",
            onClick = onJaTenhoContaClick
        )
    }
}

@Composable
fun RecuperacaoScreen(
    onVoltarClick: () -> Unit = {},
    onEnviarClick: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }

    AuthScaffold {
        BackButton(onClick = onVoltarClick)
        Spacer(modifier = Modifier.height(26.dp))
        FitUnityBrand()
        Spacer(modifier = Modifier.height(44.dp))
        AuthHeading(
            eyebrow = "RECUPERAÇÃO",
            titulo = "Recuperar senha",
            texto = "Informe seu e-mail para receber as instruções de acesso."
        )
        Spacer(modifier = Modifier.height(24.dp))
        AuthTextField(
            value = email,
            onValueChange = { email = it },
            label = "E-mail",
            placeholder = "seuemail@exemplo.com",
            keyboardType = KeyboardType.Email
        )
        Spacer(modifier = Modifier.height(20.dp))
        EntryPrimaryButton(texto = "Enviar instruções", onClick = onEnviarClick)
    }
}

private data class SetupData(
    val indice: Int,
    val titulo: String,
    val texto: String,
    val opcoes: List<String>
)

private val setupPages = mapOf(
    1 to SetupData(
        1,
        "Qual é o seu objetivo?",
        "Isso nos ajuda a personalizar sua experiência.",
        listOf("Emagrecer", "Ganhar massa muscular", "Manter a saúde", "Mais disposição")
    ),
    2 to SetupData(
        2,
        "Quando você vai treinar?",
        "Escolha a frequência que cabe na sua rotina.",
        listOf("3 vezes por semana", "4 vezes por semana", "5 vezes por semana", "Todos os dias")
    ),
    3 to SetupData(
        3,
        "Alguma restrição alimentar?",
        "Você pode alterar isso quando quiser.",
        listOf("Sem restrições", "Vegetariano", "Vegano", "Sem lactose")
    )
)

@Composable
fun SetupScreen(
    passo: Int,
    onVoltar: () -> Unit,
    onContinuar: () -> Unit
) {
    val context = LocalContext.current
    val db = remember { FitUnityDbHelper(context) }
    val perfil = SessionManager.usuarioAtual.value
    val dados = setupPages[passo] ?: setupPages.getValue(1)
    var escolha by remember(passo) { mutableStateOf(dados.opcoes.first()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceWhite)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 30.dp, vertical = 30.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            BackButton(onClick = onVoltar)
            Text(text = "${dados.indice} de 3", fontSize = 12.sp, color = Muted)
        }
        Spacer(modifier = Modifier.height(17.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(SoftBlueBackground)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(dados.indice / 3f)
                    .height(4.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(BrandBlue)
            )
        }
        Spacer(modifier = Modifier.height(46.dp))
        Text(
            text = "PERSONALIZE SEU PLANO",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = BrandBlue,
            letterSpacing = 1.4.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = dados.titulo,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Ink,
            letterSpacing = (-0.6).sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = dados.texto, fontSize = 13.sp, color = Muted, lineHeight = 20.sp)
        Spacer(modifier = Modifier.height(26.dp))
        dados.opcoes.forEach { opcao ->
            val ativo = escolha == opcao
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
                    .clip(RoundedCornerShape(13.dp))
                    .background(if (ativo) Color(0xFFF0FAFF) else SurfaceWhite)
                    .border(
                        1.dp,
                        if (ativo) BrandBlue else SoftBlueBorder,
                        RoundedCornerShape(13.dp)
                    )
                    .clickable { escolha = opcao }
                    .padding(horizontal = 15.dp, vertical = 15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .border(
                            if (ativo) 5.dp else 1.5.dp,
                            if (ativo) BrandBlue else Color(0xFFAAC0CC),
                            CircleShape
                        )
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = opcao,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (ativo) BrandDark else Ink,
                    modifier = Modifier.weight(1f)
                )
                if (ativo) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null,
                        tint = BrandBlue,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(22.dp))
        EntryPrimaryButton(
            texto = "Continuar",
            onClick = {
                perfil?.let {
                    val objetivo = if (passo == 1) escolha else it.objetivoPlano
                    val frequencia = if (passo == 2) escolha else it.frequenciaTreino
                    val restricao = if (passo == 3) escolha else it.restricaoAlimentar
                    db.atualizarPreferenciasPlano(it.id, objetivo, frequencia, restricao)
                    SessionManager.login(
                        it.copy(
                            objetivoPlano = objetivo,
                            frequenciaTreino = frequencia,
                            restricaoAlimentar = restricao
                        )
                    )
                }
                onContinuar()
            }
        )
    }
}

@Composable
fun ReadyScreen(
    onIrParaInicio: () -> Unit,
    onReverPreferencias: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceWhite)
            .padding(horizontal = 30.dp, vertical = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(74.dp)
                .clip(CircleShape)
                .background(Brush.linearGradient(listOf(Color(0xFF08B7F2), Color(0xFF087BD1)))),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(34.dp)
            )
        }
        Spacer(modifier = Modifier.height(26.dp))
        Text(
            text = "TUDO PRONTO",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = BrandBlue,
            letterSpacing = 1.4.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Seu plano está pronto!",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Ink,
            textAlign = TextAlign.Center,
            letterSpacing = (-0.6).sp
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Preparamos uma experiência sob medida para seus objetivos e sua rotina.",
            fontSize = 13.sp,
            color = Muted,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )
        Spacer(modifier = Modifier.height(24.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFF7FAFC))
                .border(1.dp, SoftBlueBorder, RoundedCornerShape(16.dp))
        ) {
            val perfil = SessionManager.usuarioAtual.value
            ReadySummaryRow("Objetivo", perfil?.objetivoPlano ?: "Manter a saúde")
            ReadySummaryRow("Frequência", perfil?.frequenciaTreino ?: "4 vezes por semana")
            ReadySummaryRow("Alimentação", perfil?.restricaoAlimentar ?: "Sem restrições", ultima = true)
        }
        Spacer(modifier = Modifier.height(24.dp))
        EntryPrimaryButton(texto = "Ir para o início", onClick = onIrParaInicio)
        Text(
            text = "Rever preferências",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = BrandBlue,
            modifier = Modifier
                .padding(top = 18.dp)
                .clickable { onReverPreferencias() }
        )
    }
}

@Composable
private fun ReadySummaryRow(rotulo: String, valor: String, ultima: Boolean = false) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 13.dp)
    ) {
        Text(text = rotulo, fontSize = 11.sp, color = Muted)
        Text(text = valor, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Ink)
    }
    if (!ultima) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(SoftBlueBorder)
        )
    }
}

@Composable
private fun AuthScaffold(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceWhite)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 30.dp, vertical = 30.dp),
        content = content
    )
}

@Composable
private fun AuthHeading(eyebrow: String, titulo: String, texto: String) {
    Text(
        text = eyebrow,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = BrandBlue,
        letterSpacing = 1.4.sp
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = titulo,
        fontSize = 26.sp,
        fontWeight = FontWeight.Bold,
        color = Ink,
        letterSpacing = (-0.6).sp
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(text = texto, fontSize = 13.sp, color = Muted, lineHeight = 20.sp)
}

@Composable
private fun AuthSwitch(pergunta: String, acao: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = pergunta + " ", fontSize = 13.sp, color = Muted)
        Text(
            text = acao,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = BrandBlue,
            modifier = Modifier.clickable { onClick() }
        )
    }
}

@Composable
private fun BackButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(11.dp))
            .background(Color(0xFFF1F7FA))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = "Voltar",
            tint = Ink,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun EntryPrimaryButton(texto: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(13.dp),
        colors = ButtonDefaults.buttonColors(containerColor = BrandBlue)
    ) {
        Text(text = texto, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false
) {
    var senhaVisivel by remember { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Ink,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(text = placeholder, fontSize = 13.sp, color = Muted) },
            singleLine = true,
            visualTransformation = if (isPassword && !senhaVisivel) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = if (isPassword) KeyboardType.Password else keyboardType
            ),
            trailingIcon = if (isPassword) {
                {
                    IconButton(onClick = { senhaVisivel = !senhaVisivel }) {
                        Icon(
                            imageVector = if (senhaVisivel) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = if (senhaVisivel) "Ocultar senha" else "Mostrar senha",
                            tint = Muted,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            } else {
                null
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            textStyle = TextStyle(fontSize = 13.sp, color = Ink),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BrandBlue,
                unfocusedBorderColor = SoftBlueBorder,
                focusedContainerColor = SurfaceWhite,
                unfocusedContainerColor = Color(0xFFFBFDFE),
                cursorColor = BrandBlue
            )
        )
    }
}
