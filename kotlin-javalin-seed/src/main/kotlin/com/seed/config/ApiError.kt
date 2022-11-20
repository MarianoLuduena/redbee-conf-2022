package com.seed.config

enum class ApiError(val errorCode: Int, val defaultMessage: String) {
    INTERNAL_ERROR(100, "Internal server error"),
    BAD_REQUEST(101, "Bad request"),
    RESOURCE_NOT_FOUND(102, "Resource not found"),
    TIMEOUT_FOUND(103, "Timeout")
}
