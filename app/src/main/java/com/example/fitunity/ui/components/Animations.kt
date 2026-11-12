package com.example.fitunity.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

val LocalRevealShadowProgress = compositionLocalOf { 1f }

/**
 * Aplica sombra com elevação proporcional ao progresso de revelação atual.
 * Fora de um [RevealOnAppear] o progresso é 1f e a sombra se comporta normalmente.
 */
@Composable
fun Modifier.revealShadow(
    elevation: Dp,
    shape: Shape = RectangleShape,
    clip: Boolean = elevation > 0.dp
): Modifier {
    val progresso = LocalRevealShadowProgress.current
    return shadow(elevation * progresso, shape, clip)
}

/**
 * Revela o conteúdo com um leve fade + slide (e escala opcional) ao entrar em composição.
 * Ideal para animar seções de uma tela com efeito cascata usando [delayMillis].
 * A sombra dos cards só aparece, de forma linear, após o término da revelação.
 */
@Composable
fun RevealOnAppear(
    modifier: Modifier = Modifier,
    delayMillis: Int = 0,
    durationMillis: Int = 620,
    offsetY: Dp = 24.dp,
    scaleFrom: Float = 1f,
    content: @Composable () -> Unit
) {
    var visivel by remember { mutableStateOf(false) }
    var sombraVisivel by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        if (delayMillis > 0) delay(delayMillis.toLong())
        visivel = true
        delay(durationMillis.toLong())
        sombraVisivel = true
    }
    val progresso by animateFloatAsState(
        targetValue = if (visivel) 1f else 0f,
        animationSpec = tween(durationMillis, easing = FastOutSlowInEasing),
        label = "revealOnAppear"
    )
    val progressoSombra by animateFloatAsState(
        targetValue = if (sombraVisivel) 1f else 0f,
        animationSpec = tween(durationMillis = 420, easing = LinearEasing),
        label = "revealShadow"
    )
    CompositionLocalProvider(LocalRevealShadowProgress provides progressoSombra) {
        Box(
            modifier = modifier.graphicsLayer {
                clip = false
                alpha = progresso
                translationY = (1f - progresso) * offsetY.toPx()
                val escala = scaleFrom + (1f - scaleFrom) * progresso
                scaleX = escala
                scaleY = escala
            }
        ) {
            content()
        }
    }
}

/** Animação de "pop" com mola, usada para ícones e destaques. */
@Composable
fun PopInOnAppear(
    modifier: Modifier = Modifier,
    delayMillis: Int = 0,
    content: @Composable () -> Unit
) {
    RevealOnAppear(
        modifier = modifier,
        delayMillis = delayMillis,
        durationMillis = 560,
        offsetY = 0.dp,
        scaleFrom = 0.55f,
        content = content
    )
}

/**
 * Retorna um progresso animado de 0 até [target] (0f..1f) na primeira composição
 * e sempre que [target] mudar. Útil para arcos, barras e preenchimentos.
 */
@Composable
fun rememberAnimatedProgress(
    target: Float,
    durationMillis: Int = 900,
    delayMillis: Int = 0
): Float {
    val animacao = remember { Animatable(0f) }
    LaunchedEffect(target, durationMillis, delayMillis) {
        if (delayMillis > 0) delay(delayMillis.toLong())
        animacao.animateTo(
            targetValue = target.coerceIn(0f, 1f),
            animationSpec = tween(durationMillis, easing = FastOutSlowInEasing)
        )
    }
    return animacao.value
}

/** Versão em Dp de [rememberAnimatedProgress], para animar alturas/larguras. */
@Composable
fun rememberAnimatedDp(
    target: Dp,
    durationMillis: Int = 700,
    delayMillis: Int = 0
): Dp {
    val animacao = remember { Animatable(0f) }
    LaunchedEffect(target, durationMillis, delayMillis) {
        if (delayMillis > 0) delay(delayMillis.toLong())
        animacao.animateTo(
            targetValue = target.value,
            animationSpec = tween(durationMillis, easing = FastOutSlowInEasing)
        )
    }
    return animacao.value.dp
}

/** Contador inteiro animado, para números de destaque (percentuais, calorias etc.). */
@Composable
fun rememberAnimatedInt(
    target: Int,
    durationMillis: Int = 900
): Int {
    val animacao = remember { Animatable(0f) }
    LaunchedEffect(target, durationMillis) {
        animacao.animateTo(
            targetValue = target.toFloat(),
            animationSpec = tween(durationMillis, easing = FastOutSlowInEasing)
        )
    }
    return animacao.value.roundToInt()
}

/** Check com entrada em mola quando marcado. */
@Composable
fun AnimatedCheck(
    visivel: Boolean,
    modifier: Modifier = Modifier,
    tint: Color = Color.White
) {
    val escala by animateFloatAsState(
        targetValue = if (visivel) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "animatedCheck"
    )
    Icon(
        imageVector = Icons.Filled.Check,
        contentDescription = null,
        tint = tint,
        modifier = modifier.graphicsLayer {
            scaleX = escala
            scaleY = escala
            alpha = escala.coerceIn(0f, 1f)
        }
    )
}

/** Ponto pulsante, usado para indicar notificações não lidas. */
@Composable
fun PulsingDot(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFFFF5862)
) {
    val transicao = rememberInfiniteTransition(label = "pulsingDot")
    val escala by transicao.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.35f,
        animationSpec = infiniteRepeatable(
            animation = tween(750, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulsingDotScale"
    )
    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = escala
                scaleY = escala
            }
            .clip(CircleShape)
            .background(color)
    )
}
