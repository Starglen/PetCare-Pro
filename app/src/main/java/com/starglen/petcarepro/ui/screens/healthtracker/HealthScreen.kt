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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.starglen.petcarepro.R
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.starglen.petcarepro.ui.screens.nutrition.ROUTE_HOME
import com.starglen.petcarepro.ui.theme.maincolor
import com.starglen.petcarepro.ui.theme.newwhite
import java.text.SimpleDateFormat
import java.util.*

@Composable fun AppBackground() = if (isSystemInDarkTheme()) Color(0xFF121212) else Color(0xFFFFFFFF)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthScreen(navController: NavController) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var showVisitForm by remember { mutableStateOf(false) }
    var showVaccineForm by remember { mutableStateOf(false) }
    var showMedicationForm by remember { mutableStateOf(false) }

    var vetTitle by remember { mutableStateOf(TextFieldValue()) }
    var vetName by remember { mutableStateOf(TextFieldValue()) }
    var vetDate by remember { mutableStateOf("") }
    var vetNote by remember { mutableStateOf(TextFieldValue()) }

    var vaccineName by remember { mutableStateOf(TextFieldValue()) }
    var vaccineDisease by remember { mutableStateOf(TextFieldValue()) }
    var vaccineDate by remember { mutableStateOf("") }
    var vaccineManufacturer by remember { mutableStateOf(TextFieldValue()) }
    var vaccineNote by remember { mutableStateOf(TextFieldValue()) }

    var medName by remember { mutableStateOf(TextFieldValue()) }
    var medDose by remember { mutableStateOf(TextFieldValue()) }
    var medStartDate by remember { mutableStateOf("") }
    var medNote by remember { mutableStateOf(TextFieldValue()) }

    var documentUploaded by remember { mutableStateOf(false) }

   Column (modifier = Modifier.background(AppBackground())){
       TopAppBar(
           title = { Text(text = "Health Tracker") },
           colors = TopAppBarDefaults.topAppBarColors(
               containerColor = maincolor,
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

       Column(
           modifier = Modifier
               .fillMaxSize()
               .verticalScroll(scrollState)
               .padding(16.dp)
       ) {

           Spacer(modifier = Modifier.height(16.dp))

           // Vet Visit Form
           Card(modifier = Modifier.fillMaxWidth()) {
               Column(Modifier.padding(16.dp)) {
                   Row(verticalAlignment = Alignment.CenterVertically) {
                       Image(
                           painter = painterResource(id = R.drawable.vetvisit),
                           contentDescription = null,
                           modifier = Modifier.size(24.dp)
                       )
                       Spacer(modifier = Modifier.width(8.dp))
                       Text("Vet Visit", fontSize = 18.sp)
                   }
                   Spacer(Modifier.height(4.dp))
                   Text(if (vetDate.isNotBlank()) "Last Visit: $vetDate" else "No visits yet")

                   Row {
                       TextButton(onClick = { showVisitForm = !showVisitForm }) {
                           Text(if (showVisitForm) "Hide Visit Input" else "Add Visit")
                       }

                     Row (horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()){
                         TextButton(onClick = { },) {
                             Text("View Visit History")
                         }
                     }
                   }

                   AnimatedVisibility(visible = showVisitForm, enter = expandVertically()) {
                       Column {
                           OutlinedTextField(value = vetTitle, onValueChange = { vetTitle = it }, label = { Text("Visit Title") }, modifier = Modifier.fillMaxWidth())
                           OutlinedTextField(value = vetName, onValueChange = { vetName = it }, label = { Text("Vet Name") }, modifier = Modifier.fillMaxWidth())
                           DateField("Visit Date", vetDate, context) { vetDate = it }
                           OutlinedTextField(value = vetNote, onValueChange = { vetNote = it }, label = { Text("Visit Notes") }, modifier = Modifier.fillMaxWidth())

                           Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                               TextButton(onClick = { showVisitForm = false }) { Text("Cancel") }
                               Spacer(modifier = Modifier.width(8.dp))
                               Button(onClick = {
                                   if (vetDate.isNotBlank()) {
                                       // Save visit logic here
                                       vetTitle = TextFieldValue()
                                       vetName = TextFieldValue()
                                       vetNote = TextFieldValue()
                                       vetDate = ""
                                       showVisitForm = false
                                   }
                               }) {
                                   Text("Save Visit")
                               }
                           }
                       }
                   }
               }
           }

           Spacer(modifier = Modifier.height(16.dp))

           // Vaccination Form
           Card(modifier = Modifier.fillMaxWidth()) {
               Column(Modifier.padding(16.dp)) {
                   Row(verticalAlignment = Alignment.CenterVertically) {
                       Image(
                           painter = painterResource(id = R.drawable.vaccine),
                           contentDescription = null,
                           modifier = Modifier.size(24.dp)
                       )
                       Spacer(modifier = Modifier.width(8.dp))
                       Text("Vaccination", fontSize = 18.sp)
                   }
                   Spacer(Modifier.height(4.dp))
                   Text(if (vaccineDate.isNotBlank()) "Last Vaccine: $vaccineDate" else "No records yet")

                  Row {
                      TextButton(onClick = { showVaccineForm = !showVaccineForm }) {
                          Text(if (showVaccineForm) "Hide Vaccine Input" else "Add Vaccine")
                      }
                      Row (horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()){
                          TextButton(onClick = { },) {
                              Text("View Vaccine History")
                          }
                      }
                  }

                   AnimatedVisibility(visible = showVaccineForm, enter = expandVertically()) {
                       Column {
                           OutlinedTextField(value = vaccineName, onValueChange = { vaccineName = it }, label = { Text("Vaccine Name") }, modifier = Modifier.fillMaxWidth())
                           OutlinedTextField(value = vaccineDisease, onValueChange = { vaccineDisease = it }, label = { Text("Disease") }, modifier = Modifier.fillMaxWidth())
                           DateField("Date Given", vaccineDate, context) { vaccineDate = it }
                           OutlinedTextField(value = vaccineManufacturer, onValueChange = { vaccineManufacturer = it }, label = { Text("Manufacturer") }, modifier = Modifier.fillMaxWidth())
                           OutlinedTextField(value = vaccineNote, onValueChange = { vaccineNote = it }, label = { Text("Vaccine Notes") }, modifier = Modifier.fillMaxWidth())

                           Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                               TextButton(onClick = { showVaccineForm = false }) { Text("Cancel") }
                               Spacer(modifier = Modifier.width(8.dp))
                               Button(onClick = {
                                   if (vaccineDate.isNotBlank()) {
                                       // Save vaccine logic here
                                       vaccineName = TextFieldValue()
                                       vaccineDisease = TextFieldValue()
                                       vaccineManufacturer = TextFieldValue()
                                       vaccineNote = TextFieldValue()
                                       vaccineDate = ""
                                       showVaccineForm = false
                                   }
                               }) {
                                   Text("Save Vaccine")
                               }
                           }
                       }
                   }
               }
           }

           Spacer(modifier = Modifier.height(16.dp))

           // Medication Form
           Card(modifier = Modifier.fillMaxWidth()) {
               Column(Modifier.padding(16.dp)) {
                   Row(verticalAlignment = Alignment.CenterVertically) {
                       Image(
                           painter = painterResource(id = R.drawable.meds),
                           contentDescription = null,
                           modifier = Modifier.size(24.dp)
                       )
                       Spacer(modifier = Modifier.width(8.dp))
                       Text("Medication", fontSize = 18.sp)
                   }
                   Spacer(Modifier.height(4.dp))
                   Text(if (medStartDate.isNotBlank()) "Last Medication: $medStartDate" else "No medication records yet")

                   Row {
                       TextButton(onClick = { showMedicationForm = !showMedicationForm }) {
                           Text(if (showMedicationForm) "Hide Medication Input" else "Add Medication")
                       }
                       Row (horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()){
                           TextButton(onClick = { },) {
                               Text("View Medication History")
                           }
                       }
                   }

                   AnimatedVisibility(visible = showMedicationForm, enter = expandVertically()) {
                       Column {
                           OutlinedTextField(value = medName, onValueChange = { medName = it }, label = { Text("Medication Name") }, modifier = Modifier.fillMaxWidth())
                           OutlinedTextField(value = medDose, onValueChange = { medDose = it }, label = { Text("Dose") }, modifier = Modifier.fillMaxWidth())
                           DateField("Start Date", medStartDate, context) { medStartDate = it }
                           OutlinedTextField(value = medNote, onValueChange = { medNote = it }, label = { Text("Medication Notes") }, modifier = Modifier.fillMaxWidth())

                           Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                               TextButton(onClick = { showMedicationForm = false }) { Text("Cancel") }
                               Spacer(modifier = Modifier.width(8.dp))
                               Button(onClick = {
                                   if (medStartDate.isNotBlank()) {
                                       // Save medication logic here
                                       medName = TextFieldValue()
                                       medDose = TextFieldValue()
                                       medNote = TextFieldValue()
                                       medStartDate = ""
                                       showMedicationForm = false
                                   }
                               }) {
                                   Text("Save Medication")
                               }
                           }
                       }
                   }
               }
           }

           Spacer(modifier = Modifier.height(16.dp))

           // Notes and Docs Section
           Card(modifier = Modifier.fillMaxWidth()) {
               Column(Modifier.padding(16.dp)) {
                   Row(verticalAlignment = Alignment.CenterVertically) {
                       Image(
                           painter = painterResource(id = R.drawable.notesdocs),
                           contentDescription = null,
                           modifier = Modifier.size(24.dp)
                       )
                       Spacer(modifier = Modifier.width(8.dp))
                       Text("Notes & Docs", fontSize = 18.sp)
                   }
                   Spacer(modifier = Modifier.height(8.dp))
                   Row (){
                       TextButton(onClick = { documentUploaded = !documentUploaded }) {
                           Text("Upload Document")
                       }
                       Spacer(modifier = Modifier.height(8.dp))
                       if (documentUploaded) {
                           Text("Document Uploaded")
                       }

                       Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                           TextButton(onClick = { /* Navigate to view files */ }) { Text("View Files") }
                       }
                   }
               }
           }

           Spacer(modifier = Modifier.height(24.dp))
       }
   }
}

@Composable
fun DateField(label: String, dateText: String, context: Context, onDateSelected: (String) -> Unit) {
    OutlinedTextField(
        value = dateText,
        onValueChange = {},
        readOnly = true,
        label = { Text(label) },
        trailingIcon = {
            Icon(Icons.Default.DateRange, contentDescription = null, tint = maincolor, modifier = Modifier.clickable { showDatePicker(context, onDateSelected) })
        },
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showDatePicker(context, onDateSelected) }
            .padding(vertical = 4.dp)
    )
}

fun showDatePicker(context: Context, onDateSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, day: Int ->
            val selected = Calendar.getInstance().apply {
                set(year, month, day)
            }
            val formatted = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(selected.time)
            onDateSelected(formatted)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).show()
}

@Preview(showBackground = true)
@Composable
fun PreviewHealthScreen() {
    HealthScreen(rememberNavController())
}
