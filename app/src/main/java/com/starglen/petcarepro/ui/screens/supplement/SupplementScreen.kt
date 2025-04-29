package com.starglen.petcarepro.ui.screens.supplement

import androidx.compose.animation.*
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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
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
import com.starglen.petcarepro.ui.theme.maincolor
import com.starglen.petcarepro.ui.theme.newwhite

@Composable fun AppMainColor() = if (isSystemInDarkTheme()) Color(0xFF90CAF9) else Color(0xFF68929A)
@Composable fun AppBackground() = if (isSystemInDarkTheme()) Color(0xFF121212) else Color(0xFFFFFFFF)
@Composable fun AppTextColor() = if (isSystemInDarkTheme()) Color(0xFFFFFFFF) else Color(0xFF000000)
@Composable fun AppCardColor() = if (isSystemInDarkTheme()) Color(0xFF1E1E1E) else Color(0xFFE6F2F3)
@Composable fun AppSecondaryText() = if (isSystemInDarkTheme()) Color(0xFFB0BEC5) else Color.Gray
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupplementScreen(navController: NavController) {
    val mainColor = AppMainColor()
    val bgColor = AppBackground()
    val textColor = AppTextColor()
    val secondaryText = AppSecondaryText()

    val scrollState = rememberScrollState()

    Column (modifier = Modifier.background(bgColor)){

        TopAppBar(
            title = { Text(text = "PetCare Supplement Guide") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = mainColor,
                titleContentColor = newwhite,
                navigationIconContentColor = newwhite
            ),
            navigationIcon = {
                IconButton(onClick = {
                    navController.navigate("nutrition") // Adjust the navigation path as needed
                }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState)
                ,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.petsupplements),
                contentDescription = "Diet Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))



            ExpandableCard(
                image = painterResource(id = R.drawable.joint),
                "Joint Health", mapOf(
                "Glucosamine & Chondroitin" to "Supports cartilage repair and joint mobility. Ideal for aging pets.",
                "Omega-3 Fatty Acids" to "Helps reduce inflammation. Found in fish oil and flaxseed.",
                "Natural Alternative" to "Green-lipped mussels and turmeric are natural anti-inflammatories."
            ))

            ExpandableCard(
                image = painterResource(id = R.drawable.skin),
                "Skin & Coat", mapOf(
                "Fish Oil" to "Great source of EPA & DHA, boosts shine and reduces dryness.",
                "Vitamin E" to "Antioxidant that helps skin healing.",
                "Natural Alternative" to "Coconut oil – improves shine, reduces itching."
            ))

            ExpandableCard(
                image = painterResource(id = R.drawable.degestivehealth),
                "Digestive Health", mapOf(
                "Probiotics" to "Maintain gut flora, reduce diarrhea, boost immunity.",
                "Pumpkin" to "Rich in fiber, helps with both constipation and diarrhea.",
                "Natural Alternative" to "Plain yogurt – contains live cultures beneficial for digestion."
            ))

            ExpandableCard(
                image = painterResource(id = R.drawable.aids),
                "Calming Aids", mapOf(
                "Chamomile" to "Soothes nerves, helps with anxiety.",
                "L-Theanine" to "Promotes relaxation without sedation.",
                "CBD (check legality)" to "May reduce anxiety and pain. Always consult a vet."
            ))

            ExpandableCard(
                image = painterResource(id = R.drawable.multivitamin),
                "Multivitamins", mapOf(
                "General Support" to "Covers daily nutritional gaps. Great for picky eaters or home-cooked diets.",
                "Vitamin B Complex" to "Boosts energy, supports metabolism.",
                "Note" to "Always check for pet-specific formulations!"
            ))

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun ExpandableCard(image: Painter, title: String, items: Map<String, String>) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(6.dp),
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
                Image(
                    painter = image,
                    contentDescription = title,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppTextColor(),
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = null,
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
                        Text(text = content, fontSize = 14.sp, color = AppSecondaryText())
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun SupplementScreenPreview() {
    SupplementScreen(navController = rememberNavController())
}


