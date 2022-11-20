package com.seed.adapter.rest.client

import com.seed.adapter.rest.exception.BadRequestRestClientException
import com.seed.adapter.rest.exception.RestClientGenericException
import com.seed.config.ApiError
import com.seed.config.exception.ResourceNotFoundException
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.net.URI
import java.net.http.HttpResponse

@DisplayName("RestClientErrorHandler Test")
class RestClientErrorHandlerTest {

    private var handler: RestClientErrorHandler? = null

    @BeforeEach
    fun setup() {
        handler = RestClientErrorHandler()
    }

    @Test
    @DisplayName("hasErrors should return true for HTTP status codes greater or equal than 400")
    fun codesGreaterOrEqualThan400ShouldBeConsideredErroneous() {
        val actual = (400 until 600).all { httpStatusCode ->
            val httpResponse = buildResponseMock()
            Mockito.`when`(httpResponse.statusCode()).thenReturn(httpStatusCode)

            handler!!.hasError(httpResponse)
        }

        Assertions.assertThat(actual).isTrue
    }

    @Test
    @DisplayName("hasErrors should return false for HTTP status codes lower than 400")
    fun codesLowerThan400ShouldBeConsideredSuccessful() {
        val actual = (1 until 400).any { httpStatusCode ->
            val httpResponse = buildResponseMock()
            Mockito.`when`(httpResponse.statusCode()).thenReturn(httpStatusCode)

            handler!!.hasError(httpResponse)
        }

        Assertions.assertThat(actual).isFalse
    }

    @Test
    @DisplayName("handleError should map a 400 status code to a BadRequestRestClientException")
    fun anHttpResponseWithA400StatusCodeShouldBeHandledAsABadRequestException() {
        val httpResponse = buildResponseMock()
        Mockito.`when`(httpResponse.statusCode()).thenReturn(400)
        Mockito.`when`(httpResponse.body()).thenReturn("".encodeToByteArray())
        Mockito.`when`(httpResponse.uri()).thenReturn(REQ_URI)

        val thrown = Assertions.catchThrowable { handler!!.handleError(httpResponse) }

        Assertions.assertThat(thrown)
            .isExactlyInstanceOf(BadRequestRestClientException::class.java)
            .hasMessage(ApiError.BAD_REQUEST.defaultMessage)
    }

    @Test
    @DisplayName("handleError should map a 404 status code to a ResourceNotFoundException")
    fun anHttpResponseWithA404StatusCodeShouldBeHandledAsAResourceNotFoundException() {
        val httpResponse = buildResponseMock()
        Mockito.`when`(httpResponse.statusCode()).thenReturn(404)
        Mockito.`when`(httpResponse.body()).thenReturn("".encodeToByteArray())
        Mockito.`when`(httpResponse.uri()).thenReturn(REQ_URI)

        val thrown = Assertions.catchThrowable { handler!!.handleError(httpResponse) }

        Assertions.assertThat(thrown)
            .isExactlyInstanceOf(ResourceNotFoundException::class.java)
            .hasMessage(ApiError.RESOURCE_NOT_FOUND.defaultMessage)
    }

    @Test
    @DisplayName("handleError should map a 418 status code to a RestClientGenericException")
    fun anHttpResponseWithA418StatusCodeShouldBeHandledAsARestClientGenericException() {
        val httpResponse = buildResponseMock()
        Mockito.`when`(httpResponse.statusCode()).thenReturn(418)
        Mockito.`when`(httpResponse.body()).thenReturn("".encodeToByteArray())
        Mockito.`when`(httpResponse.uri()).thenReturn(REQ_URI)

        val thrown = Assertions.catchThrowable { handler!!.handleError(httpResponse) }

        Assertions.assertThat(thrown)
            .isExactlyInstanceOf(RestClientGenericException::class.java)
            .hasMessage(ApiError.INTERNAL_ERROR.defaultMessage)
    }

    private fun buildResponseMock(): HttpResponse<ByteArray> {
        @Suppress("UNCHECKED_CAST") return Mockito.mock(HttpResponse::class.java) as HttpResponse<ByteArray>
    }

    companion object {
        private val REQ_URI = URI.create("http://localhost:1234/some-resource")
    }

}
