package com.seed.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.seed.adapter.rest.client.RestClient
import com.seed.adapter.rest.StarWarsRestAdapter
import com.seed.adapter.rest.client.RestClientErrorHandler
import com.seed.application.port.`in`.GetCharacterByIdInPort
import com.seed.application.usecase.GetCharacterByIdUseCase
import com.seed.config.exception.UnexpectedException
import java.net.http.HttpClient

object IoC {

    private val RESOURCE_BY_CLASS: Map<Class<out Any>, Any> = kotlin.run {
        val configSource = System.getenv("CONFIG_FILE") ?: "application.properties"
        val config = ConfigLoader.load(configSource, System.getenv())

        val objectMapper =
            ObjectMapper()
                .registerModule(JavaTimeModule())
                .registerModule(KotlinModule.Builder().build())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)

        val restClient = RestClient(HttpClient.newHttpClient(), objectMapper, RestClientErrorHandler())
        val starWarsRestAdapter = StarWarsRestAdapter(config, restClient)
        val getCharacterByIdInPort = GetCharacterByIdUseCase(starWarsRestAdapter)

        mapOf(
            Pair(GetCharacterByIdInPort::class.java, getCharacterByIdInPort)
        )
    }

    fun <T> get(clazz: Class<T>): T {
        return RESOURCE_BY_CLASS[clazz]?.let { @Suppress("UNCHECKED_CAST") return it as T }
            ?: throw UnexpectedException(ApiError.INTERNAL_ERROR)
    }

}
