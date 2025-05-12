package com.starglen.petcarepro.ui.screens.nutrition

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.starglen.petcarepro.R
import com.starglen.petcarepro.ui.screens.dietplan.AppBackground
import com.starglen.petcarepro.ui.screens.dietplan.AppMainColor
import com.starglen.petcarepro.ui.screens.dietplan.AppSecondaryText
import com.starglen.petcarepro.ui.screens.dietplan.AppTextColor
import com.starglen.petcarepro.ui.screens.supplement.AppCardColor
import com.starglen.petcarepro.ui.theme.maincolor
import com.starglen.petcarepro.ui.theme.newwhite

@Composable fun AppMainColor() = if (isSystemInDarkTheme()) Color(0xFF90CAF9) else Color(0xFF68929A)
@Composable fun AppBackground() = if (isSystemInDarkTheme()) Color(0xFF121212) else Color(0xFFFFFFFF)
@Composable fun AppTextColor() = if (isSystemInDarkTheme()) Color(0xFFFFFFFF) else Color(0xFF000000)
@Composable fun AppCardColor() = if (isSystemInDarkTheme()) Color(0xFF1E1E1E) else Color(0xFFE6F2F3)
@Composable fun AppSecondaryText() = if (isSystemInDarkTheme()) Color(0xFFB0BEC5) else Color.Gray

// Define your route constants (can be moved to a Routes.kt file)
const val ROUTE_HOME = "home"
const val ROUTE_DIET = "diet"
const val ROUTE_SUPPLEMENTS = "supplements"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NutritionScreen(navController: NavController) {
    val mainColor = AppMainColor()
    val bgColor = AppBackground()
    val textColor = AppTextColor()
    val secondaryText = AppSecondaryText()

    val scrollState = rememberScrollState()

    Column (modifier = Modifier.background(bgColor)){
        TopAppBar(
            title = { Text(text = "PetCare Nutrition") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = mainColor,
                titleContentColor = newwhite,
                navigationIconContentColor = newwhite
            ),
            navigationIcon = {
                IconButton(onClick = {
                    navController.navigate(ROUTE_HOME)
                }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.catdognutri),
                contentDescription = "Pet Nutrition Image",
                modifier = Modifier.size(200.dp)
            )

            Text(
                text = "Proper nutrition is essential for your pet's health. Learn about the best feeding practices, nutrition plans, and supplements to keep your pet healthy and energetic.",
                fontSize = 16.sp,
                color = textColor,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Nutrition Tips for Your Pet:",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = mainColor,
                modifier = Modifier.fillMaxWidth()
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("• Ensure balanced meals with proteins, fats, and carbohydrates.", color = textColor)
                Text("• Provide fresh water throughout the day.", color = textColor)
                Text("• Avoid harmful foods like chocolate, onions, and grapes.", color = textColor)
                Text("• Regularly monitor weight and health to adjust diet.", color = textColor)
            }

            // Nutrition Feature Card
            NutritionCard(
                title = "Diet Plans",
                description = "Explore specific meal plans for your pet's age, breed, and size.",
                imageResId = R.drawable.petdiet, // Add this image to your drawable folder
                onClick = {
                    navController.navigate(ROUTE_DIET)
                }
            )

            // Supplement Feature Card
            NutritionCard(
                title = "Supplements",
                description = "Check out recommended supplements to support your pet's health.",
                imageResId = R.drawable.petsupplements, // Add this image to your drawable folder
                onClick = {
                    navController.navigate(ROUTE_SUPPLEMENTS)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun NutritionCard(
    title: String,
    description: String,
    imageResId: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = AppCardColor()),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppMainColor()
                )
                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = AppTextColor(),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NutritionScreenPreview() {
    NutritionScreen(navController = rememberNavController())
}
