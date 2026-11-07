package com.example.fitunity.ui.screens.profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
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
import com.example.fitunity.ui.components.FilterChips
import com.example.fitunity.ui.components.PrimaryActionButton
import com.example.fitunity.ui.components.SectionHeading
import com.example.fitunity.ui.theme.BrandBlue
import com.example.fitunity.ui.theme.BrandDark
import com.example.fitunity.ui.theme.Ink
import com.example.fitunity.ui.theme.Muted
import com.example.fitunity.ui.theme.SurfaceWhite

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

            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "Seus dados", fontSize = 23.sp, fontWeight = FontWeight.Bold, color = Ink)
            Text(
                text = "Mantenha suas informações sempre atualizadas.",
                fontSize = 13.sp,
                color = Muted,
                modifier = Modifier.padding(top = 4.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(82.dp)
                        .clip(CircleShape)
                        .clickable { seletorFoto.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    if (foto != null) {
                        Image(
                            bitmap = foto.asImageBitmap(),
                            contentDescription = "Foto de perfil",
                            modifier = Modifier
                                .size(74.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Avatar(
                            iniciais = iniciais.ifBlank { "FU" },
                            tamanho = 74.dp
                        )
                    }
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CameraAlt,
                            contentDescription = "Alterar foto",
                            tint = BrandBlue,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
                Text(
                    text = "Alterar foto",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BrandDark,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .clickable { seletorFoto.launch("image/*") }
                )
            }

            SectionHeading(titulo = "Informações pessoais")

            AppTextField(
                value = nome,
                onValueChange = { nome = it },
                placeholder = "Seu nome",
                label = "Nome completo"
            )

            Spacer(modifier = Modifier.height(10.dp))

            AppTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = "seu@email.com",
                label = "E-mail",
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(10.dp))

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
                    keyboardType = KeyboardType.Decimal
                )
                AppTextField(
                    value = altura,
                    onValueChange = { altura = it.filter { ch -> ch.isDigit() || ch == ',' || ch == '.' } },
                    placeholder = "1,75",
                    label = "Altura (m)",
                    modifier = Modifier.weight(1f),
                    keyboardType = KeyboardType.Decimal
                )
            }

            SectionHeading(titulo = "Gênero")

            FilterChips(
                opcoes = generos,
                selecionado = genero,
                onSelecionar = { genero = it }
            )

            SectionHeading(titulo = "Objetivo de peso")

            AppTextField(
                value = objetivo,
                onValueChange = { objetivo = it.filter { ch -> ch.isDigit() || ch == ',' || ch == '.' } },
                placeholder = "65,0",
                label = "Peso desejado (kg)",
                keyboardType = KeyboardType.Decimal
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(SurfaceWhite)
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Conta criada", fontSize = 12.sp, color = Muted)
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = perfil?.tempoCadastrado ?: "Há 3 meses",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Ink
                )
            }

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

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}
