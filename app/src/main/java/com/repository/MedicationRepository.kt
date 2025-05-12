package com.repository

import android.content.Context
import com.data.MedicationDatabase
import com.model.Medication
import kotlinx.coroutines.flow.Flow

class MedicationRepository(context: Context) {

    private val medicationDao = MedicationDatabase.getDatabase(context).medicationDao()

    // Use Flow to emit updates when the medication list changes
    fun getAllMedications(): Flow<List<Medication>> {
        return medicationDao.getAllMedications()
    }

    suspend fun insertMedication(medication: Medication) {
        medicationDao.insertMedication(medication)
    }

    suspend fun updateMedication(medication: Medication) {
        medicationDao.updateMedication(medication)
    }

    suspend fun deleteMedication(medication: Medication) {
        medicationDao.deleteMedication(medication)
    }
}
