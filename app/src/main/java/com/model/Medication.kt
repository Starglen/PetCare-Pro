package com.model

data class Medication(
    val id: Int,
    val medicationName: String,
    val dosage: String,
    val schedule: String,
    val startDate: String,
    val notes: String? = null
)

