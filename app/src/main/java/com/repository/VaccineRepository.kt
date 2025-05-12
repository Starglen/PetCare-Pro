package com.repository

import android.content.Context
import com.data.VaccineDatabase
import com.model.Vaccine
import kotlinx.coroutines.flow.Flow

class VaccineRepository(context: Context) {

    private val vaccineDao = VaccineDatabase.getDatabase(context).vaccineDao()

    // Use Flow to emit updates when the vaccine list changes
    fun getAllVaccines(): Flow<List<Vaccine>> {
        return vaccineDao.getAllVaccines()
    }

    suspend fun insertVaccine(vaccine: Vaccine) {
        vaccineDao.insertVaccine(vaccine)
    }

    suspend fun updateVaccine(vaccine: Vaccine) {
        vaccineDao.updateVaccine(vaccine)
    }

    suspend fun deleteVaccine(vaccine: Vaccine) {
        vaccineDao.deleteVaccine(vaccine)
    }
}
