package com.andrea.subscriptionlist.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.andrea.subscriptionlist.core.ui.R

val Geist = FontFamily(
    Font(R.font.geist_regular,  weight = FontWeight.Normal),
    Font(R.font.geist_medium,   weight = FontWeight.Medium),
    Font(R.font.geist_semibold, weight = FontWeight.SemiBold),
    Font(R.font.geist_bold,     weight = FontWeight.Bold),
)

val InstrumentSerif = FontFamily(
    Font(R.font.instrument_serif_italic, FontWeight.Normal)
)

val GeistMono = FontFamily(
    Font(R.font.geist_mono_medium, FontWeight.Normal)
)

// ── Typography scale (mapped from Design System) ──────────────────────────────
//
//  displayLarge  → Display   : Instrument Serif Italic 52sp  -0.02em
//  headlineLarge → Heading   : Instrument Serif Italic 32sp
//  titleLarge    → App title : Geist SemiBold         22sp  -0.02em
//  bodyLarge     → Body      : Geist Medium            15sp
//  bodyMedium    → Label     : Geist Medium            13sp
//  labelMedium   → Eyebrow   : Geist Medium CAPS       12sp  +0.08em
//  labelSmall    → Mono/data : Geist Mono Regular      12sp

val SubscriptionTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = InstrumentSerif,
        fontWeight = FontWeight.Normal,
        fontStyle = FontStyle.Italic,
        fontSize = 52.sp,
        lineHeight = 52.sp,
        letterSpacing = (-0.02).em,
    ),
    headlineLarge = TextStyle(
        fontFamily = InstrumentSerif,
        fontWeight = FontWeight.Normal,
        fontStyle = FontStyle.Italic,
        fontSize = 32.sp,
        lineHeight = 35.sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = InstrumentSerif,
        fontWeight = FontWeight.Normal,
        fontStyle = FontStyle.Italic,
        fontSize = 28.sp,
        lineHeight = 32.sp,
    ),
    headlineSmall = TextStyle(
        fontFamily = InstrumentSerif,
        fontWeight = FontWeight.Normal,
        fontStyle = FontStyle.Italic,
        fontSize = 24.sp,
        lineHeight = 28.sp,
    ),
    titleLarge = TextStyle(
        fontFamily = Geist,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = (-0.02).em,
    ),
    titleMedium = TextStyle(
        fontFamily = Geist,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = (-0.01).em,
    ),
    titleSmall = TextStyle(
        fontFamily = Geist,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = Geist,
        fontWeight = FontWeight.Medium,
        fontSize = 15.sp,
        lineHeight = 23.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = Geist,
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp,
        lineHeight = 20.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = Geist,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 18.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = Geist,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
    labelMedium = TextStyle(
        fontFamily = Geist,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = (0.08).em,
    ),
    labelSmall = TextStyle(
        fontFamily = GeistMono,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
    ),
)
