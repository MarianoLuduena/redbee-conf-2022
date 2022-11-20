package com.seed.adapter.rest.exception

import com.seed.config.ApiError

class BadRequestRestClientException(
    error: ApiError,
    cause: Throwable? = null
) : RestClientGenericException(
    error,
    cause
)
