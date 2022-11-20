package com.seed.adapter.rest

import com.seed.adapter.rest.client.RestClient
import com.seed.adapter.rest.model.SWCharacterRestModel
import com.seed.mock.CharacterMockFactory
import com.seed.mock.ConfigMockFactory
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito

@DisplayName("StarWars REST Adapter Test")
class StarWarsRestAdapterTest {

    private val restClient = Mockito.mock(RestClient::class.java)

    private var adapter: StarWarsRestAdapter? = null

    @BeforeEach
    fun setup() {
        adapter = StarWarsRestAdapter(CONFIG, restClient)
    }

    @Test
    @DisplayName("get should return the domain model of the found character")
    fun getCharacterByIdShouldReturnACharacter() {
        Mockito.`when`(
            restClient.get(
                buildGetCharacterByIdUrl(),
                SWCharacterRestModel::class.java,
                CONFIG.swCharacterRepository.getByIdTimeout
            )
        ).thenReturn(CharacterMockFactory.getCharacterRestModel())

        val actual = adapter!!.get(CHARACTER_ID)

        Assertions.assertThat(actual).isEqualTo(CharacterMockFactory.getCharacter())
    }

    private fun buildGetCharacterByIdUrl() =
        CONFIG.swCharacterRepository.getByIdUri.replace("{id}", CHARACTER_ID.toString())

    companion object {
        private const val CHARACTER_ID = 1
        private val CONFIG = ConfigMockFactory.get()
    }

}
