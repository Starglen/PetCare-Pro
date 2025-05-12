package com.starglen.petcarepro.ui.screens.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.navigation.ROUT_LOGIN
import com.starglen.petcarepro.R
import com.starglen.petcarepro.ui.theme.maincolor
import kotlinx.coroutines.delay
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(navController: NavController) {
    var showFullLogo by remember { mutableStateOf(false) }

    val pawAlpha = remember { Animatable(0f) }
    val pawScale = remember { Animatable(0.8f) }

    val fullLogoAlpha = remember { Animatable(0f) }
    val fullLogoOffsetY = remember { Animatable(30f) }

    LaunchedEffect(Unit) {
        // Fade in and scale up the paw
        launch {
            pawAlpha.animateTo(
                1f,
                animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
            )
        }
        pawScale.animateTo(
            1f,
            animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
        )

        delay(1000)

        // Fade out paw
        launch {
            pawAlpha.animateTo(
                0f,
                animationSpec = tween(durationMillis = 500, easing = FastOutLinearInEasing)
            )
        }

        delay(300)
        showFullLogo = true

        // Animate full logo in
        launch {
            fullLogoAlpha.animateTo(
                1f,
                animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing)
            )
        }
        fullLogoOffsetY.animateTo(
            0f,
            animationSpec = spring(
                stiffness = Spring.StiffnessLow,
                dampingRatio = Spring.DampingRatioNoBouncy
            )
        )

        delay(1300)

        navController.navigate(ROUT_LOGIN) {
            popUpTo(0)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(maincolor),
        contentAlignment = Alignment.Center
    ) {
        if (!showFullLogo) {
            Image(
                painter = painterResource(id = R.drawable.logo_paw),
                contentDescription = "Paw Logo",
                modifier = Modifier
                    .graphicsLayer(
                        scaleX = pawScale.value,
                        scaleY = pawScale.value,
                        alpha = pawAlpha.value
                    )
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.logo_full),
                contentDescription = "Full Logo",
                modifier = Modifier
                    .offset(y = fullLogoOffsetY.value.dp)
                    .alpha(fullLogoAlpha.value)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen(rememberNavController())
}
