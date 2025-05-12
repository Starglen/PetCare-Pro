package com.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.model.Medication

@Dao
interface MedicationDao {

    @Query("SELECT * FROM medications")
    fun getAllMedications(): Flow<List<Medication>> // Get all medications as a Flow

    @Insert
    suspend fun insertMedication(medication: Medication)

    @Update
    suspend fun updateMedication(medication: Medication)

    @Delete
    suspend fun deleteMedication(medication: Medication)
}
