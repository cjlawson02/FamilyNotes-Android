package com.familynotes.frame.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.familynotes.frame.ui.theme.BrandBackground
import com.familynotes.frame.ui.theme.BrandBlack
import com.familynotes.frame.ui.theme.BrandRed
import com.familynotes.frame.ui.theme.BrandWhite

private val DarkColorScheme = darkColorScheme(
    primary = BrandRed,
    secondary = BrandBackground,
    tertiary = BrandWhite,
    background = BrandBackground,
    surface = BrandBackground,
    onPrimary = BrandWhite,
    onSecondary = BrandWhite,
    onTertiary = BrandBlack,
    onBackground = BrandWhite,
    onSurface = BrandWhite
)

private val LightColorScheme = lightColorScheme(
    primary = BrandRed,
    secondary = BrandBackground,
    tertiary = BrandWhite,
    background = BrandWhite,
    surface = BrandWhite,
    onPrimary = BrandWhite,
    onSecondary = BrandWhite,
    onTertiary = BrandBlack,
    onBackground = BrandBlack,
    onSurface = BrandBlack
)

@Composable
fun FamilyNotesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
