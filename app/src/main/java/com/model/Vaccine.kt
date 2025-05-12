package com.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vaccines")
data class Vaccine(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val vaccineName: String,
    val disease: String,
    val dateGiven: String,
    val manufacturer: String,
    val note: String? = null
)
