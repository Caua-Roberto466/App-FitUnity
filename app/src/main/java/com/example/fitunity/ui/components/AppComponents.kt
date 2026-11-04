package com.example.fitunity.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitunity.R
import com.example.fitunity.ui.theme.BrandBlue
import com.example.fitunity.ui.theme.BrandDark
import com.example.fitunity.ui.theme.Danger
import com.example.fitunity.ui.theme.Ink
import com.example.fitunity.ui.theme.Muted
import com.example.fitunity.ui.theme.SoftBlue
import com.example.fitunity.ui.theme.SoftBlueBackground
import com.example.fitunity.ui.theme.SoftBlueBorder
import com.example.fitunity.ui.theme.SurfaceWhite
import com.example.fitunity.ui.theme.Track

@Composable
fun FitUnityBrand(
    modifier: Modifier = Modifier,
    textColor: Color = Ink,
    textSize: Int = 20
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(id = R.drawable.ic_fitunity_logo),
            contentDescription = "Logo FitUnity",
            modifier = Modifier
                .height((textSize * 1.45f).dp)
                .width((textSize * 2.8f).dp),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "FitUnity",
            fontSize = textSize.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            letterSpacing = (-0.5).sp
        )
    }
}

@Composable
fun PageHeader(
    titulo: String,
    subtitulo: String,
    modifier: Modifier = Modifier,
    trailing: @Composable () -> Unit = {}
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = titulo, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Ink)
            Text(text = subtitulo, fontSize = 13.sp, color = Muted, modifier = Modifier.padding(top = 2.dp))
        }
        trailing()
    }
}

@Composable
fun DetailHeader(
    titulo: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    trailing: @Composable () -> Unit = {}
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .shadow(5.dp, RoundedCornerShape(11.dp))
                .clip(RoundedCornerShape(11.dp))
                .background(SurfaceWhite)
                .clickable { onBack() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Voltar",
                tint = Ink,
                modifier = Modifier.size(20.dp)
            )
        }
        Text(
            text = titulo,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Ink,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )
        Box(modifier = Modifier.width(44.dp), contentAlignment = Alignment.CenterEnd) {
            trailing()
        }
    }
}

@Composable
fun SectionHeading(
    titulo: String,
    subtitulo: String? = null,
    acao: String? = null,
    onAcao: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 22.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = titulo, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Ink)
            if (subtitulo != null) {
                Text(text = subtitulo, fontSize = 13.sp, color = Muted, modifier = Modifier.padding(top = 1.dp))
            }
        }
        if (acao != null && onAcao != null) {
            Text(
                text = acao,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = BrandDark,
                modifier = Modifier.clickable { onAcao() }
            )
        }
    }
}

@Composable
fun PrimaryActionButton(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    cor: Color = BrandBlue
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .height(52.dp),
        shape = RoundedCornerShape(14.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 3.dp,
            pressedElevation = 1.dp,
            disabledElevation = 0.dp
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = cor,
            contentColor = Color.White,
            disabledContainerColor = cor.copy(alpha = 0.45f),
            disabledContentColor = Color.White
        )
    ) {
        Text(text = texto, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ProgressRing(
    valor: Int,
    rotulo: String,
    cor: Color = BrandBlue,
    tamanho: Dp = 78.dp
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.size(tamanho), contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val stroke = 7.dp.toPx()
                drawArc(
                    color = Track,
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = stroke, cap = StrokeCap.Round)
                )
                drawArc(
                    color = cor,
                    startAngle = -90f,
                    sweepAngle = 360f * valor / 100f,
                    useCenter = false,
                    style = Stroke(width = stroke, cap = StrokeCap.Round)
                )
            }
            Text(text = "$valor%", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Ink)
        }
        Text(
            text = rotulo,
            fontSize = 12.sp,
            color = Muted,
            modifier = Modifier.padding(top = 7.dp)
        )
    }
}

@Composable
fun CalorieRing(
    atual: String,
    meta: String,
    percentual: Int,
    tamanho: Dp = 125.dp,
    cor: Color = BrandBlue
) {
    Box(modifier = Modifier.size(tamanho), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val stroke = 13.dp.toPx()
            drawArc(
                color = Track,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = stroke)
            )
            drawArc(
                color = cor,
                startAngle = -90f,
                sweepAngle = 360f * percentual / 100f,
                useCenter = false,
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = atual, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Ink)
            Text(text = meta, fontSize = 12.sp, color = Muted)
        }
    }
}

@Composable
fun MacroBar(
    rotulo: String,
    valor: String,
    percentual: Float,
    cor: Color = BrandBlue,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(text = rotulo, fontSize = 12.sp, color = Muted, modifier = Modifier.weight(1f))
            Text(text = valor, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Ink)
        }
        Spacer(modifier = Modifier.height(5.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp)
                .clip(CircleShape)
                .background(Track)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(percentual.coerceIn(0f, 1f))
                    .height(5.dp)
                    .clip(CircleShape)
                    .background(cor)
            )
        }
    }
}

@Composable
fun SegmentedControl(
    opcoes: List<String>,
    selecionado: Int,
    onSelecionar: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(13.dp))
            .background(Track)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        opcoes.forEachIndexed { index, opcao ->
            val ativo = index == selecionado
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (ativo) SurfaceWhite else Color.Transparent)
                    .clickable { onSelecionar(index) }
                    .padding(vertical = 9.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = opcao,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (ativo) BrandDark else Muted
                )
            }
        }
    }
}

@Composable
fun FilterChips(
    opcoes: List<String>,
    selecionado: Int,
    onSelecionar: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        opcoes.forEachIndexed { index, opcao ->
            val ativo = index == selecionado
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (ativo) BrandBlue else Track)
                    .clickable { onSelecionar(index) }
                    .padding(horizontal = 12.dp, vertical = 7.dp)
            ) {
                Text(
                    text = opcao,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (ativo) Color.White else Muted
                )
            }
        }
    }
}

data class MenuAction(
    val label: String,
    val detail: String? = null,
    val danger: Boolean = false,
    val onClick: () -> Unit
)

@Composable
fun MoreMenu(itens: List<MenuAction>, modifier: Modifier = Modifier) {
    var aberto by remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        IconButton(
            onClick = { aberto = true },
            modifier = Modifier
                .size(36.dp)
                .shadow(4.dp, RoundedCornerShape(11.dp))
                .clip(RoundedCornerShape(11.dp))
                .background(SurfaceWhite)
        ) {
            Icon(
                imageVector = Icons.Filled.MoreHoriz,
                contentDescription = "Mais ações",
                tint = Muted,
                modifier = Modifier.size(20.dp)
            )
        }
        DropdownMenu(
            expanded = aberto,
            onDismissRequest = { aberto = false },
            modifier = Modifier
                .width(220.dp)
                .background(SurfaceWhite)
        ) {
            itens.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Column {
                            Text(
                                text = item.label,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (item.danger) Danger else Ink
                            )
                            if (item.detail != null) {
                                Text(text = item.detail, fontSize = 12.sp, color = Muted)
                            }
                        }
                    },
                    onClick = {
                        aberto = false
                        item.onClick()
                    }
                )
            }
        }
    }
}

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    label: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(modifier = modifier.fillMaxWidth()) {
        if (label != null) {
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Ink,
                modifier = Modifier.padding(bottom = 6.dp)
            )
        }
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(text = placeholder, fontSize = 13.sp, color = Muted) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 13.sp, color = Ink),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BrandBlue,
                unfocusedBorderColor = SoftBlueBorder,
                focusedContainerColor = SurfaceWhite,
                unfocusedContainerColor = SurfaceWhite,
                cursorColor = BrandBlue
            )
        )
    }
}

@Composable
fun SearchField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "Buscar exercício...",
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(text = placeholder, fontSize = 13.sp, color = Muted) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = null,
                tint = Muted,
                modifier = Modifier.size(18.dp)
            )
        },
        singleLine = true,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(13.dp),
        textStyle = androidx.compose.ui.text.TextStyle(fontSize = 13.sp, color = Ink),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = BrandBlue,
            unfocusedBorderColor = SoftBlueBorder,
            focusedContainerColor = SurfaceWhite,
            unfocusedContainerColor = SurfaceWhite,
            cursorColor = BrandBlue
        )
    )
}

@Composable
fun InfoPill(texto: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(SoftBlueBackground)
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(text = texto, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = BrandDark)
    }
}

@Composable
fun ChevronRight(tint: Color = Muted, modifier: Modifier = Modifier) {
    Icon(
        imageVector = Icons.Filled.ChevronRight,
        contentDescription = null,
        tint = tint,
        modifier = modifier.size(18.dp)
    )
}

@Composable
fun StatBlock(
    valor: String,
    rotulo: String,
    modifier: Modifier = Modifier,
    corValor: Color = BrandDark
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = valor, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = corValor)
        Text(text = rotulo, fontSize = 12.sp, color = Muted)
    }
}

@Composable
fun Avatar(
    iniciais: String,
    modifier: Modifier = Modifier,
    tamanho: Dp = 45.dp,
    corInicial: Color = BrandDark,
    corFinal: Color = BrandBlue
) {
    Box(
        modifier = modifier
            .size(tamanho)
            .clip(CircleShape)
            .background(androidx.compose.ui.graphics.Brush.linearGradient(listOf(corInicial, corFinal))),
        contentAlignment = Alignment.Center
    ) {
        Text(text = iniciais, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun TagChip(texto: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(SoftBlueBackground)
            .padding(horizontal = 11.dp, vertical = 7.dp)
    ) {
        Text(text = texto, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = BrandDark)
    }
}

@Composable
fun SoftCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(20.dp), clip = false)
            .clip(RoundedCornerShape(20.dp))
            .background(SurfaceWhite)
            .border(1.dp, SoftBlueBorder.copy(alpha = 0.35f), RoundedCornerShape(20.dp))
    ) {
        content()
    }
}

@Composable
fun SectionTitleInline(texto: String, modifier: Modifier = Modifier) {
    Text(
        text = texto,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = Ink,
        modifier = modifier
    )
}

@Composable
fun EmptyNote(texto: String, modifier: Modifier = Modifier) {
    Text(
        text = texto,
        fontSize = 13.sp,
        color = Muted,
        textAlign = TextAlign.Center,
        modifier = modifier.fillMaxWidth()
    )
}
