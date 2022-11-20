package com.seed.adapter.rest.client

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration

class RestClient(
    private val httpClient: HttpClient,
    private val objectMapper: ObjectMapper,
    private val errorHandler: RestClientErrorHandler
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    fun <T> get(uri: String, clazz: Class<T>, timeout: Long = 10000L): T {
        val request = HttpRequest.newBuilder(URI.create(uri)).GET().timeout(Duration.ofMillis(timeout)).build()
        log.info("{} {} | Timeout: {}, Headers: {}", request.method(), request.uri(), timeout, request.headers().map())
        val startTime = System.currentTimeMillis()
        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray())

        if (errorHandler.hasError(response)) {
            errorHandler.handleError(response)
        }

        val parsedBody = objectMapper.readValue(response.body(), clazz)
        log.info(
            "{} {} {} in {} ms | Headers: {} | Body: {}",
            response.statusCode(),
            response.request().method(),
            response.uri(),
            System.currentTimeMillis() - startTime,
            response.headers().map(),
            parsedBody
        )

        return parsedBody
    }

}
