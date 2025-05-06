package com.viewmodel

import androidx.lifecycle.ViewModel
import com.model.Medication
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MedicationViewModel : ViewModel() {

    // Private MutableStateFlow to manage medications
    private val _medications = MutableStateFlow<List<Medication>>(emptyList())
    val medications: StateFlow<List<Medication>> = _medications

    // Add a new medication
    fun addMedication(medication: Medication) {
        // Add medication to the list
        _medications.value = _medications.value + medication
    }

    // Edit an existing medication
    fun editMedication(updatedMedication: Medication) {
        _medications.value = _medications.value.map {
            if (it.id == updatedMedication.id) updatedMedication else it
        }
    }

    // Delete a medication
    fun deleteMedication(id: Int) {
        // Remove medication by matching the ID
        _medications.value = _medications.value.filter { it.id != id }
    }
}
