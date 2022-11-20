package com.seed.adapter.rest.exception

import com.seed.config.ApiError
import com.seed.config.exception.GenericException

open class RestClientGenericException(
    error: ApiError,
    cause: Throwable? = null
) : GenericException(
    error.errorCode,
    error.defaultMessage,
    cause
)
