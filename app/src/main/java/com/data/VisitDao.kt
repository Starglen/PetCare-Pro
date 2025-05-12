package com.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.model.VetVisit

@Dao
interface VisitDao {

    // Get all vet visits
    @Query("SELECT * FROM vet_visits")
    fun getAllVetVisits(): Flow<List<VetVisit>>

    // Insert a new vet visit
    @Insert
    suspend fun insertVetVisit(vetVisit: VetVisit)

    // Update an existing vet visit
    @Update
    suspend fun updateVetVisit(vetVisit: VetVisit)

    // Delete a vet visit
    @Delete
    suspend fun deleteVetVisit(vetVisit: VetVisit)
}
