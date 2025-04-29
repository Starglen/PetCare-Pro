package com.starglen.petcarepro.ui.screens.splash

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.navigation.ROUT_HOME
import com.starglen.petcarepro.R
import com.starglen.petcarepro.ui.theme.maincolor
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    LaunchedEffect(Unit) {
        delay(2000)
        navController.navigate(ROUT_HOME) {
            popUpTo(0) // Optional: clear backstack
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(maincolor),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.paw),
            contentDescription = "App Splash Logo"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen(rememberNavController())
}
