package com.example.fitunity.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitunity.R

// onCadastrarClick recebe os dados preenchidos e deve chamar, por exemplo:
// FitUnityDbHelper(context).cadastrarUsuario(email, nome, dataNascimento, genero, senha)
//
// onJaTenhoContaClick -> deve navegar de volta para a tela de login
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CadastroScreen(
    onCadastrarClick: (nome: String, email: String, dataNascimento: String, genero: String, senha: String) -> Unit = { _, _, _, _, _ -> },
    onEntrarClick: () -> Unit = {}
) {
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var dataNascimento by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var confirmarSenha by remember { mutableStateOf("") }

    var generoExpanded by remember { mutableStateOf(false) }
    val generos = listOf("Masculino", "Feminino", "Outro")

    var erro by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = { CadastroTopBar() },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            // Banner de boas-vindas (mesmo padrão da tela de login)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(FitUnityBlue, FitUnityBlue.copy(alpha = 0.6f))
                        )
                    )
                    .padding(20.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Column {
                    Text(
                        text = "Crie sua conta",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Monte treinos e dietas personalizados\npara alcançar seus objetivos",
                        color = Color.White,
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            CadastroField(
                label = "Nome completo",
                value = nome,
                onValueChange = { nome = it },
                placeholder = "Digite seu nome"
            )

            Spacer(modifier = Modifier.height(16.dp))

            CadastroField(
                label = "Email",
                value = email,
                onValueChange = { email = it },
                placeholder = "seuemail@exemplo.com",
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(16.dp))

            CadastroField(
                label = "Data de nascimento",
                value = dataNascimento,
                onValueChange = { novoValor ->
                    // Mantém só os números digitados (máx. 8: dd mm aaaa) e deixa
                    // a formatação visual com as barras por conta do VisualTransformation
                    val apenasDigitos = novoValor.filter { it.isDigit() }
                    if (apenasDigitos.length <= 8) {
                        dataNascimento = apenasDigitos
                    }
                },
                placeholder = "dd/mm/aaaa",
                keyboardType = KeyboardType.Number,
                visualTransformation = DataNascimentoVisualTransformation()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Seletor de gênero
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = "Gênero", fontSize = 14.sp, color = Color.Black, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(6.dp))
                ExposedDropdownMenuBox(
                    expanded = generoExpanded,
                    onExpandedChange = { generoExpanded = it }
                ) {
                    OutlinedTextField(
                        value = genero,
                        onValueChange = {},
                        readOnly = true,
                        placeholder = { Text("Selecione", color = Color.Gray) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FitUnityBlue,
                            unfocusedBorderColor = Color.LightGray,
                            focusedContainerColor = Color(0xFFF2F2F2),
                            unfocusedContainerColor = Color(0xFFF2F2F2),
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            cursorColor = FitUnityBlue
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = generoExpanded,
                        onDismissRequest = { generoExpanded = false }
                    ) {
                        generos.forEach { opcao ->
                            DropdownMenuItem(
                                text = { Text(opcao) },
                                onClick = {
                                    genero = opcao
                                    generoExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            CadastroField(
                label = "Senha",
                value = senha,
                onValueChange = { senha = it },
                placeholder = "Crie uma senha",
                isPassword = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            CadastroField(
                label = "Confirmar senha",
                value = confirmarSenha,
                onValueChange = { confirmarSenha = it },
                placeholder = "Repita a senha",
                isPassword = true,
                imeAction = ImeAction.Done
            )

            erro?.let {
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = it, color = Color.Red, fontSize = 13.sp, textAlign = TextAlign.Center)
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Botão Cadastrar
            Button(
                onClick = {
                    erro = when {
                        nome.isBlank() || email.isBlank() || dataNascimento.isBlank() ||
                                genero.isBlank() || senha.isBlank() -> "Preencha todos os campos"
                        senha != confirmarSenha -> "As senhas não coincidem"
                        senha.length < 6 -> "A senha deve ter no mínimo 6 caracteres"
                        else -> null
                    }
                    if (erro == null) {
                        onCadastrarClick(nome, email, dataNascimento, genero, senha)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = FitUnityBlue)
            ) {
                Text(text = "Cadastrar", fontSize = 18.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Logo rodapé
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.ic_fitunity_logo),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "FitUnity", color = FitUnityBlue, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Link para login
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Já tem uma conta? ", color = Color.Black, fontSize = 14.sp)
                Text(
                    text = "Entrar",
                    color = FitUnityBlue,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { onEntrarClick() }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun CadastroTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp)
            .padding(top = 32.dp, bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_fitunity_logo),
                contentDescription = null,
                modifier = Modifier.size(26.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "FitUnity", color = FitUnityBlue, fontSize = 15.sp, fontWeight = FontWeight.Medium)
        }

        Text(
            text = "Cadastro",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )

        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.End
        ) {
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = null,
                tint = FitUnityBlue,
                modifier = Modifier.size(22.dp)
            )
        }
    }
    HorizontalDivider(color = FitUnityBlue.copy(alpha = 0.3f), thickness = 1.dp)
}

@Composable
private fun CadastroField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    visualTransformation: VisualTransformation? = null
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, fontSize = 14.sp, color = Color.Black, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = Color.Gray) },
            singleLine = true,
            visualTransformation = visualTransformation
                ?: if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(
                keyboardType = if (isPassword) KeyboardType.Password else keyboardType,
                imeAction = imeAction
            ),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = FitUnityBlue,
                unfocusedBorderColor = Color.LightGray,
                focusedContainerColor = Color(0xFFF2F2F2),
                unfocusedContainerColor = Color(0xFFF2F2F2),
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                cursorColor = FitUnityBlue
            )
        )
    }
}

// Formata a digitação da data de nascimento inserindo as barras automaticamente: dd/mm/aaaa
private class DataNascimentoVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val digitos = if (text.text.length > 8) text.text.substring(0, 8) else text.text

        val textoFormatado = buildString {
            for (i in digitos.indices) {
                append(digitos[i])
                if (i == 1 || i == 3) append('/')
            }
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return when {
                    offset <= 1 -> offset
                    offset <= 3 -> offset + 1
                    else -> offset + 2
                }
            }

            override fun transformedToOriginal(offset: Int): Int {
                return when {
                    offset <= 2 -> offset
                    offset <= 5 -> offset - 1
                    else -> offset - 2
                }.coerceIn(0, digitos.length)
            }
        }

        return TransformedText(AnnotatedString(textoFormatado), offsetMapping)
    }
}