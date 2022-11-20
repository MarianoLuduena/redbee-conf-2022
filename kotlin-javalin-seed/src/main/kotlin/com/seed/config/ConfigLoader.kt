package com.seed.config

import org.slf4j.LoggerFactory
import java.io.BufferedInputStream
import java.io.FileInputStream

object ConfigLoader {

    private val LOG = LoggerFactory.getLogger(this::class.java)
    private val ENV_VARIABLE_REGEX = "^\\\$\\{[A-Z0-9_]+}\$".toRegex() // ${VAR_1}

    fun load(resource: String, envVariables: Map<String, String>): Config {
        LOG.info("Loading properties from {}", resource)
        val props = java.util.Properties()
        BufferedInputStream(javaClass.classLoader.getResourceAsStream(resource) ?: FileInputStream(resource)).use {
            props.load(it)
            LOG.info("Found {} keys in {}", props.size, resource)
        }

        // Replace environment variables
        props.stringPropertyNames().forEach { property ->
            val currentPropValue = props.getProperty(property)
            ENV_VARIABLE_REGEX.find(currentPropValue)?.value?.drop(2)?.dropLast(1)
                ?.also { variable ->
                    envVariables[variable]?.also { envVariableValue -> props.setProperty(property, envVariableValue) }
                }
        }

        return Config.from(props)
    }

}
