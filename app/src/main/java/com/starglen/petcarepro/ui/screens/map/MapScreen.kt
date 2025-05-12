package com.starglen.petcarepro.ui.screens.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import com.google.maps.android.compose.*
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(navController: NavController) {
    val permissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    val context = LocalContext.current
    var userLocation by remember { mutableStateOf<LatLng?>(null) }
    var nearbyVets by remember { mutableStateOf<List<LatLng>>(emptyList()) }

    // Request permission if not granted
    if (!permissionState.status.isGranted) {
        SideEffect {
            permissionState.launchPermissionRequest()
        }
    }

    LaunchedEffect(permissionState.status.isGranted) {
        if (permissionState.status.isGranted) {
            userLocation = getCurrentLocation(context)
            userLocation?.let {
                // Fetch nearby vet clinics once location is available
                nearbyVets = getNearbyVets(context, it)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (userLocation != null) {
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(userLocation!!, 15f)
            }

            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(isMyLocationEnabled = true)
            ) {
                // Marker for the user's location
                Marker(
                    state = MarkerState(position = userLocation!!),
                    title = "You are here"
                )

                // Markers for nearby vet clinics
                nearbyVets.forEach { vet ->
                    Marker(
                        state = MarkerState(position = vet),
                        title = "Nearby Vet Clinic"
                    )
                }
            }
        } else {
            // Show loading indicator while waiting for location
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@SuppressLint("MissingPermission")
suspend fun getCurrentLocation(context: Context): LatLng? {
    return try {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        val location: Location? = fusedLocationProviderClient.lastLocation.await()
        location?.let {
            LatLng(it.latitude, it.longitude)
        } ?: run {
            // Log or handle the case when location is null (e.g., location services are off)
            null
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

// Fetch nearby vet clinics
suspend fun getNearbyVets(context: Context, location: LatLng): List<LatLng> {
    return withContext(Dispatchers.IO) {
        try {
            val url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" +
                    "?location=${location.latitude},${location.longitude}" +
                    "&radius=5000&type=veterinary_care&key=AIzaSyCffAJdmF3H_JNGLU2ok0VdblsEkH7dgm0"  // Your API Key

            val result = URL(url).readText()
            val json = JSONObject(result)
            val results = json.getJSONArray("results")

            val vets = mutableListOf<LatLng>()
            for (i in 0 until results.length()) {
                val loc = results.getJSONObject(i).getJSONObject("geometry").getJSONObject("location")
                vets.add(LatLng(loc.getDouble("lat"), loc.getDouble("lng")))
            }
            return@withContext vets
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext emptyList<LatLng>() // Return empty list on failure
        }
    }
}
