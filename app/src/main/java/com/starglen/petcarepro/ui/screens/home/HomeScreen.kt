package com.starglen.petcarepro.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.navigation.ROUT_ACTIVITY
import com.navigation.ROUT_HEALTH
import com.navigation.ROUT_MAP
import com.navigation.ROUT_NUTRITION
import com.navigation.ROUT_REMINDER
import kotlinx.coroutines.launch

private val Forest = Color(0xFF176B5B)
private val Mint = Color(0xFFE7F3EE)
private val Ink = Color(0xFF1E2D2A)
private val Muted = Color(0xFF697774)
private val Canvas = Color(0xFFF7F8F5)

private data class CareTask(val title: String, val detail: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    var showQuickActions by remember { mutableStateOf(false) }
    var showReminderSheet by remember { mutableStateOf(false) }
    var showActivitySheet by remember { mutableStateOf(false) }
    var showPetPicker by remember { mutableStateOf(false) }
    var reminderTitle by remember { mutableStateOf("") }
    var reminderTime by remember { mutableStateOf("") }
    var activityTitle by remember { mutableStateOf("") }
    var selectedPet by remember { mutableStateOf("Milo") }
    var selectedTab by remember { mutableStateOf(0) }
    val tasks = remember {
        mutableStateListOf(
            CareTask("Morning walk", "Today · 8:00 AM"),
            CareTask("Flea treatment", "Tomorrow · 9:00 AM")
        )
    }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    Scaffold(
        containerColor = Canvas,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showQuickActions = true },
                containerColor = Forest,
                contentColor = Color.White,
                shape = CircleShape
            ) { Icon(Icons.Default.Add, contentDescription = "Add care item") }
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                val tabs = listOf("Home", "Health", "Activity", "More")
                val icons = listOf(Icons.Default.Pets, Icons.Default.FavoriteBorder, Icons.Default.TaskAlt, Icons.Default.CalendarMonth)
                tabs.forEachIndexed { index, label ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = {
                            selectedTab = index
                            when (index) {
                                1 -> navController.navigate(ROUT_HEALTH)
                                2 -> navController.navigate(ROUT_ACTIVITY)
                                3 -> navController.navigate(ROUT_NUTRITION)
                            }
                        },
                        icon = { Icon(icons[index], contentDescription = label) },
                        label = { Text(label) }
                    )
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("GOOD MORNING", style = MaterialTheme.typography.labelMedium, color = Muted, fontWeight = FontWeight.Bold)
                        Text("Let’s care for ${selectedPet}", style = MaterialTheme.typography.headlineSmall, color = Ink, fontWeight = FontWeight.Bold)
                    }
                    Surface(
                        shape = CircleShape,
                        color = Mint,
                        modifier = Modifier.clickable { showPetPicker = true }
                    ) {
                        Row(Modifier.padding(horizontal = 12.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Pets, contentDescription = null, tint = Forest, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("Change", color = Forest, style = MaterialTheme.typography.labelLarge)
                        }
                    }
                }
            }
            item {
                Card(shape = RoundedCornerShape(28.dp), colors = CardDefaults.cardColors(containerColor = Forest)) {
                    Column(Modifier.fillMaxWidth().padding(22.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(shape = CircleShape, color = Color.White.copy(alpha = 0.16f), modifier = Modifier.size(56.dp)) {
                                Box(contentAlignment = Alignment.Center) { Icon(Icons.Default.Pets, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp)) }
                            }
                            Spacer(Modifier.width(14.dp))
                            Column {
                                Text(selectedPet, style = MaterialTheme.typography.titleLarge, color = Color.White, fontWeight = FontWeight.Bold)
                                Text("Happy, healthy, and ready to play", style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = .8f))
                            }
                        }
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            PetStat("AGE", "3 years")
                            PetStat("WEIGHT", "12.4 kg")
                            PetStat("NEXT CHECKUP", "Jun 18")
                        }
                    }
                }
            }
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Today’s care", style = MaterialTheme.typography.titleLarge, color = Ink, fontWeight = FontWeight.Bold)
                    TextButton(onClick = { navController.navigate(ROUT_REMINDER) }) { Text("See all", color = Forest) }
                }
            }
            items(tasks) { task ->
                Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Surface(shape = RoundedCornerShape(14.dp), color = Mint, modifier = Modifier.size(46.dp)) {
                            Box(contentAlignment = Alignment.Center) { Icon(Icons.Default.Schedule, contentDescription = null, tint = Forest) }
                        }
                        Spacer(Modifier.width(14.dp))
                        Column(Modifier.weight(1f)) {
                            Text(task.title, style = MaterialTheme.typography.titleMedium, color = Ink, fontWeight = FontWeight.SemiBold)
                            Text(task.detail, style = MaterialTheme.typography.bodySmall, color = Muted)
                        }
                        Icon(Icons.Default.NotificationsActive, contentDescription = "Reminder", tint = Forest)
                    }
                }
            }
            item {
                Text("Quick access", style = MaterialTheme.typography.titleLarge, color = Ink, fontWeight = FontWeight.Bold)
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    QuickLink("Health", "Records & care", Modifier.weight(1f)) { navController.navigate(ROUT_HEALTH) }
                    QuickLink("Nutrition", "Meals & guides", Modifier.weight(1f)) { navController.navigate(ROUT_NUTRITION) }
                }
                Spacer(Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    QuickLink("Find a vet", "Nearby clinics", Modifier.weight(1f)) { navController.navigate(ROUT_MAP) }
                    QuickLink("Reminders", "Stay on schedule", Modifier.weight(1f)) { navController.navigate(ROUT_REMINDER) }
                }
            }
            item { Spacer(Modifier.height(50.dp)) }
        }
    }

    if (showQuickActions) {
        ModalBottomSheet(onDismissRequest = { showQuickActions = false }, sheetState = sheetState, containerColor = Color.White) {
            Column(Modifier.fillMaxWidth().padding(horizontal = 24.dp).padding(bottom = 28.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Quick care", style = MaterialTheme.typography.headlineSmall, color = Ink, fontWeight = FontWeight.Bold)
                Text("What would you like to add for $selectedPet?", style = MaterialTheme.typography.bodyMedium, color = Muted)
                QuickActionRow("Log an activity", "Walk, play, grooming", Icons.Default.TaskAlt) {
                    showQuickActions = false
                    showActivitySheet = true
                }
                QuickActionRow("Add a reminder", "Medication, meal, appointment", Icons.Default.NotificationsActive) {
                    showQuickActions = false
                    showReminderSheet = true
                }
                QuickActionRow("Health record", "Visit, vaccine, or medication", Icons.Default.FavoriteBorder) {
                    showQuickActions = false
                    navController.navigate(ROUT_HEALTH)
                }
            }
        }
    }

    if (showReminderSheet) {
        ModalBottomSheet(onDismissRequest = { showReminderSheet = false }, containerColor = Color.White) {
            Column(Modifier.fillMaxWidth().padding(horizontal = 24.dp).padding(bottom = 28.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text("New reminder", style = MaterialTheme.typography.headlineSmall, color = Ink, fontWeight = FontWeight.Bold)
                Text("A gentle nudge for ${selectedPet}’s care routine.", style = MaterialTheme.typography.bodyMedium, color = Muted)
                OutlinedTextField(value = reminderTitle, onValueChange = { reminderTitle = it }, label = { Text("Reminder title") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                OutlinedTextField(value = reminderTime, onValueChange = { reminderTime = it }, label = { Text("Date and time (e.g. Today, 6:00 PM)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                Button(
                    onClick = {
                        if (reminderTitle.isNotBlank()) {
                            tasks.add(CareTask(reminderTitle.trim(), reminderTime.ifBlank { "Time not set" }))
                            reminderTitle = ""
                            reminderTime = ""
                            showReminderSheet = false
                        }
                    },
                    enabled = reminderTitle.isNotBlank(),
                    modifier = Modifier.fillMaxWidth().height(54.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Forest)
                ) { Text("Save reminder") }
            }
        }
    }

    if (showActivitySheet) {
        ModalBottomSheet(onDismissRequest = { showActivitySheet = false }, containerColor = Color.White) {
            Column(Modifier.fillMaxWidth().padding(horizontal = 24.dp).padding(bottom = 28.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text("Log an activity", style = MaterialTheme.typography.headlineSmall, color = Ink, fontWeight = FontWeight.Bold)
                Text("Keep a simple record of ${selectedPet}’s day.", style = MaterialTheme.typography.bodyMedium, color = Muted)
                OutlinedTextField(value = activityTitle, onValueChange = { activityTitle = it }, label = { Text("Activity (walk, play, grooming…)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                Button(
                    onClick = {
                        if (activityTitle.isNotBlank()) {
                            tasks.add(0, CareTask(activityTitle.trim(), "Logged just now"))
                            activityTitle = ""
                            showActivitySheet = false
                        }
                    },
                    enabled = activityTitle.isNotBlank(),
                    modifier = Modifier.fillMaxWidth().height(54.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Forest)
                ) { Text("Save activity") }
            }
        }
    }

    if (showPetPicker) {
        AlertDialog(
            onDismissRequest = { showPetPicker = false },
            title = { Text("Choose a pet") },
            text = {
                Column {
                    listOf("Milo", "Luna", "Max").forEach { pet ->
                        TextButton(onClick = { selectedPet = pet; showPetPicker = false }, modifier = Modifier.fillMaxWidth()) {
                            Text(pet, modifier = Modifier.fillMaxWidth())
                        }
                    }
                }
            },
            confirmButton = { TextButton(onClick = { showPetPicker = false }) { Text("Done") } }
        )
    }
}

@Composable
private fun PetStat(label: String, value: String) {
    Column {
        Text(label, style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = .7f))
        Text(value, style = MaterialTheme.typography.labelLarge, color = Color.White, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun QuickLink(title: String, subtitle: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, color = Forest, fontWeight = FontWeight.Bold)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = Muted)
        }
    }
}

@Composable
private fun QuickActionRow(title: String, description: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick), shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Canvas)) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = Forest, modifier = Modifier.size(24.dp))
            Spacer(Modifier.width(14.dp))
            Column {
                Text(title, style = MaterialTheme.typography.titleMedium, color = Ink, fontWeight = FontWeight.SemiBold)
                Text(description, style = MaterialTheme.typography.bodySmall, color = Muted)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    HomeScreen(rememberNavController())
}
