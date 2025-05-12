package com.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medications")
data class Medication(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val medicationName: String,
    val dosage: String,
    val schedule: String,
    val startDate: String,
    val notes: String? = null
)
