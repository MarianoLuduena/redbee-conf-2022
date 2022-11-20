package com.seed.application.usecase

import com.seed.application.port.out.GetCharacterByIdOutPort
import com.seed.mock.CharacterMockFactory
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito

@DisplayName("Get character by ID use case test")
class GetCharacterByIdUseCaseTest {

    private var useCase: GetCharacterByIdUseCase? = null

    @BeforeEach
    fun setup() {
        useCase = GetCharacterByIdUseCase(GET_CHARACTER_BY_ID_PORT)
    }

    @Test
    @DisplayName("should get a Star Wars character by ID")
    fun getSWCharacterById() {
        val expected = CharacterMockFactory.getCharacter()
        Mockito.`when`(GET_CHARACTER_BY_ID_PORT.get(CHARACTER_ID)).thenReturn(expected)

        val actual = useCase!!.query(CHARACTER_ID)

        Assertions.assertThat(actual).isEqualTo(expected)
    }

    companion object {
        private const val CHARACTER_ID = 1
        private val GET_CHARACTER_BY_ID_PORT = Mockito.mock(GetCharacterByIdOutPort::class.java)
    }

}
