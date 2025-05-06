package com.viewmodel

import androidx.lifecycle.ViewModel
import com.model.VetVisit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HealthViewModel : ViewModel() {

    // StateFlow to hold the list of Vet Visits
    private val _vetVisits = MutableStateFlow<List<VetVisit>>(emptyList())
    val vetVisits: StateFlow<List<VetVisit>> = _vetVisits

    // Add a new visit to the list
    fun addVisit(visit: VetVisit) {
        _vetVisits.value = _vetVisits.value + visit
    }

    // Delete a visit from the list by its ID
    fun deleteVisit(id: Int) {
        _vetVisits.value = _vetVisits.value.filterNot { it.id == id }
    }

    // Edit a visit in the list (replaces the old visit with the new one)
    fun editVisit(updatedVisit: VetVisit) {
        _vetVisits.value = _vetVisits.value.map {
            if (it.id == updatedVisit.id) updatedVisit else it
        }
    }
}
