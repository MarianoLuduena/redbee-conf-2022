package com.seed.adapter.rest.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.seed.domain.SWCharacter
import java.time.LocalDateTime

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class SWCharacterRestModel(
    val name: String,
    val height: String,
    val mass: String,
    val hairColor: String,
    val skinColor: String,
    val eyeColor: String,
    val birthYear: String,
    val gender: String,
    @JsonProperty("homeworld") val homeWorld: String,
    val films: List<String>,
    val created: LocalDateTime,
    val edited: LocalDateTime?
) {

    fun toDomain() =
        SWCharacter(
            name = name,
            height = height.toIntOrNull(),
            mass = mass.toIntOrNull(),
            hairColor = hairColor,
            eyeColor = eyeColor,
            birthYear = birthYear,
            gender = gender,
            createdAt = created,
            updatedAt = edited
        )

}
