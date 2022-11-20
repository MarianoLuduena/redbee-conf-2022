package com.seed.config

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Config Loader Test")
class ConfigLoaderTest {

    @Test
    fun readPropertiesFromFS() {
        val actual = ConfigLoader.load("src/test/resources/config/application-test.properties", ENV_VARIABLES)
        Assertions.assertThat(actual).isEqualTo(EXPECTED)
    }

    @Test
    fun readPropertiesFromClasspath() {
        val actual = ConfigLoader.load("config/application-test.properties", ENV_VARIABLES)
        Assertions.assertThat(actual).isEqualTo(EXPECTED)
    }

    companion object {
        private const val GET_BY_ID_URI = "http://localhost:9090"
        private const val GET_BY_ID_TIMEOUT_ENV = "GET_BY_ID_TIMEOUT"
        private const val GET_BY_ID_TIMEOUT_ENV_VALUE = "10000"

        private val ENV_VARIABLES = mapOf(Pair(GET_BY_ID_TIMEOUT_ENV, GET_BY_ID_TIMEOUT_ENV_VALUE))

        private val EXPECTED = Config(
            swCharacterRepository = Config.SWCharacterRepository(
                getByIdUri = GET_BY_ID_URI,
                getByIdTimeout = GET_BY_ID_TIMEOUT_ENV_VALUE.toLong()
            )
        )
    }

}
