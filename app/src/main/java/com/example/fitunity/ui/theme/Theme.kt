package com.example.fitunity.ui.theme

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

private val FitUnityColorScheme = lightColorScheme(
    primary = BrandBlue,
    onPrimary = Color.White,
    primaryContainer = SoftBlue,
    onPrimaryContainer = BrandDark,
    secondary = Mint,
    onSecondary = Color.White,
    secondaryContainer = MintBackground,
    onSecondaryContainer = Mint,
    tertiary = Yellow,
    onTertiary = Ink,
    background = Canvas,
    onBackground = Ink,
    surface = SurfaceWhite,
    onSurface = Ink,
    surfaceVariant = SoftBlueBackground,
    onSurfaceVariant = Muted,
    outline = SoftBlueBorder,
    error = Danger,
    onError = Color.White
)

@Composable
fun FitUnityTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = FitUnityColorScheme,
        typography = Typography
    ) {
        CompositionLocalProvider(
            LocalTextStyle provides TextStyle(fontFamily = AppFont),
            content = content
        )
    }
}
