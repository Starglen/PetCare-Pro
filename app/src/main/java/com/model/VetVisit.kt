package com.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vet_visits")
data class VetVisit(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val vetName: String,
    val date: String,
    val note: String? = null
)
