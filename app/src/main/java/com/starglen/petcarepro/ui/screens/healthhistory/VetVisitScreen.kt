package com.starglen.petcarepro.ui.screens.healthhistory

import android.app.Application
import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.model.VetVisit
import com.starglen.petcarepro.ui.theme.maincolor
import com.viewmodel.VisitViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VetVisitScreen(
    navController: NavController,
    viewModel: VisitViewModel
) {
    val visits by viewModel.allVetVisits.observeAsState(emptyList())
    var showEditForm by remember { mutableStateOf(false) }
    var selectedVisit by remember { mutableStateOf<VetVisit?>(null) }
    var showAddForm by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🏥 Vet Visit History") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = maincolor),
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate("health")
                    }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddForm = true }, containerColor = maincolor) {
                Icon(Icons.Default.Add, contentDescription = "Add Vet Visit")
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            if (visits.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        "No vet visit history.\nPress + to add one.",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                LazyColumn {
                    items(visits) { visit ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Visit Title: ${visit.title}", fontWeight = FontWeight.Bold)
                                Text("Vet: ${visit.vetName}  •  Date: ${visit.date}")
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Notes: ${visit.note}")

                                Row(
                                    horizontalArrangement = Arrangement.End,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp)
                                ) {
                                    Button(
                                        onClick = {
                                            selectedVisit = visit
                                            showEditForm = true
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB3E5FC))
                                    ) {
                                        Text("Edit")
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Button(
                                        onClick = { viewModel.deleteVetVisit(visit) },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFCDD2))
                                    ) {
                                        Text("Delete")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (showEditForm && selectedVisit != null) {
            EditVetVisitDialog(
                visit = selectedVisit!!,
                onDismiss = { showEditForm = false },
                onSave = {
                    viewModel.updateVetVisit(it)
                    showEditForm = false
                }
            )
        }

        if (showAddForm) {
            AddVetVisitDialog(
                onDismiss = { showAddForm = false },
                onSave = {
                    viewModel.addVetVisit(it)
                    showAddForm = false
                }
            )
        }
    }
}

@Composable
fun AddVetVisitDialog(
    onDismiss: () -> Unit,
    onSave: (VetVisit) -> Unit
) {
    val context = LocalContext.current
    var vetTitle by remember { mutableStateOf("") }
    var vetName by remember { mutableStateOf("") }
    var vetDate by remember { mutableStateOf("") }
    var vetNote by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Vet Visit") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = vetTitle,
                    onValueChange = { vetTitle = it },
                    label = { Text("Visit Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = vetName,
                    onValueChange = { vetName = it },
                    label = { Text("Vet Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = vetDate,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Visit Date") },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = null,
                            tint = maincolor,
                            modifier = Modifier.clickable {
                                val calendar = Calendar.getInstance()
                                val datePickerDialog = DatePickerDialog(
                                    context,
                                    { _: DatePicker, year: Int, month: Int, day: Int ->
                                        vetDate = "%02d/%02d/%04d".format(day, month + 1, year)
                                    },
                                    calendar.get(Calendar.YEAR),
                                    calendar.get(Calendar.MONTH),
                                    calendar.get(Calendar.DAY_OF_MONTH)
                                )
                                datePickerDialog.window?.setBackgroundDrawableResource(android.R.color.white)
                                datePickerDialog.show()
                            }
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = vetNote,
                    onValueChange = { vetNote = it },
                    label = { Text("Visit Notes") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                onSave(
                    VetVisit(
                        id = UUID.randomUUID().hashCode(),
                        title = vetTitle,
                        vetName = vetName,
                        date = vetDate,
                        note = vetNote
                    )
                )
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
fun EditVetVisitDialog(
    visit: VetVisit,
    onDismiss: () -> Unit,
    onSave: (VetVisit) -> Unit
) {
    val context = LocalContext.current
    // Initialize vetNote as a non-nullable String, defaulting to an empty string if it's null
    var vetTitle by remember { mutableStateOf(visit.title) }
    var vetName by remember { mutableStateOf(visit.vetName) }
    var vetDate by remember { mutableStateOf(visit.date) }
    var vetNote by remember { mutableStateOf(visit.note ?: "") } // Handle nullability here

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Vet Visit") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = vetTitle,
                    onValueChange = { vetTitle = it },
                    label = { Text("Visit Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = vetName,
                    onValueChange = { vetName = it },
                    label = { Text("Vet Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = vetDate,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Visit Date") },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = null,
                            tint = maincolor,
                            modifier = Modifier.clickable {
                                val calendar = Calendar.getInstance()
                                val datePickerDialog = DatePickerDialog(
                                    context,
                                    { _: DatePicker, year: Int, month: Int, day: Int ->
                                        vetDate = "%02d/%02d/%04d".format(day, month + 1, year)
                                    },
                                    calendar.get(Calendar.YEAR),
                                    calendar.get(Calendar.MONTH),
                                    calendar.get(Calendar.DAY_OF_MONTH)
                                )
                                datePickerDialog.window?.setBackgroundDrawableResource(android.R.color.white)
                                datePickerDialog.show()
                            }
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Ensure vetNote is treated as a non-nullable String
                OutlinedTextField(
                    value = vetNote, // Now vetNote is guaranteed to be a non-null String
                    onValueChange = { vetNote = it },
                    label = { Text("Visit Notes") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val updatedVisit = visit.copy(
                    title = vetTitle,
                    vetName = vetName,
                    date = vetDate,
                    note = vetNote // vetNote is now non-nullable
                )
                onSave(updatedVisit)
            }) {
                Text("Save Changes")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun VetVisitScreenPreview() {
    // You can use a mock ViewModel for previews
    val mockViewModel = VisitViewModel(LocalContext.current.applicationContext as Application)

    VetVisitScreen(
        navController = rememberNavController(),
        viewModel = mockViewModel
    )
}

