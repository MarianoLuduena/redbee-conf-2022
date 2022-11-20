package com.seed.config.exception

import com.seed.config.ApiError

open class ResourceNotFoundException(
    error: ApiError,
    cause: Throwable? = null
) : GenericException(
    error.errorCode,
    error.defaultMessage,
    cause
)
