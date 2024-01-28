package com.pwojtowicz.buybuddies.ui.theme


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = Purple20,
    secondary = DarkGray,
    surface = DarkSurfaceColor,
    tertiary = Blue20
)

private val LightColorScheme = lightColorScheme(
    primary = Purple20,
    background = LightGray,
    surface = DarkSurfaceColor,
    tertiary = Blue20
)

@Composable
fun BuyBuddiesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}