package com.starglen.petcarepro.ui.screens

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalConfiguration
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.starglen.petcarepro.R

// Dynamic theme colors
@Composable
fun AppMainColor(): Color = if (isSystemInDarkTheme()) Color(0xFF90CAF9) else Color(0xFF68929A)
@Composable
fun AppBackground(): Color = if (isSystemInDarkTheme()) Color(0xFF121212) else Color(0xFFFFFFFF)
@Composable
fun AppTextColor(): Color = if (isSystemInDarkTheme()) Color(0xFFFFFFFF) else Color(0xFF000000)
@Composable
fun AppCardColor(): Color = if (isSystemInDarkTheme()) Color(0xFF1E1E1E) else Color.White
@Composable
fun AppSecondaryText(): Color = if (isSystemInDarkTheme()) Color(0xFFB0BEC5) else Color.Gray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityScreen(navController: NavController) {
    val context = LocalContext.current
    val mainColor = AppMainColor()
    val backgroundColor = AppBackground()
    val textColor = AppTextColor()

    val tabs = listOf(
        TabItem("Walk", R.drawable.paw),
        TabItem("Activity Log", R.drawable.activitylog),
        TabItem("Grooming Schedule", R.drawable.grooming),
        TabItem("Behavioral Notes", R.drawable.behaviour)
    )

    var selectedTab by remember { mutableStateOf(tabs.first().title) }

    var activityList by remember { mutableStateOf(listOf("Playtime: 30 mins", "Training: Sit, Stay")) }
    var groomingList by remember {
        mutableStateOf(listOf("Bath: April 15", "Nail Trim: April 20", "Flea Treatment: May 1"))
    }
    var behavioralNotes by remember { mutableStateOf(listOf("Barked at mailman", "Calm during thunder")) }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(backgroundColor)) {

        TopAppBar(
            title = { Text("Daily Care & Activities") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = mainColor,
                titleContentColor = Color.White,
                navigationIconContentColor = Color.White
            ),
            navigationIcon = {
                IconButton(onClick = {
                    navController.navigate("home")
                }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                tabs.forEach { tab ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedTab = tab.title }
                            .padding(8.dp)
                            .background(
                                color = if (selectedTab == tab.title)
                                    mainColor.copy(alpha = 0.2f) else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            )
                    ) {
                        Image(
                            painter = painterResource(id = tab.imageRes),
                            contentDescription = tab.title,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = tab.title,
                            fontSize = 12.sp,
                            color = textColor
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            when (selectedTab) {
                "Walk" -> CardBox { WalkTracker() }
                "Activity Log" -> CardBox {
                    ActivityLog(activityList) {
                        activityList = activityList + it
                        Toast.makeText(context, "Activity added!", Toast.LENGTH_SHORT).show()
                    }
                }
                "Grooming Schedule" -> CardBox {
                    GroomingSchedule(groomingList) {
                        groomingList = groomingList + it
                        Toast.makeText(context, "Schedule added!", Toast.LENGTH_SHORT).show()
                    }
                }
                "Behavioral Notes" -> CardBox {
                    BehavioralNotes(behavioralNotes) {
                        behavioralNotes = behavioralNotes + it
                        Toast.makeText(context, "Note added!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}

@Composable
fun CardBox(content: @Composable ColumnScope.() -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = AppCardColor()),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), content = content)
    }
}

@Composable
fun WalkTracker() {
    val textColor = AppTextColor()
    val mainColor = AppMainColor()

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Walk Tracker", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = textColor)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Distance\n1.2 mi", color = textColor)
            Text("Time\n25 mins", color = textColor)
            TextButton(onClick = { }) {
                Text("View Map", color = mainColor)
            }
        }

        Divider()

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { }, colors = ButtonDefaults.buttonColors(mainColor)) {
                Text("💩 Logged", color = Color.White)
            }
            Button(onClick = { }, colors = ButtonDefaults.buttonColors(mainColor)) {
                Text("💧 Logged", color = Color.White)
            }
        }
    }
}

@Composable
fun ActivityLog(activities: List<String>, onAdd: (String) -> Unit) {
    val textColor = AppTextColor()
    val hintColor = AppSecondaryText()
    val mainColor = AppMainColor()

    var text by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Activity Log", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = textColor)

        activities.forEach {
            Text("• $it", color = textColor)
        }

        Divider()

        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Type activity", color = hintColor) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
                focusedBorderColor = mainColor,
                unfocusedBorderColor = hintColor
            )
        )

        Button(
            onClick = {
                if (text.isNotBlank()) {
                    onAdd(text)
                    text = ""
                }
            },
            colors = ButtonDefaults.buttonColors(mainColor),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Add Activity", color = Color.White)
        }
    }
}

@Composable
fun GroomingSchedule(groomings: List<String>, onAdd: (String) -> Unit) {
    val textColor = AppTextColor()
    val hintColor = AppSecondaryText()
    val mainColor = AppMainColor()

    var text by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Grooming Schedule", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = textColor)

        groomings.forEach {
            Text("• $it", color = textColor)
        }

        Divider()

        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Type grooming task", color = hintColor) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
                focusedBorderColor = mainColor,
                unfocusedBorderColor = hintColor
            )
        )

        Button(
            onClick = {
                if (text.isNotBlank()) {
                    onAdd(text)
                    text = ""
                }
            },
            colors = ButtonDefaults.buttonColors(mainColor),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Add Schedule", color = Color.White)
        }
    }
}

@Composable
fun BehavioralNotes(notes: List<String>, onAdd: (String) -> Unit) {
    val textColor = AppTextColor()
    val hintColor = AppSecondaryText()
    val mainColor = AppMainColor()

    var text by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Behavioral Notes", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = textColor)

        notes.forEach {
            Text("• $it", color = textColor)
        }

        Divider()

        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Add behavior note", color = hintColor) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
                focusedBorderColor = mainColor,
                unfocusedBorderColor = hintColor
            )
        )

        Button(
            onClick = {
                if (text.isNotBlank()) {
                    onAdd(text)
                    text = ""
                }
            },
            colors = ButtonDefaults.buttonColors(mainColor),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Add Note", color = Color.White)
        }
    }
}

data class TabItem(val title: String, val imageRes: Int)

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun LightPreview() {
    ActivityScreen(navController = rememberNavController())
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DarkPreview() {
    ActivityScreen(navController = rememberNavController())
}
