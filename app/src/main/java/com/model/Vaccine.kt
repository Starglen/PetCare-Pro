package com.model



data class Vaccine(
    val id: Int,
    val vaccineName: String,
    val disease: String,
    val dateGiven: String,
    val manufacturer: String,
    val note: String? = null
)
