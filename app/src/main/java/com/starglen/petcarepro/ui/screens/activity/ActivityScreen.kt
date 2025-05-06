package com.starglen.petcarepro.ui.screens

import android.os.Build
import android.content.Context
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.model.Activity
import com.model.Behavior
import com.model.Grooming
import com.viewmodel.ActivityViewModel

@Composable fun AppMainColor(): Color = if (isSystemInDarkTheme()) Color(0xFF90CAF9) else Color(0xFF00796B)
@Composable fun AppBackground(): Color = if (isSystemInDarkTheme()) Color(0xFF121212) else Color(0xFFF5F5F5)
@Composable fun AppTextColor(): Color = if (isSystemInDarkTheme()) Color(0xFFFFFFFF) else Color(0xFF212121)
@Composable fun AppCardColor(): Color = if (isSystemInDarkTheme()) Color(0xFF1E1E1E) else Color.White
@Composable fun AppSecondaryText(): Color = if (isSystemInDarkTheme()) Color(0xFFB0BEC5) else Color.Gray

@RequiresApi(Build.VERSION_CODES.N)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityScreen(viewModel: ActivityViewModel = viewModel(), navController: NavController) {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Activity", "Grooming", "Behavior")
    val mainColor = AppMainColor()

    Scaffold(
        containerColor = AppBackground(),
        topBar = {
            TopAppBar(
                title = { Text("Pet Care Logs", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = mainColor)
            )
        },
        floatingActionButton = {
            AddEntryFAB(viewModel, tabs[selectedTab])
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(AppBackground())
        ) {
            TabRow(selectedTabIndex = selectedTab, containerColor = mainColor) {
                tabs.forEachIndexed { index, tab ->
                    Tab(
                        text = { Text(tab, color = Color.White) },
                        selected = selectedTab == index,
                        onClick = { selectedTab = index }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            when (tabs[selectedTab]) {
                "Activity" -> ActivitySection(viewModel, context)
                "Grooming" -> GroomingSection(viewModel, context)
                "Behavior" -> BehaviorSection(viewModel, context)
            }
        }
    }
}

@Composable
fun AddEntryFAB(viewModel: ActivityViewModel, selectedTab: String) {
    var showDialog by remember { mutableStateOf(false) }

    FloatingActionButton(onClick = { showDialog = true }, containerColor = AppMainColor()) {
        Icon(Icons.Default.Add, contentDescription = "Add Entry", tint = Color.White)
    }

    if (showDialog) {
        when (selectedTab) {
            "Activity" -> AddActivityDialog(viewModel) { showDialog = false }
            "Grooming" -> AddGroomingDialog(viewModel) { showDialog = false }
            "Behavior" -> AddBehaviorDialog(viewModel) { showDialog = false }
        }
    }
}

@Composable
fun AddActivityDialog(viewModel: ActivityViewModel, onDismiss: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var text by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                if (title.isNotBlank() && text.isNotBlank()) {
                    viewModel.addActivity(title, text)
                    onDismiss()
                }
            }) { Text("Add") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
        title = { Text("New Activity Entry") },
        text = {
            Column {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
                OutlinedTextField(value = text, onValueChange = { text = it }, label = { Text("Details") })
            }
        }
    )
}

@Composable
fun AddGroomingDialog(viewModel: ActivityViewModel, onDismiss: () -> Unit) {
    var task by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                if (task.isNotBlank() && status.isNotBlank()) {
                    viewModel.addGrooming(task, status)
                    onDismiss()
                }
            }) { Text("Add") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
        title = { Text("New Grooming Task") },
        text = {
            Column {
                OutlinedTextField(value = task, onValueChange = { task = it }, label = { Text("Task") })
                OutlinedTextField(value = status, onValueChange = { status = it }, label = { Text("Status") })
            }
        }
    )
}

@Composable
fun AddBehaviorDialog(viewModel: ActivityViewModel, onDismiss: () -> Unit) {
    var behavior by remember { mutableStateOf("") }
    var reaction by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                if (behavior.isNotBlank() && reaction.isNotBlank()) {
                    viewModel.addBehavior(behavior, reaction, notes)
                    onDismiss()
                }
            }) { Text("Add") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
        title = { Text("New Behavioral Note") },
        text = {
            Column {
                OutlinedTextField(value = behavior, onValueChange = { behavior = it }, label = { Text("Behavior") })
                OutlinedTextField(value = reaction, onValueChange = { reaction = it }, label = { Text("Reaction") })
                OutlinedTextField(value = notes, onValueChange = { notes = it }, label = { Text("Notes (Optional)") })
            }
        }
    )
}

@Composable
fun ActivitySection(viewModel: ActivityViewModel, context: Context) {
    val list = viewModel.activityLog
    LazyColumn {
        items(list) { item ->
            ExpandableCard(title = item.title, subtitle = item.text, date = item.date,
                onEdit = { t, s -> viewModel.editActivity(item.id, t, s) },
                onDelete = { viewModel.deleteActivity(item.id) }
            )
        }
    }
}

@Composable
fun GroomingSection(viewModel: ActivityViewModel, context: Context) {
    val list = viewModel.groomingList
    LazyColumn {
        items(list) { item ->
            ExpandableCard(title = item.task, subtitle = "Status: ${item.status}", date = item.date,
                onEdit = { t, s -> viewModel.editGrooming(item.id, t, s) },
                onDelete = { viewModel.deleteGrooming(item.id) }
            )
        }
    }
}

@Composable
fun BehaviorSection(viewModel: ActivityViewModel, context: Context) {
    val list = viewModel.behavioralNotes
    LazyColumn {
        items(list) { item ->
            ExpandableCard(title = item.behavior, subtitle = "Reaction: ${item.reaction}\nNotes: ${item.notes}", date = item.date,
                onEdit = { r, n -> viewModel.editBehavior(item.id, item.behavior, r, n) },
                onDelete = { viewModel.deleteBehavior(item.id) }
            )
        }
    }
}

@Composable
fun ExpandableCard(
    title: String,
    subtitle: String,
    date: String,
    onEdit: (String, String) -> Unit,
    onDelete: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, AppMainColor(), RoundedCornerShape(12.dp))
            .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(containerColor = AppCardColor()),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = AppTextColor())
            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(subtitle, color = AppSecondaryText())
                Spacer(modifier = Modifier.height(8.dp))
                Text(date, style = MaterialTheme.typography.bodySmall, color = AppSecondaryText())
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = { onEdit(title, subtitle) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = AppMainColor())
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.N)
@Preview(showBackground = true)
@Composable
fun ActivityScreenPreview() {
    ActivityScreen(navController = rememberNavController())
}
