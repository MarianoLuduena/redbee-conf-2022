package com.seed.adapter.controller

import com.seed.application.port.`in`.GetCharacterByIdInPort
import com.seed.config.ExceptionHandler
import com.seed.mock.CharacterMockFactory
import io.javalin.Javalin
import io.javalin.http.HttpCode
import io.javalin.plugin.json.JavalinJackson
import io.javalin.testtools.JavalinTest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito

@DisplayName("SWCharacterControllerAdapter Test")
class SWCharacterControllerAdapterTest {

    private val getCharacterByIdInPort = Mockito.mock(GetCharacterByIdInPort::class.java)
    private val app =
        Javalin.create().also {
            it.routes { SWCharacterControllerAdapter.routes(getCharacterByIdInPort) }
                .exception(Exception::class.java, ExceptionHandler())
        }

    @Test
    @DisplayName("get by ID should return the found character")
    fun getCharacterByIdIsFound() {
        JavalinTest.test(app) { _, client ->
            val expected = CharacterMockFactory.getCharacterControllerModel()
            val expectedJson = JSON_MAPPER.writeValueAsString(expected)
            Mockito.`when`(getCharacterByIdInPort.query(CHARACTER_ID)).thenReturn(CharacterMockFactory.getCharacter())
            val actual = client.get("/characters/$CHARACTER_ID")
            Assertions.assertThat(actual.code).isEqualTo(HttpCode.OK.status)
            Assertions.assertThat(actual.body!!.string()).isEqualTo(expectedJson)
        }
    }

    @Test
    @DisplayName("get by ID should return a Bad Request if the ID is not an Int")
    fun getCharacterByIdShouldFailIfIdIsNotAnInt() {
        JavalinTest.test(app) { _, client ->
            val actual = client.get("/characters/a")
            Assertions.assertThat(actual.code).isEqualTo(HttpCode.BAD_REQUEST.status)
            Assertions.assertThat(actual.body!!.string()).contains("\"detail\":\"id TYPE_CONVERSION_FAILED\"")
        }
    }

    @Test
    @DisplayName("get by ID should return a Bad Request if the ID is not a positive Int")
    fun getCharacterByIdShouldFailIfIdIsNotAPositiveInt() {
        JavalinTest.test(app) { _, client ->
            val actual = client.get("/characters/0")
            Assertions.assertThat(actual.code).isEqualTo(HttpCode.BAD_REQUEST.status)
            Assertions.assertThat(actual.body!!.string()).contains("\"detail\":\"id must be greater than 0\"")
        }
    }

    companion object {
        private const val CHARACTER_ID = 1
        private val JSON_MAPPER = JavalinJackson.defaultMapper()
    }

}
