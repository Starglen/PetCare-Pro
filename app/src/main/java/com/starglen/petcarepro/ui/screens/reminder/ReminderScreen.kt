package com.starglen.petcarepro.ui.screens.reminder

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.starglen.petcarepro.R
import com.starglen.petcarepro.ui.theme.maincolor
import java.util.*

data class ReminderData(
    val label: TextFieldValue = TextFieldValue(""),
    val time: String = "08:00 AM",
    val selectedDays: Set<Int> = setOf(1, 3, 5),
    val petType: String = "Dog",
    val feedChecked: Boolean = false,
    val waterChecked: Boolean = false,
    val walkChecked: Boolean = false,
    val scheduledDate: String? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderScreen(navController: NavController, initialReminders: List<ReminderData> = emptyList()) {
    var reminders by remember { mutableStateOf(initialReminders.toMutableList()) }
    var showNewReminderForm by remember { mutableStateOf(false) }
    var newReminder by remember { mutableStateOf(ReminderData()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reminders", fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = maincolor)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showNewReminderForm = true },
                containerColor = maincolor
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Reminder")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            if (showNewReminderForm) {
                item {
                    ReminderCard(reminderData = newReminder, onUpdate = { newReminder = it })
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = {
                                reminders.add(newReminder)
                                reminders = reminders.toMutableList()
                                newReminder = ReminderData()
                                showNewReminderForm = false
                            },
                            modifier = Modifier.weight(1f)
                        ) { Text("Save") }

                        OutlinedButton(
                            onClick = {
                                newReminder = ReminderData()
                                showNewReminderForm = false
                            },
                            modifier = Modifier.weight(1f)
                        ) { Text("Cancel") }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            if (reminders.isEmpty() && !showNewReminderForm) {
                item {
                    Text(
                        "No reminders yet. Tap + to add one.",
                        modifier = Modifier.padding(32.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            itemsIndexed(reminders) { index, reminder ->
                ReminderCard(
                    reminderData = reminder,
                    onUpdate = {
                        reminders[index] = it
                        reminders = reminders.toMutableList()
                    },
                    onDelete = {
                        reminders.removeAt(index)
                        reminders = reminders.toMutableList()
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun ReminderCard(
    reminderData: ReminderData,
    onUpdate: (ReminderData) -> Unit,
    onDelete: (() -> Unit)? = null
) {
    var expanded by remember { mutableStateOf(false) }
    var isLabelEditing by remember { mutableStateOf(false) }

    val feedImage = painterResource(id = R.drawable.feed_icon)
    val waterImage = painterResource(id = R.drawable.water_icon)
    val walkImage = painterResource(id = R.drawable.paw)

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    if (isLabelEditing) {
                        OutlinedTextField(
                            value = reminderData.label,
                            onValueChange = { onUpdate(reminderData.copy(label = it)) },
                            label = { Text("Label") },
                            textStyle = TextStyle(fontSize = 16.sp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Text(
                            text = if (reminderData.label.text.isBlank()) "Add Label" else reminderData.label.text,
                            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Medium),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .clickable { isLabelEditing = true }
                                .padding(bottom = 8.dp)
                        )
                    }

                    TimePickerField(
                        time = reminderData.time,
                        onSet = { onUpdate(reminderData.copy(time = it)) }
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    WeekdaySelector(selected = reminderData.selectedDays, onChange = {
                        onUpdate(reminderData.copy(selectedDays = it))
                    })
                }

                Column(horizontalAlignment = Alignment.End) {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = "Expand"
                        )
                    }
                    if (onDelete != null) {
                        IconButton(onClick = onDelete) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }
                }
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(12.dp))

                ScheduleAlarmView(
                    scheduledDate = reminderData.scheduledDate,
                    onDatePicked = { onUpdate(reminderData.copy(scheduledDate = it)) }
                )
                DropdownSection("Pet Type", reminderData.petType, listOf("Dog", "Cat", "Bird", "Fish", "Other")) {
                    onUpdate(reminderData.copy(petType = it))
                }

                ActionSwitchItem("Feed", reminderData.feedChecked, feedImage) {
                    onUpdate(reminderData.copy(feedChecked = it))
                }
                Divider(color = Color.LightGray)
                ActionSwitchItem("Water", reminderData.waterChecked, waterImage) {
                    onUpdate(reminderData.copy(waterChecked = it))
                }
                Divider(color = Color.LightGray)
                ActionSwitchItem("Walk", reminderData.walkChecked, walkImage) {
                    onUpdate(reminderData.copy(walkChecked = it))
                }
            }
        }
    }
}

@Composable
fun ScheduleAlarmView(scheduledDate: String?, onDatePicked: (String?) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    var showDatePicker by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        if (scheduledDate == null) {
            IconButton(onClick = { showDatePicker = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Date")
            }
            Text("Add Schedule", modifier = Modifier.weight(1f))
        } else {
            Text(
                text = "ReminderDate: $scheduledDate",
                modifier = Modifier.weight(1f),
                style = TextStyle(fontWeight = FontWeight.Medium)
            )
            IconButton(onClick = { onDatePicked(null) }) {
                Icon(Icons.Default.Clear, contentDescription = "Remove Date")
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val selected = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
                onDatePicked(selected)
                showDatePicker = false
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            setOnDismissListener { showDatePicker = false }
            show()
        }
    }
}

@Composable
fun TimePickerField(time: String, onSet: (String) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    var showTimePicker by remember { mutableStateOf(false) }

    Text(
        text = time,
        style = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Bold),
        modifier = Modifier
            .clickable { showTimePicker = true }
            .padding(top = 4.dp)
    )

    if (showTimePicker) {
        LaunchedEffect(Unit) {
            val timePickerDialog = TimePickerDialog(
                context,
                { _, hourOfDay, minute ->
                    val isPM = hourOfDay >= 12
                    val hour = if (hourOfDay % 12 == 0) 12 else hourOfDay % 12
                    val formattedTime = String.format("%02d:%02d %s", hour, minute, if (isPM) "PM" else "AM")
                    onSet(formattedTime)
                    showTimePicker = false
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
            )
            timePickerDialog.setOnDismissListener {
                showTimePicker = false
            }
            timePickerDialog.show()
        }
    }
}

@Composable
fun ActionSwitchItem(text: String, checked: Boolean, image: Painter, onCheckedChange: (Boolean) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Image(painter = image, contentDescription = text, modifier = Modifier.size(30.dp).padding(end = 8.dp))
        Text(text = text, modifier = Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
fun WeekdaySelector(selected: Set<Int>, onChange: (Set<Int>) -> Unit) {
    val days = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        days.forEachIndexed { index, day ->
            FilterChip(
                selected = selected.contains(index),
                onClick = {
                    val updated = selected.toMutableSet()
                    if (updated.contains(index)) updated.remove(index) else updated.add(index)
                    onChange(updated)
                },
                label = { Text(day) }
            )
        }
    }
}

@Composable
fun DropdownSection(title: String, selectedValue: String, options: List<String>, onValueChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = title, fontWeight = FontWeight.Bold)
        Box {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable { expanded = true }
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(text = selectedValue, modifier = Modifier.weight(1f))
                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Expand")
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onValueChange(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReminderScreenPreview() {
    ReminderScreen(rememberNavController(), initialReminders = listOf(ReminderData()))
}
