package com.shiftkey.codingchallenge.availableshiftsbrowser.models

data class LocalizedSpecialty(
    val id: String,
    val specialtyId: Long,
    val stateId: Long,
    val name: String,
    val abbreviation: String,
    val specialty: Specialty
)