package com.gals.prayertimes.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.gals.prayertimes.R

private val ArialFontFamily = FontFamily(
    Font(R.font.arial)
)

val PrayerTypography = Typography(
    headlineMedium = TextStyle(
        fontFamily = ArialFontFamily,
        fontWeight = FontWeight.W400,
        fontSize = 22.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = ArialFontFamily,
        fontWeight = FontWeight.W400,
        fontSize = 20.sp
    ),
    titleLarge = TextStyle(
        fontFamily = ArialFontFamily,
        fontWeight = FontWeight.W400,
        fontSize = 18.sp
    ),
    titleMedium = TextStyle(
        fontFamily = ArialFontFamily,
        fontWeight = FontWeight.W400,
        fontSize = 16.sp
    )
)

val LightTextStyle = TextStyle(
    color = Color.White,
    fontFamily = ArialFontFamily,
    fontSize = 20.sp,
    fontWeight = FontWeight.W400
)

val DarkTextStyle = TextStyle(
    color = Color.Black,
    fontFamily = ArialFontFamily,
    fontSize = 20.sp,
    fontWeight = FontWeight.W400
)