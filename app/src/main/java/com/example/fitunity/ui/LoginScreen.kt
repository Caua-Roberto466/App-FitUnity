package com.example.fitunity.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitunity.R

// onEntrarClick recebe (email, senha) -> validar/autenticar e navegar para a tela principal
// onEsqueceuSenhaClick -> deve levar para a tela de recuperação de senha
// onCriarContaClick -> deve levar de volta para a tela de CADASTRO
// erro -> mensagem a ser exibida (ex.: "e-mail ou senha incorretos"); null quando não há erro
//
// Exemplo de uso com Navigation Compose:
//
// LoginScreen(
//     erro = erro,
//     onEntrarClick = { email, senha -> viewModel.login(email, senha) },
//     onEsqueceuSenhaClick = { navController.navigate("recuperar_senha") },
//     onCriarContaClick = { navController.navigate("cadastro") }
// )
@Composable
fun LoginScreen(
    erro: String? = null,
    onEntrarClick: (String, String) -> Unit = { _, _ -> },
    onEsqueceuSenhaClick: () -> Unit = {},
    onCriarContaClick: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var senhaVisivel by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(64.dp))

            // Logo
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.ic_fitunity_logo),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "FitUnity", color = FitUnityBlue, fontSize = 22.sp, fontWeight = FontWeight.Medium)
            }

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Bem-vindo de volta",
                color = FitUnityBlue,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Entre para continuar sua jornada",
                fontSize = 15.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Campo E-mail
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("E-mail") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = FitUnityBlue,
                    focusedLabelColor = FitUnityBlue,
                    cursorColor = FitUnityBlue
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Senha
            OutlinedTextField(
                value = senha,
                onValueChange = { senha = it },
                label = { Text("Senha") },
                singleLine = true,
                visualTransformation = if (senhaVisivel) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { senhaVisivel = !senhaVisivel }) {
                        Icon(
                            imageVector = if (senhaVisivel) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = if (senhaVisivel) "Ocultar senha" else "Mostrar senha"
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = FitUnityBlue,
                    focusedLabelColor = FitUnityBlue,
                    cursorColor = FitUnityBlue
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Esqueceu a senha
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "Esqueceu a senha?",
                    color = FitUnityBlue,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable { onEsqueceuSenhaClick() }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            if (erro != null) {
                Text(
                    text = erro,
                    color = Color.Red,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Botão Entrar
            Button(
                onClick = { onEntrarClick(email, senha) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = FitUnityBlue)
            ) {
                Text(text = "Entrar", fontSize = 18.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.weight(1f))

            // Link para criar conta
            Row(
                modifier = Modifier.padding(bottom = 32.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Ainda não tem conta? ", fontSize = 14.sp, color = Color.Black)
                Text(
                    text = "Cadastre-se",
                    fontSize = 14.sp,
                    color = FitUnityBlue,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { onCriarContaClick() }
                )
            }
        }
    }
}
