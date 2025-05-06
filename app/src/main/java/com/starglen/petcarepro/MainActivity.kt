package com.starglen.petcarepro


import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.navigation.AppNavHost
import com.starglen.petcarepro.ui.theme.PetCareProTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PetCareProTheme {
                AppNavHost()
            }
        }
    }
}
