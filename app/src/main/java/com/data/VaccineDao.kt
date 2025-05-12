package com.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.model.Vaccine

@Dao
interface VaccineDao {

    @Query("SELECT * FROM vaccines")
    fun getAllVaccines(): Flow<List<Vaccine>> // Change LiveData to Flow

    @Insert
    suspend fun insertVaccine(vaccine: Vaccine)

    @Update
    suspend fun updateVaccine(vaccine: Vaccine)

    @Delete
    suspend fun deleteVaccine(vaccine: Vaccine)
}
