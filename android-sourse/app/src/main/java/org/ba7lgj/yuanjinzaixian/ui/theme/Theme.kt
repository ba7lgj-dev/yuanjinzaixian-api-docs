package org.ba7lgj.yuanjinzaixian.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = BrandGreen,
    onPrimary = Color.White,
    primaryContainer = BrandGreenContainer,
    onPrimaryContainer = BrandGreenDark,
    secondary = BrandGreenDark,
    onSecondary = Color.White,
    secondaryContainer = BrandGreenContainer,
    onSecondaryContainer = BrandGreenDark,
    error = BrandError,
    onError = Color.White,
    background = BrandSurface,
    onBackground = Color(0xFF1B1B18),
    surface = BrandSurface,
    onSurface = Color(0xFF1B1B18),
    surfaceVariant = Color(0xFFEDEFE9),
    onSurfaceVariant = Color(0xFF4A4F49),
    outline = BrandOutline,
    outlineVariant = Color(0xFFD7DAD3)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF8FCF95),
    onPrimary = Color(0xFF10300F),
    primaryContainer = BrandGreenContainerDark,
    onPrimaryContainer = Color(0xFFC8E6C9),
    secondary = Color(0xFFA5D6A7),
    onSecondary = Color(0xFF10300F),
    secondaryContainer = BrandGreenContainerDark,
    onSecondaryContainer = Color(0xFFC8E6C9),
    error = Color(0xFFEF9A9A),
    onError = Color(0xFF3B0907),
    background = BrandSurfaceDark,
    onBackground = Color(0xFFE5E7E2),
    surface = BrandSurfaceDark,
    onSurface = Color(0xFFE5E7E2),
    surfaceVariant = Color(0xFF222722),
    onSurfaceVariant = Color(0xFFBFC5BC),
    outline = BrandOutline,
    outlineVariant = Color(0xFF3A3F39)
)

@Composable
fun YuanjinzaixianTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}
