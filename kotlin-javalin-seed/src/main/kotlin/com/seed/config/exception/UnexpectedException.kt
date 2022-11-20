package com.seed.config.exception

import com.seed.config.ApiError

open class UnexpectedException(
    error: ApiError,
    cause: Throwable? = null
) : GenericException(
    error.errorCode,
    error.defaultMessage,
    cause
)
