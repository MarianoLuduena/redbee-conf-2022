package com.seed.adapter.rest.client

import com.seed.adapter.rest.exception.BadRequestRestClientException
import com.seed.adapter.rest.exception.RestClientGenericException
import com.seed.config.ApiError
import com.seed.config.exception.GenericException
import com.seed.config.exception.ResourceNotFoundException
import org.slf4j.LoggerFactory
import java.net.http.HttpResponse

class RestClientErrorHandler {

    private val log = LoggerFactory.getLogger(this::class.java)

    fun hasError(httpResponse: HttpResponse<ByteArray>): Boolean = httpResponse.statusCode() >= 400

    fun handleError(httpResponse: HttpResponse<ByteArray>) {
        val statusCode = httpResponse.statusCode()
        val body = httpResponse.body().decodeToString().replace("\n", " ")
        log.warn(
            "Error in HTTP request: {} {} | {}",
            statusCode,
            httpResponse.uri().toASCIIString(),
            body
        )
        throw getExceptionByStatusCode(statusCode)
    }

    private fun getExceptionByStatusCode(statusCode: Int): GenericException =
        EXCEPTIONS_BY_STATUS_CODE.getOrDefault(statusCode) { RestClientGenericException(ApiError.INTERNAL_ERROR) }
            .invoke()

    companion object {
        private val EXCEPTIONS_BY_STATUS_CODE: Map<Int, () -> GenericException> =
            mapOf(
                Pair(400) { BadRequestRestClientException(ApiError.BAD_REQUEST) },
                Pair(404) { ResourceNotFoundException(ApiError.RESOURCE_NOT_FOUND) }
            )
    }

}
