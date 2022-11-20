package com.seed.adapter.rest.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.seed.adapter.rest.exception.RestClientGenericException
import com.seed.adapter.rest.model.SWCharacterRestModel
import com.seed.config.ApiError
import com.seed.mock.CharacterMockFactory
import io.javalin.http.HttpCode
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpHeaders
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration

@DisplayName("RestClient Test")
class RestClientTest {

    private val httpClient = Mockito.mock(HttpClient::class.java)
    private val objectMapper = Mockito.mock(ObjectMapper::class.java)
    private val errorHandler = Mockito.mock(RestClientErrorHandler::class.java)

    private var client: RestClient? = null

    @BeforeEach
    fun setup() {
        client = RestClient(httpClient, objectMapper, errorHandler)
    }

    @Test
    fun getShouldQueryReadAndReturnTheResponse() {
        val timeout = 5000L

        val request = Mockito.mock(HttpRequest::class.java)
        Mockito.`when`(request.method()).thenReturn("GET")

        val response = buildResponseMock()
        Mockito.`when`(response.statusCode()).thenReturn(HttpCode.OK.status)
        Mockito.`when`(response.uri()).thenReturn(URI.create(CHARACTER_URI))
        Mockito.`when`(response.headers()).thenReturn(HttpHeaders.of(emptyMap()) { _, _ -> true })
        Mockito.`when`(response.body()).thenReturn("".encodeToByteArray())
        Mockito.`when`(response.request()).thenReturn(request)

        val expected = CharacterMockFactory.getCharacterRestModel()
        Mockito.`when`(objectMapper.readValue("".encodeToByteArray(), SWCharacterRestModel::class.java))
            .thenReturn(expected)

        Mockito.`when`(
            httpClient.send(
                Mockito.argThat { req ->
                    req.method() == "GET" && req.timeout().get() == Duration.ofMillis(timeout)
                },
                Mockito.any(HttpResponse.BodyHandler::class.java)
            )
        ).thenReturn(response)

        Mockito.`when`(errorHandler.hasError(response)).thenReturn(false)

        val actual = client!!.get(uri = CHARACTER_URI, clazz = SWCharacterRestModel::class.java, timeout = timeout)

        Assertions.assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun getShouldDelegateTheErrorToTheHandler() {
        val response = buildResponseMock()
        Mockito.`when`(response.statusCode()).thenReturn(HttpCode.IM_A_TEAPOT.status)

        Mockito.`when`(
            httpClient.send(
                Mockito.any(HttpRequest::class.java),
                Mockito.any(HttpResponse.BodyHandler::class.java)
            )
        ).thenReturn(response)

        Mockito.`when`(errorHandler.hasError(response)).thenReturn(true)
        Mockito.`when`(errorHandler.handleError(response))
            .thenThrow(RestClientGenericException(ApiError.INTERNAL_ERROR))

        val thrown =
            Assertions.catchThrowable { client!!.get(uri = CHARACTER_URI, clazz = SWCharacterRestModel::class.java) }

        Assertions.assertThat(thrown)
            .isExactlyInstanceOf(RestClientGenericException::class.java)
            .hasMessage(ApiError.INTERNAL_ERROR.defaultMessage)
    }

    private fun buildResponseMock(): HttpResponse<ByteArray> {
        @Suppress("UNCHECKED_CAST") return Mockito.mock(HttpResponse::class.java) as HttpResponse<ByteArray>
    }

    companion object {
        private const val CHARACTER_URI = "http://localhost:1234/people/1"
    }

}
