package com.quizrevision.smartquizandrevisionapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val QuizRevisionColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = AppSurface,
    secondary = Accent,
    onSecondary = AppSurface,
    background = AppBackground,
    onBackground = TextPrimary,
    surface = AppSurface,
    onSurface = TextPrimary,
    error = ErrorColor,
    outline = Border
)

@Composable
fun QuizRevisionAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = QuizRevisionColorScheme,
        typography = AppTypography,
        content = content
    )
}
