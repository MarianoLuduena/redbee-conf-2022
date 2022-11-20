package com.seed.domain

import java.time.LocalDateTime

data class SWCharacter(
    val name: String,
    val height: Int?,
    val mass: Int?,
    val hairColor: String,
    val eyeColor: String,
    val birthYear: String,
    val gender: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?
)
