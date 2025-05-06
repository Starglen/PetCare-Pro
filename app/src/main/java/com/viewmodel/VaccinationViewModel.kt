package com.viewmodel

import androidx.lifecycle.ViewModel
import com.model.Vaccine
import com.model.VetVisit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class VaccinationViewModel : ViewModel() {

    // Private MutableStateFlow to manage vaccinations
    private val _vaccinations = MutableStateFlow<List<Vaccine>>(emptyList())
    val vaccinations: StateFlow<List<Vaccine>> = _vaccinations

    // Add a new vaccination
    fun addVaccination(vaccine: Vaccine) {
        // Add vaccine to the list
        _vaccinations.value = _vaccinations.value + vaccine
    }

    // Edit an existing vaccination
    fun editVaccination(updatedVaccination: Vaccine) {
        _vaccinations.value = _vaccinations.value.map {
            if (it.id == updatedVaccination.id) updatedVaccination else it
        }
    }

    // Delete a vaccination
    fun deleteVaccination(id: Int) {
        // Remove vaccine by matching the ID
        _vaccinations.value = _vaccinations.value.filter { it.id != id }
    }

}
