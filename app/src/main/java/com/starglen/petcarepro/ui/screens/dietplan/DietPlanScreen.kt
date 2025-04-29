package com.starglen.petcarepro.ui.screens.dietplan

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.starglen.petcarepro.R
import com.starglen.petcarepro.ui.theme.newwhite

// --------- Theme Aware Colors ----------
@Composable fun AppMainColor() = if (isSystemInDarkTheme()) Color(0xFF90CAF9) else Color(0xFF68929A)
@Composable fun AppBackground() = if (isSystemInDarkTheme()) Color(0xFF121212) else Color(0xFFFFFFFF)
@Composable fun AppTextColor() = if (isSystemInDarkTheme()) Color(0xFFFFFFFF) else Color(0xFF000000)
@Composable fun AppCardColor() = if (isSystemInDarkTheme()) Color(0xFF1E1E1E) else Color(0xFFE6F2F3)
@Composable fun AppSecondaryText() = if (isSystemInDarkTheme()) Color(0xFFB0BEC5) else Color.Gray

// --------- Main Screen ----------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DietPlanScreen(navController: NavController) {
    val scrollState = rememberScrollState()
    var petWeight by remember { mutableStateOf("") }
    var activityLevel by remember { mutableStateOf("") }
    var portionResult by remember { mutableStateOf("") }

    val mainColor = AppMainColor()
    val bgColor = AppBackground()
    val textColor = AppTextColor()
    val secondaryText = AppSecondaryText()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
    ) {
        TopAppBar(
            title = { Text(text = "Personalized Diet Plans") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = mainColor,
                titleContentColor = newwhite,
                navigationIconContentColor = newwhite
            ),
            navigationIcon = {
                IconButton(onClick = {
                    navController.navigate("nutrition")
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.catdognutri),
                contentDescription = "Diet Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )

            ExpandableCard("Meal Plan by Pet Type", mapOf(
                "Dogs" to "• High-protein meals\n• Twice daily feeding\n• Occasional treats (no chocolate!)",
                "Cats" to "• Wet food preferred\n• Requires taurine-rich meals\n• No onions or garlic",
                "Birds" to "• Seeds and pellets\n• Occasional fruits (no avocado!)\n• Clean water daily"
            ))

            ExpandableCard("Based on Life Stage", mapOf(
                "Puppy/Kitten" to "• High-fat, high-calcium meals\n• 3-4 small meals per day",
                "Adult" to "• Balanced proteins and fibers\n• 2 meals/day, portion-controlled",
                "Senior" to "• Low-calorie meals\n• Supplements for joints and digestion"
            ))

            ExpandableCard("Weight Management", mapOf(
                "Weight Gain" to "• High-calorie kibble\n• Frequent meals\n• Add protein supplements",
                "Weight Loss" to "• Low-fat food\n• Reduce snacks\n• Increase activity",
                "Maintain Healthy Weight" to "• Maintain portion size\n• Mix wet and dry food\n• Routine exercise"
            ))

            ExpandableCard("Special Needs", mapOf(
                "Grain-Free / Allergies" to "• Avoid wheat, corn, soy\n• Use single-protein meals",
                "Diabetic-Friendly" to "• Consistent feeding times\n• Low-carb meals\n• Monitor blood sugar",
                "Renal Support" to "• Low-phosphorus diet\n• Prescription renal food\n• Always fresh water"
            ))

            SectionTitle("Feeding Schedule")
            Text(
                "• Morning: 1/3 Daily Portion\n• Evening: 2/3 Daily Portion\n• Fresh Water: Always Available",
                fontSize = 14.sp, color = secondaryText
            )

            SectionTitle("DIY Recipes")
            Text(
                "• Chicken & Rice Bowl\n• Pumpkin Dog Biscuits\n• Tuna & Sweet Potato Mash",
                fontSize = 14.sp, color = secondaryText
            )

            SectionTitle("Portion Calculator")

            OutlinedTextField(
                value = petWeight,
                onValueChange = { petWeight = it },
                label = { Text("Pet Weight (kg)") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = activityLevel,
                onValueChange = { activityLevel = it },
                label = { Text("Activity Level (Low, Medium, High)") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    val weight = petWeight.toFloatOrNull() ?: 0f
                    portionResult = when (activityLevel.lowercase()) {
                        "low" -> "${(weight * 15).toInt()}g/day"
                        "medium" -> "${(weight * 20).toInt()}g/day"
                        "high" -> "${(weight * 25).toInt()}g/day"
                        else -> "Please enter a valid activity level"
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = mainColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Calculate Portion", color = Color.White)
            }

            if (portionResult.isNotEmpty()) {
                Text(
                    "Recommended Portion: $portionResult",
                    fontWeight = FontWeight.SemiBold,
                    color = textColor,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// --------- Expandable Card ----------
@Composable
fun ExpandableCard(title: String, items: Map<String, String>) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = AppCardColor()),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .clickable { expanded = !expanded }
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppTextColor(),
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = "Toggle",
                    tint = AppMainColor()
                )
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier.padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items.forEach { (heading, content) ->
                        Text(
                            text = heading,
                            fontWeight = FontWeight.SemiBold,
                            color = AppMainColor(),
                            fontSize = 16.sp
                        )
                        Text(
                            text = content,
                            fontSize = 14.sp,
                            color = AppSecondaryText()
                        )
                    }
                }
            }
        }
    }
}

// --------- Section Title ----------
@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = AppTextColor()
    )
}

// --------- Preview ----------
@Preview(showBackground = true)
@Composable
fun DietPlanScreenPreview() {
    DietPlanScreen(navController = rememberNavController())
}
