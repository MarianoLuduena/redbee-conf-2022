package com.seed.extension

import io.javalin.core.validation.ValidationError

fun Map<String, List<ValidationError<Any>>>.message(): String {
    return this.map { entry -> "${entry.key} ${entry.value.joinToString { error -> error.message }}" }
        .joinToString(separator = "; ")
}
