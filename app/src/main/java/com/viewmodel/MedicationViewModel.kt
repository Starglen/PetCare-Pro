package com.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.repository.MedicationRepository
import com.model.Medication
import kotlinx.coroutines.launch

class MedicationViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MedicationRepository = MedicationRepository(application.applicationContext)

    // Expose all medications as LiveData, converting Flow to LiveData
    val allMedications: LiveData<List<Medication>> = repository.getAllMedications().asLiveData()

    // Function to add a new medication
    fun addMedication(medication: Medication) {
        viewModelScope.launch {
            repository.insertMedication(medication)
        }
    }

    // Function to update an existing medication
    fun updateMedication(medication: Medication) {
        viewModelScope.launch {
            repository.updateMedication(medication)
        }
    }

    // Function to delete a medication
    fun deleteMedication(medication: Medication) {
        viewModelScope.launch {
            repository.deleteMedication(medication)
        }
    }
}
