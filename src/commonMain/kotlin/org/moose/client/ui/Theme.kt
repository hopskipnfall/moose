package org.moose.client.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val DarkPurple = Color(0xFF1B112C)
val LightPurple = Color(0xFF8B5CF6)
val NeonBlue = Color(0xFF3B82F6)
val DarkBackground = Color(0xFF0F0B1A)
val SurfaceColor = Color(0xFF1E1533)
val TextPrimary = Color(0xFFFFFFFF)
val TextSecondary = Color(0xFFA1A1AA)

private val DarkColorPalette = darkColors(
    primary = LightPurple,
    primaryVariant = DarkPurple,
    secondary = NeonBlue,
    background = DarkBackground,
    surface = SurfaceColor,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
)

@Composable
fun MooseTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = DarkColorPalette,
        content = content
    )
}
