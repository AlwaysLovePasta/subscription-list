package com.andrea.subscriptionlist.core.ui.theme

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// ── Raw palette ───────────────────────────────────────────────────────────────
val Cream = Color(0xFFF4EBDB)
val CreamDark = Color(0xFFEFE4D0)
val Parchment = Color(0xFFFBF5E9)
val CardWhite = Color(0xFFFFFFFF)

val InkDeep = Color(0xFF2E2720)
val InkMid = Color(0xFF5E5349)
val InkLight = Color(0xFF8A7F72)

// rgba(60,45,25,0.10) and rgba(60,45,25,0.18)
val BorderSubtle = Color(0x1A3C2D19)
val BorderMid = Color(0x2E3C2D19)

// oklch approximations (see Design System.html)
val AccentGreen = Color(0xFF3E8443)     // oklch(0.55 0.12 145)
val AccentGreenLight = Color(0xFF5E9660) // oklch(0.62 0.10 145)
val AccentGreenBg = Color(0xFFD7ECD2)   // oklch(0.92 0.04 140)
val Danger = Color(0xFFB94642)          // oklch(0.55 0.15 25)

// ── Color scheme ──────────────────────────────────────────────────────────────
val SubscriptionColorScheme = lightColorScheme(
    primary = AccentGreen,
    onPrimary = Parchment,
    primaryContainer = AccentGreenBg,
    onPrimaryContainer = InkDeep,
    secondary = InkMid,
    onSecondary = Parchment,
    secondaryContainer = CreamDark,
    onSecondaryContainer = InkDeep,
    tertiary = InkLight,
    onTertiary = Parchment,
    tertiaryContainer = Cream,
    onTertiaryContainer = InkDeep,
    error = Danger,
    onError = Parchment,
    errorContainer = Color(0xFFF5DCD9),
    onErrorContainer = Color(0xFF5C1A18),
    background = Cream,
    onBackground = InkDeep,
    surface = Parchment,
    onSurface = InkDeep,
    surfaceVariant = CreamDark,
    onSurfaceVariant = InkMid,
    outline = BorderSubtle,
    outlineVariant = BorderMid,
    scrim = InkDeep,
    inverseSurface = InkDeep,
    inverseOnSurface = Parchment,
    inversePrimary = AccentGreenLight,
)
