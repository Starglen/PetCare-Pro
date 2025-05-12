package com.starglen.petcarepro.ui.screens.health

import android.Manifest
import android.app.Application
import android.app.DatePickerDialog
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.DatePicker
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.foundation.*
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRow
import androidx.compose.material3.Tab
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.model.FileType
import com.model.Medication
import com.model.UploadedFile
import com.model.Vaccine
import com.model.VetVisit
import com.starglen.petcarepro.R
import com.starglen.petcarepro.ui.screens.nutrition.ROUTE_HOME
import com.starglen.petcarepro.ui.theme.maincolor
import com.starglen.petcarepro.ui.theme.newwhite
import com.viewmodel.VisitViewModel
import com.viewmodel.MedicationViewModel
import com.viewmodel.VaccinationViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors


@Composable fun AppBackground() = if (isSystemInDarkTheme()) Color(0xFF121212) else Color(0xFFFFFFFF)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthScreen(
    navController: NavController,
    healthViewModel: VisitViewModel,
    vaccinationViewModel: VaccinationViewModel,
    medicationViewModel: MedicationViewModel
) {
    val context = LocalContext.current

    val documentList = remember { mutableStateListOf<UploadedFile>() }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val name = uri.lastPathSegment?.substringAfterLast("/") ?: "Image"
            documentList.add(UploadedFile(name, uri, FileType.IMAGE))
        }
    }

    val documentPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            val name = uri.lastPathSegment?.substringAfterLast("/") ?: "File"
            documentList.add(UploadedFile(name, uri, FileType.DOCUMENT))
        }
    }



    val scrollState = rememberScrollState()
    val bottomSheetState = rememberModalBottomSheetState()
    var showSheet by remember { mutableStateOf(false) }


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
    var medSchedule by remember { mutableStateOf(TextFieldValue()) }
    var medStartDate by remember { mutableStateOf("") }
    var medNote by remember { mutableStateOf(TextFieldValue()) }


    Column(modifier = Modifier.background(AppBackground())) {
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

            // Vet Visit Card
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

                    val vetVisits by healthViewModel.allVetVisits.observeAsState(emptyList())  // This is for LiveData

                    val latestVisit = vetVisits.lastOrNull()
                    Text(
                        text = latestVisit?.let {
                            "Last Visit: ${it.title} with ${it.vetName} on ${it.date}"
                        } ?: "No visits yet"  // Show fallback text if there are no visits
                    )


                    Row {
                        TextButton(onClick = { showVisitForm = !showVisitForm }) {
                            Text(if (showVisitForm) "Hide Visit Input" else "Add Visit")
                        }

                        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                            TextButton(onClick = {
                                navController.navigate("vet_visit_history")
                            }) {
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
                                        val newVisit = VetVisit(
                                            id = UUID.randomUUID().hashCode(),
                                            title = vetTitle.text,
                                            vetName = vetName.text,
                                            date = vetDate,
                                            note = vetNote.text
                                        )
                                        healthViewModel.addVetVisit(newVisit)

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

            // Vaccine Card
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

                    val vaccines by vaccinationViewModel.allVaccines.observeAsState(emptyList())
                    val latestVaccine = vaccines.lastOrNull()

                    Text(
                        text = latestVaccine?.let {
                            "Last Vaccine: ${it.vaccineName} on ${it.dateGiven}"
                        } ?: "No vaccines yet"
                    )


                    Row {
                        TextButton(onClick = { showVaccineForm = !showVaccineForm }) {
                            Text(if (showVaccineForm) "Hide Vaccine Input" else "Add Vaccine")
                        }

                        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                            TextButton(onClick = {
                                navController.navigate("vaccine")
                            }) {
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
                                        val newVaccine = Vaccine(
                                            id = UUID.randomUUID().hashCode(),
                                            vaccineName = vaccineName.text,
                                            disease = vaccineDisease.text,
                                            dateGiven = vaccineDate,
                                            manufacturer = vaccineManufacturer.text,
                                            note = if (vaccineNote.text.isNotBlank()) vaccineNote.text else null
                                        )
                                        vaccinationViewModel.addVaccination(newVaccine)

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

                   val medications by medicationViewModel.allMedications.observeAsState(initial = emptyList())

                   val latestMedication = medications.lastOrNull()

                   Text(
                       text = latestMedication?.let {
                           "Last Medication: ${it.medicationName}, ${it.dosage} • ${it.startDate}"
                       } ?: "No medications yet"
                   )



                   Row {
                       TextButton(onClick = { showMedicationForm = !showMedicationForm }) {
                           Text(if (showMedicationForm) "Hide Medication Input" else "Add Medication")
                       }
                       Row (horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()){
                           TextButton(onClick = {
                               navController.navigate("medication")
                           },) {
                               Text("View Medication History")
                           }
                       }
                   }

                   AnimatedVisibility(visible = showMedicationForm, enter = expandVertically()) {
                       Column {
                           OutlinedTextField(
                               value = medName,
                               onValueChange = { medName = it },
                               label = { Text("Medication Name") },
                               modifier = Modifier.fillMaxWidth()
                           )

                           OutlinedTextField(
                               value = medDose,
                               onValueChange = { medDose = it },
                               label = { Text("Dose") },
                               modifier = Modifier.fillMaxWidth()
                           )

                           OutlinedTextField(
                               value = medSchedule,
                               onValueChange = { medSchedule = it },
                               label = { Text("Schedule (e.g., 2x daily)") },
                               modifier = Modifier.fillMaxWidth()
                           )

                           DateField("Start Date", medStartDate, context) {
                               medStartDate = it
                           }

                           OutlinedTextField(
                               value = medNote,
                               onValueChange = { medNote = it },
                               label = { Text("Medication Notes") },
                               modifier = Modifier.fillMaxWidth()
                           )


                           Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                               TextButton(onClick = { showMedicationForm = false }) { Text("Cancel") }
                               Spacer(modifier = Modifier.width(8.dp))
                               Button(onClick = {
                                   if (medStartDate.isNotBlank()) {
                                       val newMedication = Medication(
                                           id = UUID.randomUUID().hashCode(),
                                           medicationName= medName.text,
                                           dosage = medDose.text,
                                           schedule = medSchedule.text,
                                           startDate = medStartDate,
                                           notes = if (medNote.text.isNotBlank()) medNote.text else null
                                       )
                                       medicationViewModel.addMedication(newMedication)

                                       // Reset the form
                                       medName = TextFieldValue()
                                       medDose = TextFieldValue()
                                       medSchedule = TextFieldValue()
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

                    var showCameraDialog by remember { mutableStateOf(false) }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        TextButton(onClick = {
                            showSheet = true
                        }) {
                            Text("Upload Document")
                        }

                        if (showSheet) {
                            ModalBottomSheet(
                                onDismissRequest = { showSheet = false },
                                sheetState = bottomSheetState
                            ) {
                                Column(Modifier.padding(16.dp)) {
                                    Text("Select Upload Method", style = MaterialTheme.typography.titleMedium)
                                    Spacer(modifier = Modifier.height(12.dp))

                                    Text(
                                        "📷 Take Photo",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                showSheet = false
                                                showCameraDialog = true
                                            }
                                            .padding(vertical = 12.dp)
                                    )

                                    Text("🖼️ Choose from Gallery", modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            showSheet = false
                                            galleryLauncher.launch("image/*")
                                        }
                                        .padding(vertical = 12.dp)
                                    )

                                    Text("📄 Pick a Document", modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            showSheet = false
                                            documentPickerLauncher.launch(arrayOf("*/*"))
                                        }
                                        .padding(vertical = 12.dp)
                                    )
                                }
                            }
                        }

                        if (showCameraDialog) {
                            CameraCaptureDialog(
                                onImageCaptured = { uri ->
                                    documentList.add(
                                        UploadedFile(
                                            name = "Camera_${System.currentTimeMillis()}",
                                            uri = uri,
                                            type = FileType.IMAGE
                                        )
                                    )
                                },
                                onDismiss = { showCameraDialog = false }
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        var showFilesDialog by remember { mutableStateOf(false) }

                        TextButton(onClick = {
                            showFilesDialog = true
                        }) {
                            Text("View Files")
                        }

                        if (showFilesDialog) {
                            UploadedFilesDialog(
                                uploadedFiles = documentList, // Your List<UploadedFile>
                                onDismissRequest = { showFilesDialog = false },
                                onDelete = { toDelete ->
                                    // Remove selected files from the document list
                                    documentList.removeAll(toDelete)
                                    // Optionally, you can update the state to trigger UI changes after deletion
                                },
                                onShare = { toShare ->
                                    // Implement sharing logic, maybe using ShareCompat.IntentBuilder
                                    // Example:
                                    val filesToShare = toShare.map { it.uri } // Assuming `UploadedFile` has a `uri` field
                                    // Your sharing code here (e.g., using `ShareCompat.IntentBuilder`)
                                },
                                onSave = { toSave ->
                                    // Implement saving logic, for example, copying files to external storage
                                    // This will depend on your platform and file handling logic
                                },
                                onRename = { file, newName ->
                                    // Handle renaming logic
                                    // Find the file to rename and update its name
                                    val index = documentList.indexOf(file)
                                    if (index != -1) {
                                        documentList[index] = file.copy(name = newName)
                                    }
                                }
                            )
                        }


                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
       }
   }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun UploadedFilesDialog(
    uploadedFiles: List<UploadedFile>,
    onDismissRequest: () -> Unit,
    onDelete: (List<UploadedFile>) -> Unit,
    onShare: (List<UploadedFile>) -> Unit,
    onSave: (List<UploadedFile>) -> Unit,
    onRename: (UploadedFile, String) -> Unit
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    var selectedItems by remember { mutableStateOf(setOf<UploadedFile>()) }
    var showViewer by remember { mutableStateOf(false) }
    var viewerStartIndex by remember { mutableStateOf(0) }
    var renameDialogFile by remember { mutableStateOf<UploadedFile?>(null) }
    var newFileName by remember { mutableStateOf("") }

    val filesToDisplay = remember(selectedTabIndex, uploadedFiles) {
        val base = when (selectedTabIndex) {
            1 -> uploadedFiles.filter { it.type == FileType.IMAGE }
            2 -> uploadedFiles.filter { it.type == FileType.DOCUMENT }
            else -> uploadedFiles
        }
        base.sortedByDescending { it.uri.lastPathSegment?.toLongOrNull() ?: 0L }
    }

    val groupedFiles = remember(filesToDisplay) {
        filesToDisplay.groupBy { file ->
            val date = Date(file.addedAt)
            val cal = Calendar.getInstance().apply { time = date }
            val now = Calendar.getInstance()

            val formattedDate = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(date)

            when {
                cal.get(Calendar.YEAR) == now.get(Calendar.YEAR) &&
                        cal.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR) ->
                    "Today — $formattedDate"

                cal.get(Calendar.YEAR) == now.get(Calendar.YEAR) &&
                        cal.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR) - 1 ->
                    "Yesterday — $formattedDate"

                else -> formattedDate
            }
        }
    }




    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                TopAppBar(
                    title = { Text("Your Files") },
                    navigationIcon = {
                        IconButton(onClick = onDismissRequest) {
                            Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                    },
                    actions = {
                        if (selectedItems.isNotEmpty()) {
                            IconButton(onClick = { onShare(selectedItems.toList()) }) {
                                Icon(Icons.Default.Share, contentDescription = "Share")
                            }
                            IconButton(onClick = { onSave(selectedItems.toList()) }) {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = "Save")
                            }
                            IconButton(onClick = { onDelete(selectedItems.toList()) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete")
                            }

                            // Only show rename icon if exactly one item is selected
                            if (selectedItems.size == 1) {
                                IconButton(onClick = {
                                    val file = selectedItems.first()
                                    renameDialogFile = file
                                    newFileName = file.name
                                }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Rename")
                                }
                            }
                        }
                    }
                )

                val tabs = listOf("All", "Images", "Documents")
                TabRow(selectedTabIndex = selectedTabIndex) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = { Text(title) }
                        )
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    groupedFiles.forEach { (dateTitle, files) ->
                        item {
                            Text(
                                text = dateTitle,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }

                        items(files) { file ->
                            val isSelected = selectedItems.contains(file)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .background(
                                        if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                        else Color.Transparent
                                    )
                                    .combinedClickable(
                                        onClick = {
                                            if (file.type == FileType.IMAGE) {
                                                viewerStartIndex = files.indexOf(file)
                                                showViewer = true
                                            }
                                        },
                                        onLongClick = {
                                            selectedItems =
                                                if (isSelected) selectedItems - file
                                                else selectedItems + file
                                        }
                                    ),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (file.type == FileType.IMAGE) {
                                    AsyncImage(
                                        model = file.uri,
                                        contentDescription = null,
                                        modifier = Modifier.size(48.dp),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = null,
                                        modifier = Modifier.size(48.dp)
                                    )
                                }
                                Spacer(Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(file.name)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Fullscreen viewer
    if (showViewer) {
        FullscreenImageViewer(
            files = filesToDisplay.filter { it.type == FileType.IMAGE },
            startIndex = viewerStartIndex,
            onClose = { showViewer = false }
        )
    }

    // Rename dialog
    renameDialogFile?.let { file ->
        AlertDialog(
            onDismissRequest = { renameDialogFile = null },
            title = { Text("Rename File") },
            text = {
                OutlinedTextField(
                    value = newFileName,
                    onValueChange = { newFileName = it },
                    label = { Text("New Name") },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    onRename(file, newFileName)
                    renameDialogFile = null
                }) {
                    Text("Rename")
                }
            },
            dismissButton = {
                TextButton(onClick = { renameDialogFile = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun FullscreenImageViewer(
    files: List<UploadedFile>,
    startIndex: Int,
    onClose: () -> Unit
) {
    var currentIndex by remember { mutableStateOf(startIndex) }

    Dialog(onDismissRequest = onClose) {
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            AsyncImage(
                model = files[currentIndex].uri,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        // Next image
                        currentIndex = (currentIndex + 1) % files.size
                    },
                contentScale = ContentScale.Fit
            )

            IconButton(
                onClick = onClose,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
            ) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
            }
        }
    }
}


@Composable
fun CameraCaptureDialog(
    onImageCaptured: (Uri) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val outputDirectory = context.cacheDir
    val executor = remember { Executors.newSingleThreadExecutor() }

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasCameraPermission = granted
        }
    )

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    if (!hasCameraPermission) {
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = onDismiss) {
                    Text("OK")
                }
            },
            title = { Text("Camera Permission Required") },
            text = { Text("This feature needs camera access. Please grant permission.") }
        )
        return
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth(0.99f)
                .fillMaxHeight(0.99f)
        ) {
            if (imageUri == null) {
                Box(Modifier.fillMaxSize()) {
                    AndroidView(
                        factory = { ctx ->
                            val previewView = PreviewView(ctx).apply {
                                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                            }

                            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                            cameraProviderFuture.addListener({
                                val cameraProvider = cameraProviderFuture.get()

                                val preview = Preview.Builder().build().also {
                                    it.setSurfaceProvider(previewView.surfaceProvider)
                                }

                                imageCapture = ImageCapture.Builder()
                                    .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                                    .build()

                                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                                try {
                                    cameraProvider.unbindAll()
                                    cameraProvider.bindToLifecycle(
                                        lifecycleOwner,
                                        cameraSelector,
                                        preview,
                                        imageCapture
                                    )
                                } catch (e: Exception) {
                                    Log.e("Camera", "Camera binding failed", e)
                                }
                            }, ContextCompat.getMainExecutor(ctx))

                            previewView
                        },
                        modifier = Modifier.fillMaxSize()
                    )

                    IconButton(
                        onClick = {
                            val photoFile = File(
                                outputDirectory,
                                "captured_${System.currentTimeMillis()}.jpg"
                            )
                            val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

                            imageCapture?.takePicture(
                                outputOptions,
                                executor,
                                object : ImageCapture.OnImageSavedCallback {
                                    override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                                        imageUri = Uri.fromFile(photoFile)
                                    }

                                    override fun onError(exception: ImageCaptureException) {
                                        Log.e("Camera", "Image capture failed", exception)
                                    }
                                }
                            )
                        },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(20.dp)
                            .size(72.dp)
                            .background(Color.White, CircleShape)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.capture),
                            contentDescription = "Capture",
                            modifier = Modifier
                                .size(72.dp)
                                .background(Color.White, CircleShape)
                        )

                    }
                }
            } else {
                Box(Modifier.fillMaxSize()) {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = "Captured Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    Row(
                        Modifier
                            .align(Alignment.BottomCenter)
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        IconButton(
                            onClick = { imageUri = null },
                            modifier = Modifier
                                .size(64.dp)
                                .background(Color.Red, CircleShape)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Retake", tint = Color.White)
                        }

                        Spacer(Modifier.width(32.dp))

                        IconButton(
                            onClick = {
                                imageUri?.let {
                                    onImageCaptured(it)
                                }
                                onDismiss()
                            },
                            modifier = Modifier
                                .size(64.dp)
                                .background(Color.Green, CircleShape)
                        ) {
                            Icon(Icons.Default.Check, contentDescription = "Use Image", tint = Color.White)
                        }
                    }
                }
            }
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


@Composable
fun HealthScreenPreview() {

    val mockVisitViewModel = VisitViewModel(LocalContext.current.applicationContext as Application)
    val mockVaccinationViewModel = VaccinationViewModel(LocalContext.current.applicationContext as Application)
    val mockViewModel = MedicationViewModel(LocalContext.current.applicationContext as Application)
    HealthScreen(
        navController = rememberNavController(),
        healthViewModel = mockVisitViewModel,
        vaccinationViewModel = mockVaccinationViewModel,
        medicationViewModel = mockViewModel
    )
}


