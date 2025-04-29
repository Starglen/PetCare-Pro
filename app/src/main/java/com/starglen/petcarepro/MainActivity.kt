package com.starglen.petcarepro

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.navigation.AppNavHost
import com.starglen.petcarepro.ui.theme.PetCareProTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var notificationPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Notifications for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionLauncher = registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted ->
                Toast.makeText(
                    this,
                    if (isGranted) "Notification Permission Granted" else "Notification Permission Denied",
                    Toast.LENGTH_SHORT
                ).show()
            }

            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        // Background location (Android 10+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
            checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                1001
            )
        }

        // Exact alarms (Android 12+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val hasPermission = checkSelfPermission(Manifest.permission.SCHEDULE_EXACT_ALARM)
            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.SCHEDULE_EXACT_ALARM),
                    1002
                )
            }
        }

        setContent {
            PetCareProTheme {
                // Automatically adapts to light and dark themes
                AppNavHost()
            }
        }
    }
}
