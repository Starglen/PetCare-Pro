package com.starglen.petcarepro.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.navigation.ROUT_ACTIVITY
import com.navigation.ROUT_HEALTH
import com.navigation.ROUT_MAP
import com.navigation.ROUT_NUTRITION
import com.navigation.ROUT_REMINDER
import com.starglen.petcarepro.R
import com.starglen.petcarepro.ui.theme.maincolor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        // Remove the bottom bar completely
        topBar = {},

        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())

            ) {
                // App Logo and Title
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(R.drawable.paw),
                        contentDescription = "Pets",
                        modifier = Modifier
                            .size(74.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "PetCare Pro",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Description
                Text(
                    text = "Your pet’s health and care—made simple.",
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Featured Image
                Image(
                    painter = painterResource(R.drawable.catdog),
                    contentDescription = "Pets",
                    modifier = Modifier
                        .height(250.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(20.dp))

                // PetCare Reminder Button
                Button(
                    onClick = { navController.navigate(ROUT_REMINDER) },
                    colors = ButtonDefaults.buttonColors(maincolor),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                ) {
                    Icon(Icons.Default.Notifications, contentDescription = "Reminder", tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("PetCare Reminder", color = Color.White)
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Features Header
                Text(
                    text = "Features",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                // Feature Items (Health Tracker, Nutrition, etc.)
                Row(modifier = Modifier.fillMaxWidth().clickable { navController.navigate(ROUT_HEALTH) }) {
                    FeatureItem(
                        image = painterResource(id = R.drawable.health),
                        title = "Health Tracker",
                        desc = "Monitor vet visits, vaccinations, and medications in one place."
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(modifier = Modifier.fillMaxWidth().clickable { navController.navigate(ROUT_NUTRITION) }) {
                    FeatureItem(
                        image = painterResource(id = R.drawable.nutrition),
                        title = "PetCare Nutrition",
                        desc = "Get feeding tips, portion guides, and healthy treat ideas."
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(modifier = Modifier.fillMaxWidth().clickable { navController.navigate(ROUT_ACTIVITY) }) {
                    FeatureItem(
                        image = painterResource(id = R.drawable.activitylog),
                        title = "Daily Care & Activities",
                        desc = "Track daily routines like walks, playtime, grooming, etc."
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(modifier = Modifier.fillMaxWidth().clickable { navController.navigate(ROUT_MAP) }) {
                    FeatureItem(
                        image = painterResource(id = R.drawable.vetlocator),
                        title = "Emergency Vet Locator",
                        desc = "Find nearby emergency vets instantly, with directions and contact info."
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    )
}

@Composable
fun FeatureItem(image: Painter, title: String, desc: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = image,
                contentDescription = title,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(title, fontWeight = FontWeight.Bold)
                Text(desc, fontSize = 14.sp, color = Color.Gray)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(rememberNavController())
}
