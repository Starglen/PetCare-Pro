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
import com.model.Vaccine
import com.starglen.petcarepro.ui.theme.maincolor
import com.viewmodel.VaccinationViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VaccinationScreen(
    navController: NavController,
    vaccinationViewModel: VaccinationViewModel
) {
    val vaccinations by vaccinationViewModel.allVaccines.observeAsState(emptyList())
    var showEditForm by remember { mutableStateOf(false) }
    var selectedVaccine by remember { mutableStateOf<Vaccine?>(null) }
    var showAddForm by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("💉 Vaccine History") },
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
                Icon(Icons.Default.Add, contentDescription = "Add Vaccine")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            if (vaccinations.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        "No vaccine history.\nPress + to add one.",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                LazyColumn {
                    items(vaccinations) { vaccine ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Vaccine: ${vaccine.vaccineName}", fontWeight = FontWeight.Bold)
                                Text("Disease: ${vaccine.disease}  •  Date: ${vaccine.dateGiven}")
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Manufacturer: ${vaccine.manufacturer}")
                                Text("Notes: ${vaccine.note ?: "No notes available"}")

                                Row(
                                    horizontalArrangement = Arrangement.End,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp)
                                ) {
                                    Button(
                                        onClick = {
                                            selectedVaccine = vaccine
                                            showEditForm = true
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB3E5FC))
                                    ) {
                                        Text("Edit")
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Button(
                                        onClick = {
                                            vaccine.id?.let { vaccinationViewModel.deleteVaccination(vaccine) }
                                        },
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

        // Edit Form
        if (showEditForm && selectedVaccine != null) {
            EditVaccineDialog(
                vaccine = selectedVaccine!!,
                onDismiss = { showEditForm = false },
                onSave = { updatedVaccine ->
                    vaccinationViewModel.updateVaccination(updatedVaccine)
                    showEditForm = false
                }
            )
        }

        // Add Form
        if (showAddForm) {
            AddVaccineDialog(
                onDismiss = { showAddForm = false },
                onSave = { newVaccine ->
                    vaccinationViewModel.addVaccination(newVaccine)
                    showAddForm = false
                }
            )
        }
    }
}

@Composable
fun EditVaccineDialog(
    vaccine: Vaccine,
    onDismiss: () -> Unit,
    onSave: (Vaccine) -> Unit
) {
    var vaccineName by remember { mutableStateOf(vaccine.vaccineName) }
    var diseaseName by remember { mutableStateOf(vaccine.disease) }
    var dateGiven by remember { mutableStateOf(vaccine.dateGiven) }
    var manufacturer by remember { mutableStateOf(vaccine.manufacturer) }
    var note by remember { mutableStateOf(vaccine.note ?: "") }

    VaccineDialogLayout(
        title = "Edit Vaccine",
        vaccineName = vaccineName,
        onVaccineNameChange = { vaccineName = it },
        diseaseName = diseaseName,
        onDiseaseNameChange = { diseaseName = it },
        dateGiven = dateGiven,
        onDateChange = { dateGiven = it },
        manufacturer = manufacturer,
        onManufacturerChange = { manufacturer = it },
        note = note,
        onNoteChange = { note = it },
        onDismiss = onDismiss,
        onSave = {
            onSave(
                vaccine.copy(
                    vaccineName = vaccineName,
                    disease = diseaseName,
                    dateGiven = dateGiven,
                    manufacturer = manufacturer,
                    note = note
                )
            )
        },
        confirmButtonText = "Save Changes"
    )
}

@Composable
fun AddVaccineDialog(
    onDismiss: () -> Unit,
    onSave: (Vaccine) -> Unit
) {
    var vaccineName by remember { mutableStateOf("") }
    var diseaseName by remember { mutableStateOf("") }
    var dateGiven by remember { mutableStateOf("") }
    var manufacturer by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    VaccineDialogLayout(
        title = "Add Vaccine",
        vaccineName = vaccineName,
        onVaccineNameChange = { vaccineName = it },
        diseaseName = diseaseName,
        onDiseaseNameChange = { diseaseName = it },
        dateGiven = dateGiven,
        onDateChange = { dateGiven = it },
        manufacturer = manufacturer,
        onManufacturerChange = { manufacturer = it },
        note = note,
        onNoteChange = { note = it },
        onDismiss = onDismiss,
        onSave = {
            onSave(
                Vaccine(
                    id = UUID.randomUUID().hashCode(),
                    vaccineName = vaccineName,
                    disease = diseaseName,
                    dateGiven = dateGiven,
                    manufacturer = manufacturer,
                    note = note
                )
            )
        },
        confirmButtonText = "Save"
    )
}

@Composable
fun VaccineDialogLayout(
    title: String,
    vaccineName: String,
    onVaccineNameChange: (String) -> Unit,
    diseaseName: String,
    onDiseaseNameChange: (String) -> Unit,
    dateGiven: String,
    onDateChange: (String) -> Unit,
    manufacturer: String,
    onManufacturerChange: (String) -> Unit,
    note: String,
    onNoteChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit,
    confirmButtonText: String
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = vaccineName,
                    onValueChange = onVaccineNameChange,
                    label = { Text("Vaccine Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = diseaseName,
                    onValueChange = onDiseaseNameChange,
                    label = { Text("Disease") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = dateGiven,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Date Given") },
                    trailingIcon = {
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = null,
                            tint = maincolor,
                            modifier = Modifier.clickable {
                                val calendar = Calendar.getInstance()
                                val datePickerDialog = DatePickerDialog(
                                    context,
                                    { _: DatePicker, year: Int, month: Int, day: Int ->
                                        val formatted = "%02d/%02d/%04d".format(day, month + 1, year)
                                        onDateChange(formatted)
                                    },
                                    calendar.get(Calendar.YEAR),
                                    calendar.get(Calendar.MONTH),
                                    calendar.get(Calendar.DAY_OF_MONTH)
                                )
                                datePickerDialog.show()
                            }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = manufacturer,
                    onValueChange = onManufacturerChange,
                    label = { Text("Manufacturer") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = note,
                    onValueChange = onNoteChange,
                    label = { Text("Notes") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = onSave) {
                Text(confirmButtonText)
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
fun VaccinationScreenPreview() {
    // You can use a mock ViewModel for previews
    val mockViewModel = VaccinationViewModel(LocalContext.current.applicationContext as Application)
    VaccinationScreen(
        navController = rememberNavController(),
        vaccinationViewModel = mockViewModel
    )
}
