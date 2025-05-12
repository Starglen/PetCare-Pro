package com.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.repository.VaccineRepository
import com.model.Vaccine
import kotlinx.coroutines.launch

class VaccinationViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: VaccineRepository = VaccineRepository(application.applicationContext)

    // Convert Flow to LiveData using asLiveData
    val allVaccines: LiveData<List<Vaccine>> = repository.getAllVaccines().asLiveData()

    // Function to add a new vaccine
    fun addVaccination(vaccine: Vaccine) {
        viewModelScope.launch {
            repository.insertVaccine(vaccine)
        }
    }

    // Function to update an existing vaccination
    fun updateVaccination(vaccine: Vaccine) {
        viewModelScope.launch {
            repository.updateVaccine(vaccine)
        }
    }

    // Function to delete a vaccination
    fun deleteVaccination(vaccine: Vaccine) {
        viewModelScope.launch {
            repository.deleteVaccine(vaccine)
        }
    }
}
