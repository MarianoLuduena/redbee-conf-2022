package com.seed.adapter.controller.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.seed.domain.SWCharacter
import java.time.LocalDateTime

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class SWCharacterControllerModel(
    val name: String,
    val height: Int?,
    val mass: Int?,
    val hairColor: String,
    val eyeColor: String,
    val birthYear: String,
    val gender: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING) val createdAt: LocalDateTime,
    @JsonFormat(shape = JsonFormat.Shape.STRING) val updatedAt: LocalDateTime?
) {

    companion object {
        fun from(domain: SWCharacter): SWCharacterControllerModel =
            SWCharacterControllerModel(
                name = domain.name,
                height = domain.height,
                mass = domain.mass,
                hairColor = domain.hairColor,
                eyeColor = domain.eyeColor,
                birthYear = domain.birthYear,
                gender = domain.gender,
                createdAt = domain.createdAt,
                updatedAt = domain.updatedAt
            )
    }

}
