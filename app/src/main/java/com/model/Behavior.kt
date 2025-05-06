package com.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "behavior")
data class Behavior(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val behavior: String,
    val reaction: String,
    val notes: String?,
    val date: String
)
