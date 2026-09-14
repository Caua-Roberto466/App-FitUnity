package com.example.fitunity.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitunity.data.PerfilCliente


data class DadosEditaveis(
    val nome: String,
    val dataNascimento: String,
    val genero: String,
    val pesoAtual: String,
    val objetivoPeso: String,
    val foco: String
)

// onVoltarClick  -> volta para o Perfil sem salvar
// onSalvarClick  -> recebe os dados atualizados; quem chama essa tela decide como
//                   persistir (ex.: via FitUnityDbHelper) e quando voltar
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarDadosScreen(
    perfil: PerfilCliente,
    erro: String? = null,
    onVoltarClick: () -> Unit = {},
    onSalvarClick: (DadosEditaveis) -> Unit = {}
) {
    var nome by remember { mutableStateOf(perfil.nome) }
    var dataNascimento by remember { mutableStateOf(perfil.dataNascimento) }
    var genero by remember { mutableStateOf(perfil.genero) }
    var pesoAtual by remember { mutableStateOf(perfil.pesoAtual.toString()) }
    var objetivoPeso by remember { mutableStateOf(perfil.objetivoPeso.toString()) }
    var foco by remember { mutableStateOf(perfil.foco) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Dados", fontWeight = FontWeight.Bold, color = FitUnityBlue) },
                navigationIcon = {
                    IconButton(onClick = onVoltarClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Voltar", tint = FitUnityBlue)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = FitUnityBlue,
                    navigationIconContentColor = FitUnityBlue
                )
            )
        },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            CampoTexto(label = "Nome", value = nome, onValueChange = { nome = it })
            Spacer(modifier = Modifier.height(12.dp))
            CampoTexto(
                label = "Data de nascimento",
                value = dataNascimento,
                onValueChange = { dataNascimento = it },
                placeholder = "dd/mm/aaaa"
            )
            Spacer(modifier = Modifier.height(12.dp))
            CampoTexto(label = "Gênero", value = genero, onValueChange = { genero = it })
            Spacer(modifier = Modifier.height(12.dp))
            CampoTexto(
                label = "Peso atual (kg)",
                value = pesoAtual,
                onValueChange = { pesoAtual = it },
                teclado = KeyboardType.Number
            )
            Spacer(modifier = Modifier.height(12.dp))
            CampoTexto(
                label = "Meta de peso (kg)",
                value = objetivoPeso,
                onValueChange = { objetivoPeso = it },
                teclado = KeyboardType.Number
            )
            Spacer(modifier = Modifier.height(12.dp))
            CampoTexto(
                label = "Foco (ex.: Hipertrofia, Emagrecimento)",
                value = foco,
                onValueChange = { foco = it }
            )

            if (erro != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = erro, color = Color.Red, fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    onSalvarClick(
                        DadosEditaveis(
                            nome = nome,
                            dataNascimento = dataNascimento,
                            genero = genero,
                            pesoAtual = pesoAtual,
                            objetivoPeso = objetivoPeso,
                            foco = foco
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = FitUnityBlue)
            ) {
                Text(text = "Salvar", fontSize = 18.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun CampoTexto(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String? = null,
    teclado: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = { if (placeholder != null) Text(placeholder) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = teclado),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = FitUnityBlue,
            focusedLabelColor = FitUnityBlue,
            cursorColor = FitUnityBlue,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black
        )
    )
}