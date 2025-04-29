package com.starglen.petcarepro.ui.screens.health

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.starglen.petcarepro.R
import java.text.SimpleDateFormat
import java.util.*

@Composable fun AppMainColor() = if (isSystemInDarkTheme()) Color(0xFF90CAF9) else Color(0xFF68929A)
@Composable fun AppBackground() = if (isSystemInDarkTheme()) Color(0xFF121212) else Color(0xFFFFFFFF)
@Composable fun AppTextColor() = if (isSystemInDarkTheme()) Color(0xFFFFFFFF) else Color(0xFF000000)
@Composable fun AppCardColor() = if (isSystemInDarkTheme()) Color(0xFF1E1E1E) else Color(0xFFE6F2F3)
@Composable fun AppSecondaryText() = if (isSystemInDarkTheme()) Color(0xFFB0BEC5) else Color.Gray

data class VaccineRecord(
    val name: String,
    val date: String,
    val disease: String,
    val manufacturer: String,
    val notes: String
)

data class Medication(
    val name: String,
    val dosage: String,
    val schedule: String,
    val startDate: String
)

data class VetVisit(
    val note: String,
    val date: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthScreen(navController: NavController) {

    val mainColor = AppMainColor()
    val bgColor = AppBackground()
    val textColor = AppTextColor()
    val secondaryText = AppSecondaryText()
    val context = LocalContext.current

    var vetVisitDate by remember { mutableStateOf("April 15, 2025") }
    var distemperDueDate by remember { mutableStateOf("April 20") }

    // UI Toggles
    var showVaccineHistory by remember { mutableStateOf(false) }
    var showAddMedication by remember { mutableStateOf(false) }
    var showVetReminder by remember { mutableStateOf(false) }

    // Vaccine form state
    val vaccineHistory = remember { mutableStateListOf<VaccineRecord>() }
    var newVaccineName by remember { mutableStateOf(TextFieldValue()) }
    var newVaccineDisease by remember { mutableStateOf(TextFieldValue()) }
    var newVaccineManufacturer by remember { mutableStateOf(TextFieldValue()) }
    var newVaccineNotes by remember { mutableStateOf(TextFieldValue()) }
    var newVaccineDate by remember { mutableStateOf("") }

    // Medication form state
    val medications = remember { mutableStateListOf<Medication>() }
    var medName by remember { mutableStateOf(TextFieldValue()) }
    var medDosage by remember { mutableStateOf(TextFieldValue()) }
    var medSchedule by remember { mutableStateOf(TextFieldValue()) }
    var medStartDate by remember { mutableStateOf("") }

    // Vet visit form state
    val vetVisits = remember { mutableStateListOf<VetVisit>() }
    var vetNote by remember { mutableStateOf(TextFieldValue()) }
    var vetReminderDate by remember { mutableStateOf("") }
    var vetReminderDetails by remember { mutableStateOf(TextFieldValue()) }

    Column(
        modifier = Modifier.background(bgColor)
    ) {

        TopAppBar(
            title = { Text(text = "Health Tracker") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = mainColor,
                titleContentColor = Color.White,
                navigationIconContentColor = Color.White
            ),
            navigationIcon = {
                IconButton(onClick = {
                    navController.navigate("home") // Adjust the navigation path as needed
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
                .verticalScroll(rememberScrollState())
        ) {
            Text("Stay on top of your pet’s health with ease.", style = MaterialTheme.typography.bodyMedium, fontSize = 16.sp, color = AppTextColor())
            Spacer(modifier = Modifier.height(20.dp))

            // --- VET VISITS ---
            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(R.drawable.vetvisit),
                            contentDescription = "",
                            modifier = Modifier
                                .size(28.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Vet Visits", fontWeight = FontWeight.Bold)
                    }
                    Text("Track upcoming and past vet appointments.")
                    Text("Next visit: $vetVisitDate")

                    TextButton(onClick = { showVetReminder = !showVetReminder }) {
                        Text(if (showVetReminder) "Hide Visit Input" else "Add Visit")
                    }

                    AnimatedVisibility(visible = showVetReminder, enter = expandVertically()) {
                        Column {
                            OutlinedTextField(
                                value = vetNote,
                                onValueChange = { vetNote = it },
                                label = { Text("Visit Note") },
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                            )

                            OutlinedTextField(
                                value = TextFieldValue(vetReminderDate),
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Visit Date") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { showDatePicker(context) { vetReminderDate = it } }
                                    .padding(vertical = 4.dp)
                            )

                            OutlinedTextField(
                                value = vetReminderDetails,
                                onValueChange = { vetReminderDetails = it },
                                label = { Text("Reminder Details") },
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                            )

                            Button(
                                onClick = {
                                    if (vetReminderDate.isNotBlank()) {
                                        vetVisits.add(VetVisit(vetNote.text, vetReminderDate))
                                        vetVisitDate = vetReminderDate
                                        vetNote = TextFieldValue()
                                        vetReminderDate = ""
                                        vetReminderDetails = TextFieldValue()
                                    }
                                },
                                modifier = Modifier.align(Alignment.End).padding(top = 4.dp)
                            ) {
                                Text("Save Visit")
                            }
                        }
                    }
                }
            }

            // --- VACCINATIONS ---
            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(R.drawable.vaccine),
                            contentDescription = "",
                            modifier = Modifier
                                .size(28.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Vaccinations", fontWeight = FontWeight.Bold)
                    }
                    Text("✔ Rabies")
                    Text("✔ Distemper - Due $distemperDueDate")

                    TextButton(onClick = { showVaccineHistory = !showVaccineHistory }) {
                        Text(if (showVaccineHistory) "Hide Vaccine input" else "Add vaccine")
                    }

                    AnimatedVisibility(visible = showVaccineHistory, enter = expandVertically()) {
                        Column {
                            vaccineHistory.forEach {
                                ListItem(
                                    headlineContent = { Text(it.name) },
                                    supportingContent = {
                                        Text("For: ${it.disease} • ${it.date} • ${it.manufacturer} • ${it.notes}")
                                    },
                                    leadingContent = { Icon(Icons.Default.Info, contentDescription = null) }
                                )
                            }

                            Text("Add New Vaccine", fontWeight = FontWeight.Bold)
                            OutlinedTextField(value = newVaccineName, onValueChange = { newVaccineName = it }, label = { Text("Vaccine Name") }, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp))
                            OutlinedTextField(value = newVaccineDisease, onValueChange = { newVaccineDisease = it }, label = { Text("Disease") }, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp))
                            OutlinedTextField(value = newVaccineManufacturer, onValueChange = { newVaccineManufacturer = it }, label = { Text("Manufacturer (optional)") }, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp))

                            OutlinedTextField(
                                value = TextFieldValue(newVaccineDate),
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Vaccine Date") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { showDatePicker(context) { newVaccineDate = it } }
                                    .padding(vertical = 4.dp)
                            )

                            OutlinedTextField(value = newVaccineNotes, onValueChange = { newVaccineNotes = it }, label = { Text("Notes") }, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp))

                            Button(
                                onClick = {
                                    if (newVaccineName.text.isNotBlank() && newVaccineDisease.text.isNotBlank() && newVaccineDate.isNotBlank()) {
                                        vaccineHistory.add(
                                            VaccineRecord(
                                                newVaccineName.text,
                                                newVaccineDate,
                                                newVaccineDisease.text,
                                                newVaccineManufacturer.text,
                                                newVaccineNotes.text
                                            )
                                        )
                                        newVaccineName = TextFieldValue()
                                        newVaccineDisease = TextFieldValue()
                                        newVaccineManufacturer = TextFieldValue()
                                        newVaccineNotes = TextFieldValue()
                                        newVaccineDate = ""
                                    }
                                },
                                modifier = Modifier.align(Alignment.End).padding(top = 4.dp)
                            ) {
                                Text("Add Vaccine")
                            }
                        }
                    }
                }
            }

            // --- MEDICATIONS ---
            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(R.drawable.meds),
                            contentDescription = "",
                            modifier = Modifier
                                .size(28.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Medications", fontWeight = FontWeight.Bold)
                    }

                    medications.forEach {
                        Text("• ${it.name} - ${it.dosage} (${it.schedule}), Start: ${it.startDate}")
                    }

                    TextButton(onClick = { showAddMedication = !showAddMedication }) {
                        Text(if (showAddMedication) "Hide Medication Input" else "Add New Medication")
                    }

                    AnimatedVisibility(visible = showAddMedication, enter = expandVertically()) {
                        Column {
                            OutlinedTextField(value = medName, onValueChange = { medName = it }, label = { Text("Medication Name") }, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp))
                            OutlinedTextField(value = medDosage, onValueChange = { medDosage = it }, label = { Text("Dosage") }, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp))
                            OutlinedTextField(value = medSchedule, onValueChange = { medSchedule = it }, label = { Text("Schedule (e.g. Daily)") }, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp))

                            OutlinedTextField(
                                value = TextFieldValue(medStartDate),
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Start Date") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { showDatePicker(context) { medStartDate = it } }
                                    .padding(vertical = 4.dp)
                            )

                            Button(
                                onClick = {
                                    if (medName.text.isNotBlank() && medDosage.text.isNotBlank() && medSchedule.text.isNotBlank() && medStartDate.isNotBlank()) {
                                        medications.add(
                                            Medication(
                                                medName.text,
                                                medDosage.text,
                                                medSchedule.text,
                                                medStartDate
                                            )
                                        )
                                        medName = TextFieldValue()
                                        medDosage = TextFieldValue()
                                        medSchedule = TextFieldValue()
                                        medStartDate = ""
                                        showAddMedication = false
                                    }
                                },
                                modifier = Modifier.align(Alignment.End).padding(top = 4.dp)
                            ) {
                                Text("Save Medication")
                            }
                        }
                    }
                }
            }

            // --- NOTES & DOCS ---
            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(R.drawable.notesdocs),
                            contentDescription = "",
                            modifier = Modifier
                                .size(28.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Notes & Docs", fontWeight = FontWeight.Bold)
                    }
                    Text("Upload vet records, lab results, or notes.")
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TextButton(onClick = {}) { Text("Upload Document") }
                        TextButton(onClick = {}) { Text("View Files") }
                    }
                }
            }
        }
    }
}

fun showDatePicker(context: Context, onDateSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, day: Int ->
            val selected = Calendar.getInstance()
            selected.set(year, month, day)
            val date = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(selected.time)
            onDateSelected(date)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).show()
}

@Preview(showBackground = true)
@Composable
fun HealthScreenPreview() {
    HealthScreen(rememberNavController())
}
