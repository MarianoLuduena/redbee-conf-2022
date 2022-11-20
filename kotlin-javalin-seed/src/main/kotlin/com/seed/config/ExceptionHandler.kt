package com.seed.config

import com.fasterxml.jackson.annotation.JsonFormat
import com.seed.adapter.controller.exception.BadRequestControllerException
import com.seed.adapter.rest.exception.BadRequestRestClientException
import com.seed.adapter.rest.exception.RestClientGenericException
import com.seed.config.exception.ResourceNotFoundException
import io.javalin.http.Context
import io.javalin.http.ExceptionHandler
import io.javalin.http.HttpCode
import io.javalin.http.HttpResponseException
import org.slf4j.LoggerFactory
import java.lang.Exception
import java.net.http.HttpTimeoutException
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.concurrent.CompletionException

class ExceptionHandler : ExceptionHandler<Exception> {

    override fun handle(exception: Exception, ctx: Context) {
        val errorResponse = Companion.handle(exception, ctx.req.requestURI)
        ctx.status(errorResponse.status).json(errorResponse)
    }

    data class ErrorResponse(
        val name: String,
        val status: Int,
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        val timestamp: ZonedDateTime,
        val code: Int,
        val resource: String,
        val detail: String
    )

    companion object {

        private val LOG = LoggerFactory.getLogger(this::class.java)

        private val HANDLERS_BY_EXCEPTION_CLASS: Map<Class<out Throwable>, (Throwable, String) -> ErrorResponse> =
            mapOf(
                Pair(HttpResponseException::class.java) { ex, r ->
                    handleHttpException(
                        ex as HttpResponseException,
                        r
                    )
                },
                Pair(CompletionException::class.java) { ex, r ->
                    handleCompletionException(
                        ex as CompletionException,
                        r
                    )
                },
                Pair(ResourceNotFoundException::class.java) { ex, r ->
                    handleResourceNotFoundException(
                        ex as ResourceNotFoundException,
                        r
                    )
                },
                Pair(BadRequestRestClientException::class.java) { ex, r ->
                    handleBadRequestRestClientException(
                        ex as BadRequestRestClientException,
                        r
                    )
                },
                Pair(RestClientGenericException::class.java) { ex, r ->
                    handleRestClientGenericException(
                        ex as RestClientGenericException,
                        r
                    )
                },
                Pair(BadRequestControllerException::class.java) { ex, r ->
                    handleBadRequestControllerException(
                        ex as BadRequestControllerException,
                        r
                    )
                },
                Pair(HttpTimeoutException::class.java) { ex, r ->
                    handleHttpTimeoutException(
                        ex as HttpTimeoutException,
                        r
                    )
                }
            )

        private fun handle(exception: Throwable, resource: String): ErrorResponse {
            return HANDLERS_BY_EXCEPTION_CLASS[exception.javaClass]
                ?.invoke(exception, resource)
                ?: handleDefault(exception, resource)
        }

        private fun handleBadRequestRestClientException(
            exception: BadRequestRestClientException,
            resource: String
        ): ErrorResponse {
            val httpCode = HttpCode.BAD_REQUEST
            LOG.error(httpCode.message, exception)
            return buildResponse(httpCode, exception, resource, exception.errorCode)
        }

        private fun handleResourceNotFoundException(
            exception: ResourceNotFoundException,
            resource: String
        ): ErrorResponse {
            val httpCode = HttpCode.NOT_FOUND
            LOG.error(httpCode.message, exception)
            return buildResponse(httpCode, exception, resource, exception.errorCode)
        }

        private fun handleRestClientGenericException(
            exception: RestClientGenericException,
            resource: String
        ): ErrorResponse {
            val httpCode = HttpCode.INTERNAL_SERVER_ERROR
            LOG.error(httpCode.message, exception)
            return buildResponse(httpCode, exception, resource, exception.errorCode)
        }

        private fun handleHttpException(exception: HttpResponseException, resource: String): ErrorResponse {
            val httpCode = HttpCode.forStatus(exception.status)!!
            LOG.error(httpCode.message, exception)
            return buildResponse(httpCode, exception, resource, ApiError.INTERNAL_ERROR.errorCode)
        }

        private fun handleBadRequestControllerException(
            exception: BadRequestControllerException,
            resource: String
        ): ErrorResponse {
            val httpCode = HttpCode.BAD_REQUEST
            LOG.error(httpCode.message, exception)
            return buildResponse(httpCode, exception, resource, exception.errorCode)
        }

        private fun handleHttpTimeoutException(
            exception: HttpTimeoutException,
            resource: String
        ): ErrorResponse {
            val httpCode = HttpCode.GATEWAY_TIMEOUT
            val error = ApiError.TIMEOUT_FOUND
            LOG.error(httpCode.message, exception)
            return buildResponse(httpCode, exception, resource, error.errorCode, error.defaultMessage)
        }

        private fun handleCompletionException(exception: CompletionException, resource: String): ErrorResponse {
            return exception.cause?.let { handle(it, resource) } ?: handleDefault(exception, resource)
        }

        private fun handleDefault(exception: Throwable, resource: String): ErrorResponse {
            val httpCode = HttpCode.INTERNAL_SERVER_ERROR
            val error = ApiError.INTERNAL_ERROR
            LOG.error(httpCode.message, exception)
            return buildResponse(httpCode, exception, resource, error.errorCode, error.defaultMessage)
        }

        private fun buildResponse(
            httpCode: HttpCode,
            ex: Throwable,
            resource: String,
            code: Int,
            message: String = ex.message ?: ""
        ): ErrorResponse {
            return ErrorResponse(
                name = httpCode.message,
                status = httpCode.status,
                timestamp = ZonedDateTime.now().withZoneSameInstant(ZoneOffset.UTC),
                code = code,
                detail = message,
                resource = resource
            )
        }

    }

}
