package com.seed.adapter.controller.exception

import com.seed.config.exception.GenericException

class BadRequestControllerException(
    errorCode: Int,
    errorMessage: String,
    cause: Throwable? = null
) : GenericException(
    errorCode,
    errorMessage,
    cause
)
