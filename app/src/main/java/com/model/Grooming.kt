package com.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "grooming_table")
data class Grooming(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val task: String,
    val status: String,
    val date: String
)
