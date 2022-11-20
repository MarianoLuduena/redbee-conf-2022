package com.seed.mock

import com.seed.config.Config

object ConfigMockFactory {

    fun get() =
        Config(
            swCharacterRepository = Config.SWCharacterRepository(
                getByIdUri = "http://localhost:7001/people/{id}",
                getByIdTimeout = 5000L
            )
        )

}
