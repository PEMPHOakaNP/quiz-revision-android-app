package com.quizrevision.smartquizandrevisionapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// NOTE: the Design artboards use Manrope for headings/buttons and Source Sans 3
// for body text. To use the exact same fonts here, download both from Google
// Fonts, add the .ttf files under app/src/main/res/font/, and replace
// FontFamily.Default below with FontFamily(Font(R.font.manrope_bold)) etc.
// Using FontFamily.Default (the system sans-serif) for now keeps the project
// buildable without extra font files, while keeping the same weights and sizes.

val HeadingFamily = FontFamily.Default
val BodyFamily = FontFamily.Default

val AppTypography = Typography(
    headlineSmall = TextStyle(
        fontFamily = HeadingFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 26.sp
    ),
    titleLarge = TextStyle(
        fontFamily = HeadingFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 19.sp
    ),
    titleMedium = TextStyle(
        fontFamily = HeadingFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = BodyFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = BodyFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp
    ),
    bodySmall = TextStyle(
        fontFamily = BodyFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),
    labelLarge = TextStyle(
        fontFamily = HeadingFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp
    )
)
