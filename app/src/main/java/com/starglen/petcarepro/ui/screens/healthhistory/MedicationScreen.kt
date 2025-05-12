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
import com.model.Medication
import com.starglen.petcarepro.ui.theme.maincolor
import com.viewmodel.MedicationViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationScreen(
    navController: NavController,
    medicationViewModel: MedicationViewModel
) {
    // Observe medications from ViewModel
    val medications by medicationViewModel.allMedications.observeAsState(initial = emptyList())

    var showEditForm by remember { mutableStateOf(false) }
    var selectedMedication by remember { mutableStateOf<Medication?>(null) }
    var showAddForm by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }  // State to show delete dialog
    var medicationToDelete by remember { mutableStateOf<Medication?>(null) }  // State to hold medication to delete

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("💊 Medication History") },
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
                Icon(Icons.Default.Add, contentDescription = "Add Medication")
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            if (medications.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No medication history.\nPress + to add one.", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                LazyColumn {
                    items(medications) { med ->
                        MedicationCard(
                            medication = med,
                            onEditClick = {
                                selectedMedication = med
                                showEditForm = true
                            },
                            onDeleteClick = {
                                medicationToDelete = med
                                showDeleteDialog = true  // Show delete confirmation dialog
                            }
                        )
                    }
                }
            }
        }

        if (showEditForm && selectedMedication != null) {
            EditMedicationDialog(
                medication = selectedMedication!!,
                onDismiss = { showEditForm = false },
                onSave = {
                    medicationViewModel.updateMedication(it)
                    showEditForm = false
                }
            )
        }

        if (showAddForm) {
            AddMedicationDialog(
                onDismiss = { showAddForm = false },
                onSave = {
                    medicationViewModel.addMedication(it)
                    showAddForm = false
                }
            )
        }

        // Deletion Confirmation Dialog
        if (showDeleteDialog && medicationToDelete != null) {
            DeleteConfirmationDialog(
                onDismiss = { showDeleteDialog = false },
                onDelete = {
                    medicationToDelete?.let {
                        medicationViewModel.deleteMedication(it)
                    }
                    showDeleteDialog = false
                }
            )
        }
    }
}

@Composable
fun MedicationCard(
    medication: Medication,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Medication: ${medication.medicationName}", fontWeight = FontWeight.Bold)
            Text("Dosage: ${medication.dosage}")
            Text("Schedule: ${medication.schedule}")
            Text("Start Date: ${medication.startDate}")
            Text("Notes: ${medication.notes ?: "No notes available"}")

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Button(
                    onClick = onEditClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB3E5FC))
                ) {
                    Text("Edit")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = onDeleteClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFCDD2))
                ) {
                    Text("Delete")
                }
            }
        }
    }
}

@Composable
fun AddMedicationDialog(
    onDismiss: () -> Unit,
    onSave: (Medication) -> Unit
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var dosage by remember { mutableStateOf("") }
    var schedule by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Medication") },
        text = {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Medication Name") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = dosage, onValueChange = { dosage = it }, label = { Text("Dosage") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = schedule, onValueChange = { schedule = it }, label = { Text("Schedule") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = startDate,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Start Date") },
                    trailingIcon = {
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = null,
                            tint = maincolor,
                            modifier = Modifier.clickable {
                                val calendar = Calendar.getInstance()
                                DatePickerDialog(
                                    context,
                                    { _: DatePicker, year: Int, month: Int, day: Int ->
                                        startDate = "%02d/%02d/%04d".format(day, month + 1, year)
                                    },
                                    calendar.get(Calendar.YEAR),
                                    calendar.get(Calendar.MONTH),
                                    calendar.get(Calendar.DAY_OF_MONTH)
                                ).show()
                            }
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = notes, onValueChange = { notes = it }, label = { Text("Notes") }, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            Button(onClick = {
                onSave(
                    Medication(
                        id = UUID.randomUUID().hashCode(),
                        medicationName = name,
                        dosage = dosage,
                        schedule = schedule,
                        startDate = startDate,
                        notes = notes
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
fun EditMedicationDialog(
    medication: Medication,
    onDismiss: () -> Unit,
    onSave: (Medication) -> Unit
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf(medication.medicationName) }
    var dosage by remember { mutableStateOf(medication.dosage) }
    var schedule by remember { mutableStateOf(medication.schedule) }
    var startDate by remember { mutableStateOf(medication.startDate) }
    var notes by remember { mutableStateOf(medication.notes ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Medication") },
        text = {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Medication Name") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = dosage, onValueChange = { dosage = it }, label = { Text("Dosage") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = schedule, onValueChange = { schedule = it }, label = { Text("Schedule") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = startDate,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Start Date") },
                    trailingIcon = {
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = null,
                            tint = maincolor,
                            modifier = Modifier.clickable {
                                val calendar = Calendar.getInstance()
                                DatePickerDialog(
                                    context,
                                    { _: DatePicker, year: Int, month: Int, day: Int ->
                                        startDate = "%02d/%02d/%04d".format(day, month + 1, year)
                                    },
                                    calendar.get(Calendar.YEAR),
                                    calendar.get(Calendar.MONTH),
                                    calendar.get(Calendar.DAY_OF_MONTH)
                                ).show()
                            }
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = notes, onValueChange = { notes = it }, label = { Text("Notes") }, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            Button(onClick = {
                val updated = medication.copy(
                    medicationName = name,
                    dosage = dosage,
                    schedule = schedule,
                    startDate = startDate,
                    notes = notes
                )
                onSave(updated)
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

@Composable
fun DeleteConfirmationDialog(
    onDismiss: () -> Unit,
    onDelete: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirm Deletion") },
        text = { Text("Are you sure you want to delete this medication? This action cannot be undone.") },
        confirmButton = {
            Button(onClick = onDelete) {
                Text("Delete")
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
fun MedicationScreenPreview() {
    val mockViewModel = MedicationViewModel(LocalContext.current.applicationContext as Application)
    MedicationScreen(rememberNavController(), medicationViewModel = mockViewModel)
}
