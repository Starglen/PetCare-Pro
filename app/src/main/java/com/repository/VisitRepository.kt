package com.repository

import android.content.Context
import com.data.VisitDatabase
import com.model.VetVisit
import kotlinx.coroutines.flow.Flow

class VisitRepository(context: Context) {

    private val visitDao = VisitDatabase.getDatabase(context).visitDao()

    // Use Flow to observe the list of vet visits
    fun getAllVetVisits(): Flow<List<VetVisit>> {
        return visitDao.getAllVetVisits()
    }

    // Insert a new vet visit
    suspend fun insertVetVisit(vetVisit: VetVisit) {
        visitDao.insertVetVisit(vetVisit)
    }

    // Update an existing vet visit
    suspend fun updateVetVisit(vetVisit: VetVisit) {
        visitDao.updateVetVisit(vetVisit)
    }

    // Delete a specific vet visit
    suspend fun deleteVetVisit(vetVisit: VetVisit) {
        visitDao.deleteVetVisit(vetVisit)
    }
}
