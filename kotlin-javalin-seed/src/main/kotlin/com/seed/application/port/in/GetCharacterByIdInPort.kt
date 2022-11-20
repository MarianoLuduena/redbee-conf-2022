package com.seed.application.port.`in`

import com.seed.domain.SWCharacter

interface GetCharacterByIdInPort {
    fun query(id: Int): SWCharacter
}
