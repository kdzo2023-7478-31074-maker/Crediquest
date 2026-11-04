package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val CrediQuestDarkColorScheme = darkColorScheme(
    primary = DarkAccent,
    onPrimary = PaperDark,
    primaryContainer = DarkSurfaceVariant,
    onPrimaryContainer = DarkInk,
    secondary = DarkAccent,
    onSecondary = PaperDark,
    background = PaperDark,
    onBackground = DarkInk,
    surface = DarkSurface,
    onSurface = DarkInk,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkMute,
    outline = DarkLine,
    outlineVariant = DarkLine
)

private val CrediQuestLightColorScheme = lightColorScheme(
    primary = InkPrimary,
    onPrimary = PaperLight,
    primaryContainer = PaperSurfaceVariant,
    onPrimaryContainer = InkPrimary,
    secondary = ClothNavy,
    onSecondary = Color.White,
    background = PaperLight,
    onBackground = InkPrimary,
    surface = PaperSurface,
    onSurface = InkPrimary,
    surfaceVariant = PaperSurfaceVariant,
    onSurfaceVariant = InkSecondary,
    outline = LineBorder,
    outlineVariant = LineBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> CrediQuestDarkColorScheme
        else -> CrediQuestLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
