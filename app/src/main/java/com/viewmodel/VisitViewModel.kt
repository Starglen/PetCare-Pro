package com.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.repository.VisitRepository
import com.model.VetVisit
import kotlinx.coroutines.launch

class VisitViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: VisitRepository = VisitRepository(application.applicationContext)

    // Expose all vet visits as LiveData, converting Flow to LiveData
    val allVetVisits: LiveData<List<VetVisit>> = repository.getAllVetVisits().asLiveData()

    // Function to add a new vet visit
    fun addVetVisit(vetVisit: VetVisit) {
        viewModelScope.launch {
            repository.insertVetVisit(vetVisit)
        }
    }

    // Function to update an existing vet visit
    fun updateVetVisit(vetVisit: VetVisit) {
        viewModelScope.launch {
            repository.updateVetVisit(vetVisit)
        }
    }

    // Function to delete a vet visit
    fun deleteVetVisit(vetVisit: VetVisit) {
        viewModelScope.launch {
            repository.deleteVetVisit(vetVisit)
        }
    }
}
