package com.seed.application.port.out

import com.seed.domain.SWCharacter

interface GetCharacterByIdOutPort {
    fun get(id: Int): SWCharacter
}
