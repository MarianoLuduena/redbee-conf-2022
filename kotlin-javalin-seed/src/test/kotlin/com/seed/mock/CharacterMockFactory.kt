package com.seed.mock

import com.seed.adapter.controller.model.SWCharacterControllerModel
import com.seed.adapter.rest.model.SWCharacterRestModel
import com.seed.domain.SWCharacter
import java.time.LocalDateTime

object CharacterMockFactory {

    private const val CHARACTER_NAME = "Luke Skywalker"
    private const val CHARACTER_HEIGHT = 172
    private const val CHARACTER_MASS = 77
    private const val CHARACTER_HAIR_COLOR = "blond"
    private const val CHARACTER_EYE_COLOR = "blue"
    private const val CHARACTER_BIRTH_YEAR = "19BBY"
    private const val CHARACTER_GENDER = "male"
    private val CHARACTER_CREATED_AT = LocalDateTime.parse("2014-12-09T13:50:51.644000")
    private val CHARACTER_UPDATED_AT = LocalDateTime.parse("2014-12-20T21:17:56.891000")

    fun getCharacter(): SWCharacter =
        SWCharacter(
            name = CHARACTER_NAME,
            height = CHARACTER_HEIGHT,
            mass = CHARACTER_MASS,
            hairColor = CHARACTER_HAIR_COLOR,
            eyeColor = CHARACTER_EYE_COLOR,
            birthYear = CHARACTER_BIRTH_YEAR,
            gender = CHARACTER_GENDER,
            createdAt = CHARACTER_CREATED_AT,
            updatedAt = CHARACTER_UPDATED_AT
        )

    fun getCharacterControllerModel(): SWCharacterControllerModel =
        SWCharacterControllerModel(
            name = CHARACTER_NAME,
            height = CHARACTER_HEIGHT,
            mass = CHARACTER_MASS,
            hairColor = CHARACTER_HAIR_COLOR,
            eyeColor = CHARACTER_EYE_COLOR,
            birthYear = CHARACTER_BIRTH_YEAR,
            gender = CHARACTER_GENDER,
            createdAt = CHARACTER_CREATED_AT,
            updatedAt = CHARACTER_UPDATED_AT
        )

    fun getCharacterRestModel(): SWCharacterRestModel =
        SWCharacterRestModel(
            name = CHARACTER_NAME,
            height = CHARACTER_HEIGHT.toString(),
            mass = CHARACTER_MASS.toString(),
            hairColor = CHARACTER_HAIR_COLOR,
            skinColor = "fair",
            eyeColor = CHARACTER_EYE_COLOR,
            birthYear = CHARACTER_BIRTH_YEAR,
            gender = CHARACTER_GENDER,
            homeWorld = "https://swapi.dev/api/planets/1/",
            films = listOf(
                "https://swapi.dev/api/films/2/",
                "https://swapi.dev/api/films/6/",
                "https://swapi.dev/api/films/3/",
                "https://swapi.dev/api/films/1/",
                "https://swapi.dev/api/films/7/"
            ),
            created = CHARACTER_CREATED_AT,
            edited = CHARACTER_UPDATED_AT
        )

}
