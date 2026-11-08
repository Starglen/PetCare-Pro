package com.starglen.petcarepro.ui.theme

import androidx.compose.ui.graphics.Color

// PetCare's visual identity: joyful orange inspired by the provided reference,
// balanced with warm cream, deep cocoa text, and restrained botanical green.
val PetOrange = Color(0xFFC65300)       // accessible primary action on cream
val PetOrangeBright = Color(0xFFFF8A00) // illustration and highlight accent
val PetOrangeSoft = Color(0xFFFFE6CC)
val PetOrangePale = Color(0xFFFFF1E2)
val PetCream = Color(0xFFFFF8F0)
val PetPaper = Color(0xFFFFFFFF)
val PetInk = Color(0xFF2D2018)
val PetMuted = Color(0xFF75665D)
val PetBorder = Color(0xFFE9DCCF)
val PetGreen = Color(0xFF32705A)
val PetGreenSoft = Color(0xFFE4F1E8)
val PetCoral = Color(0xFFB63F36)
val PetError = Color(0xFFB3261E)
val PetSuccess = Color(0xFF276749)
val PetWarning = Color(0xFF805600)

// Compatibility aliases used by legacy screens while they adopt shared tokens.
val maincolor = PetOrange
val newwhite = PetCream
val Forest = PetOrange
val ForestLight = PetOrangeBright
val Mint = PetOrangeSoft
val Canvas = PetCream
val Paper = PetPaper
val Ink = PetInk
val MutedInk = PetMuted
val Border = PetBorder
val Marigold = PetOrangeBright
val Terracotta = PetCoral
val Success = PetSuccess
val Warning = PetWarning
val Danger = PetError

// Left in place for source compatibility with the starter Material theme.
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)
val Purple40 = Color(0xFF6650A4)
val PurpleGrey40 = Color(0xFF625B71)
val Pink40 = Color(0xFF7D5260)
