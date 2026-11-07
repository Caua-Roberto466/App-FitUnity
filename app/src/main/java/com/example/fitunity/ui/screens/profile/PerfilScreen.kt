package com.example.fitunity.ui.screens.profile

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fitunity.data.FotoPerfil
import com.example.fitunity.data.SessionManager
import com.example.fitunity.ui.components.Avatar
import com.example.fitunity.ui.components.FitUnityBottomBar
import com.example.fitunity.ui.components.PageHeader
import com.example.fitunity.ui.components.RevealOnAppear
import com.example.fitunity.ui.components.SectionHeading
import com.example.fitunity.ui.components.revealShadow
import com.example.fitunity.ui.components.StatBlock
import com.example.fitunity.ui.navigation.Rotas
import com.example.fitunity.ui.theme.BrandBlue
import com.example.fitunity.ui.theme.BrandDark
import com.example.fitunity.ui.theme.Coral
import com.example.fitunity.ui.theme.Danger
import com.example.fitunity.ui.theme.Ink
import com.example.fitunity.ui.theme.Mint
import com.example.fitunity.ui.theme.Muted
import com.example.fitunity.ui.theme.SoftBlueBackground
import com.example.fitunity.ui.theme.SurfaceWhite

@Composable
fun PerfilScreen(navController: NavController) {
    val context = LocalContext.current
    val perfil = SessionManager.usuarioAtual.value
    val nome = perfil?.nome ?: "Usuário FitUnity"
    val iniciais = nome.split(" ").filter { it.isNotBlank() }.take(2)
        .joinToString("") { it.first().uppercase() }
    val foto = remember(perfil?.fotoUri) { FotoPerfil.carregar(perfil?.fotoUri) }

    Scaffold(
        bottomBar = { FitUnityBottomBar(navController) },
        containerColor = Color(0xFFF8FBFD)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 22.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            RevealOnAppear(
                modifier = Modifier.fillMaxWidth(),
                delayMillis = 0,
                offsetY = 14.dp
            ) {
            PageHeader(
                titulo = "Meu perfil",
                subtitulo = "Gerencie sua conta e preferências"
            )
            }

            Spacer(modifier = Modifier.height(20.dp))

            RevealOnAppear(
                modifier = Modifier.fillMaxWidth(),
                delayMillis = 80,
                offsetY = 20.dp,
                scaleFrom = 0.97f
            ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(listOf(Color(0xFF0A8FE8), Color(0xFF075F96)))
                    )
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .clip(CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    if (foto != null) {
                        Image(
                            bitmap = foto.asImageBitmap(),
                            contentDescription = "Foto de perfil",
                            modifier = Modifier
                                .size(82.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Avatar(
                            iniciais = iniciais.ifBlank { "FU" },
                            tamanho = 82.dp,
                            corInicial = Color(0xFF0A3F63),
                            corFinal = Color(0xFF20B7E8)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = nome, fontSize = 19.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text(
                    text = perfil?.email ?: "usuario@fitunity.com",
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
                Row(
                    modifier = Modifier.padding(top = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.White.copy(alpha = 0.18f))
                            .padding(horizontal = 11.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = nomePlanoExibicao(perfil?.tipoPlano),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.White.copy(alpha = 0.18f))
                            .padding(horizontal = 11.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = perfil?.nivel ?: "Intermediário",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }
            }
            }

            RevealOnAppear(
                modifier = Modifier.fillMaxWidth(),
                delayMillis = 160,
                offsetY = 18.dp
            ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp)
                    .revealShadow(6.dp, RoundedCornerShape(18.dp), clip = false)
                    .clip(RoundedCornerShape(18.dp))
                    .background(SurfaceWhite)
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatBlock(valor = "${perfil?.treinosRealizados ?: 0}", rotulo = "Treinos", corValor = BrandBlue)
                StatBlock(valor = "${perfil?.pesoAtual ?: 0.0} kg", rotulo = "Peso atual", corValor = Mint)
                StatBlock(valor = "${perfil?.progresso?.toInt() ?: 0}%", rotulo = "Progresso", corValor = Coral)
            }
            }

            RevealOnAppear(
                modifier = Modifier.fillMaxWidth(),
                delayMillis = 220,
                offsetY = 16.dp
            ) {
            SectionHeading(titulo = "Conta")
            }

            RevealOnAppear(
                modifier = Modifier.fillMaxWidth(),
                delayMillis = 260,
                offsetY = 14.dp
            ) {
            MenuRow(Icons.Filled.Edit, "Editar dados", "Nome, e-mail e medidas") {
                navController.navigate(Rotas.EDITAR_DADOS)
            }
            }

            RevealOnAppear(
                modifier = Modifier.fillMaxWidth(),
                delayMillis = 310,
                offsetY = 14.dp
            ) {
            MenuRow(Icons.Filled.Person, "Meu personal", "Acompanhe seu treinador") {
                navController.navigate(Rotas.PERSONAL)
            }
            }

            RevealOnAppear(
                modifier = Modifier.fillMaxWidth(),
                delayMillis = 360,
                offsetY = 14.dp
            ) {
            MenuRow(Icons.Filled.Notifications, "Notificações", "Lembretes e avisos") {
                navController.navigate(Rotas.NOTIFICACOES)
            }
            }

            RevealOnAppear(
                modifier = Modifier.fillMaxWidth(),
                delayMillis = 410,
                offsetY = 14.dp
            ) {
            MenuRow(Icons.Filled.BarChart, "Relatório mensal", "Sua evolução em detalhes") {
                navController.navigate(Rotas.RELATORIO_MENSAL)
            }
            }

            RevealOnAppear(
                modifier = Modifier.fillMaxWidth(),
                delayMillis = 460,
                offsetY = 14.dp
            ) {
            MenuRow(
                Icons.Filled.Star,
                "Assinatura",
                "Plano ${nomePlanoExibicao(perfil?.tipoPlano)}"
            ) {
                navController.navigate(Rotas.PLANOS)
            }
            }

            Spacer(modifier = Modifier.height(10.dp))

            RevealOnAppear(
                modifier = Modifier.fillMaxWidth(),
                delayMillis = 520,
                offsetY = 16.dp
            ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFFDECEE))
                    .clickable {
                        SessionManager.logout(context)
                        navController.navigate(Rotas.LOGIN) {
                            popUpTo(Rotas.HOME) { inclusive = true }
                        }
                    }
                    .padding(15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Logout,
                    contentDescription = null,
                    tint = Danger,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "Sair da conta", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Danger)
            }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
private fun MenuRow(
    icone: ImageVector,
    titulo: String,
    subtitulo: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 9.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceWhite)
            .clickable { onClick() }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(11.dp))
                .background(SoftBlueBackground),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icone,
                contentDescription = null,
                tint = BrandDark,
                modifier = Modifier.size(19.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = titulo, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Ink)
            Text(text = subtitulo, fontSize = 11.sp, color = Muted)
        }
        Icon(
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = null,
            tint = Muted,
            modifier = Modifier.size(18.dp)
        )
    }
}
