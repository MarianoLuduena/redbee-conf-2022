package com.seed.config

import java.util.Properties

data class Config(val swCharacterRepository: SWCharacterRepository) {

    companion object {
        fun from(props: Properties): Config =
            Config(
                SWCharacterRepository(
                    getByIdUri = props.getProperty("seed.sw-character-repository.get-by-id.uri"),
                    getByIdTimeout = props.getProperty("seed.sw-character-repository.get-by-id.timeout").toLong()
                )
            )
    }

    data class SWCharacterRepository(val getByIdUri: String, val getByIdTimeout: Long)

}
