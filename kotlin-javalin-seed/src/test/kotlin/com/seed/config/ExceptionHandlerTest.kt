package com.seed.config

import com.seed.adapter.controller.exception.BadRequestControllerException
import com.seed.adapter.rest.exception.BadRequestRestClientException
import com.seed.adapter.rest.exception.RestClientGenericException
import com.seed.config.exception.ResourceNotFoundException
import io.javalin.http.Context
import io.javalin.http.HttpCode
import io.javalin.http.HttpResponseException
import io.javalin.plugin.json.JSON_MAPPER_KEY
import io.javalin.plugin.json.JsonMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.lang.RuntimeException
import java.net.http.HttpTimeoutException
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.concurrent.CompletionException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@DisplayName("ExceptionHandler Test")
class ExceptionHandlerTest {

    private val httpServletRequest = Mockito.mock(HttpServletRequest::class.java)
    private val httpServletResponse = Mockito.mock(HttpServletResponse::class.java)
    private val jsonMapper = Mockito.mock(JsonMapper::class.java)
    private val context: Context =
        Context(
            httpServletRequest,
            httpServletResponse,
            mapOf(Pair(JSON_MAPPER_KEY, jsonMapper))
        )

    private var handler: ExceptionHandler? = null

    @BeforeEach
    fun setup() {
        Mockito.`when`(httpServletRequest.requestURI).thenReturn(RESOURCE)
        Mockito.`when`(jsonMapper.toJsonString(Mockito.any())).thenReturn("{}")
        Mockito.doNothing().`when`(httpServletResponse).status = Mockito.anyInt()
        Mockito.doNothing().`when`(httpServletResponse).contentType = Mockito.anyString()

        handler = ExceptionHandler()
    }

    @Test
    fun handleResourceNotFoundException() {
        val error = ApiError.RESOURCE_NOT_FOUND
        val exception = ResourceNotFoundException(error)
        val httpCode = HttpCode.NOT_FOUND

        handler!!.handle(exception, context)

        val expected = buildResponse(httpCode, error.errorCode, error.defaultMessage)
        Mockito.verify(httpServletResponse, Mockito.times(1)).status = httpCode.status
        Mockito.verify(jsonMapper, Mockito.times(1))
            .toJsonString(Mockito.argThat { it.matches(expected) })
    }

    @Test
    fun handleBadRequestRestClientException() {
        val error = ApiError.BAD_REQUEST
        val exception = BadRequestRestClientException(error)
        val httpCode = HttpCode.BAD_REQUEST

        handler!!.handle(exception, context)

        val expected = buildResponse(httpCode, error.errorCode, error.defaultMessage)
        Mockito.verify(httpServletResponse, Mockito.times(1)).status = httpCode.status
        Mockito.verify(jsonMapper, Mockito.times(1))
            .toJsonString(Mockito.argThat { it.matches(expected) })
    }

    @Test
    fun handleRestClientGenericException() {
        val error = ApiError.INTERNAL_ERROR
        val exception = RestClientGenericException(error)
        val httpCode = HttpCode.INTERNAL_SERVER_ERROR

        handler!!.handle(exception, context)

        val expected = buildResponse(httpCode, error.errorCode, error.defaultMessage)
        Mockito.verify(httpServletResponse, Mockito.times(1)).status = httpCode.status
        Mockito.verify(jsonMapper, Mockito.times(1))
            .toJsonString(Mockito.argThat { it.matches(expected) })
    }

    @Test
    fun handleHttpResponseException() {
        val httpCode = HttpCode.IM_A_TEAPOT
        val exception = HttpResponseException(httpCode.status, httpCode.message)

        handler!!.handle(exception, context)

        val expected = buildResponse(httpCode, ApiError.INTERNAL_ERROR.errorCode, httpCode.message)
        Mockito.verify(httpServletResponse, Mockito.times(1)).status = httpCode.status
        Mockito.verify(jsonMapper, Mockito.times(1))
            .toJsonString(Mockito.argThat { it.matches(expected) })
    }

    @Test
    fun handleBadRequestControllerException() {
        val error = ApiError.BAD_REQUEST
        val exception = BadRequestControllerException(error.errorCode, "some error")
        val httpCode = HttpCode.BAD_REQUEST

        handler!!.handle(exception, context)

        val expected = buildResponse(httpCode, error.errorCode, "some error")
        Mockito.verify(httpServletResponse, Mockito.times(1)).status = httpCode.status
        Mockito.verify(jsonMapper, Mockito.times(1))
            .toJsonString(Mockito.argThat { it.matches(expected) })
    }

    @Test
    fun handleHttpTimeoutException() {
        val error = ApiError.TIMEOUT_FOUND
        val exception = HttpTimeoutException("Timeout message")
        val httpCode = HttpCode.GATEWAY_TIMEOUT

        handler!!.handle(exception, context)

        val expected = buildResponse(httpCode, error.errorCode, error.defaultMessage)
        Mockito.verify(httpServletResponse, Mockito.times(1)).status = httpCode.status
        Mockito.verify(jsonMapper, Mockito.times(1))
            .toJsonString(Mockito.argThat { it.matches(expected) })
    }

    @Test
    fun handleCompletionExceptionShouldHandleTheUnderlyingException() {
        val error = ApiError.RESOURCE_NOT_FOUND
        val exception = CompletionException(ResourceNotFoundException(error))
        val httpCode = HttpCode.NOT_FOUND

        handler!!.handle(exception, context)

        val expected = buildResponse(httpCode, error.errorCode, error.defaultMessage)
        Mockito.verify(httpServletResponse, Mockito.times(1)).status = httpCode.status
        Mockito.verify(jsonMapper, Mockito.times(1))
            .toJsonString(Mockito.argThat { it.matches(expected) })
    }

    @Test
    fun handleDefaultException() {
        val error = ApiError.INTERNAL_ERROR
        val exception = RuntimeException("some error")
        val httpCode = HttpCode.INTERNAL_SERVER_ERROR

        handler!!.handle(exception, context)

        val expected = buildResponse(httpCode, error.errorCode, error.defaultMessage)
        Mockito.verify(httpServletResponse, Mockito.times(1)).status = httpCode.status
        Mockito.verify(jsonMapper, Mockito.times(1))
            .toJsonString(Mockito.argThat { it.matches(expected) })
    }

    private fun buildResponse(httpCode: HttpCode, code: Int, message: String): ExceptionHandler.ErrorResponse =
        ExceptionHandler.ErrorResponse(
            name = httpCode.message,
            status = httpCode.status,
            timestamp = ZonedDateTime.now().withZoneSameInstant(ZoneOffset.UTC),
            code = code,
            detail = message,
            resource = RESOURCE
        )

    companion object {
        private const val RESOURCE = "/some-resource"

        private fun Any.matches(that: ExceptionHandler.ErrorResponse): Boolean {
            if (this !is ExceptionHandler.ErrorResponse) {
                return false
            }
            return this.code == that.code
                    && this.detail == that.detail
                    && this.name == that.name
                    && this.resource == that.resource
                    && this.status == that.status
        }
    }

}
