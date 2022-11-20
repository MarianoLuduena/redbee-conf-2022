package com.seed.application.usecase

import com.seed.application.port.`in`.GetCharacterByIdInPort
import com.seed.application.port.out.GetCharacterByIdOutPort
import com.seed.domain.SWCharacter
import org.slf4j.LoggerFactory

class GetCharacterByIdUseCase(
    private val getCharacterByIdOutPort: GetCharacterByIdOutPort
) : GetCharacterByIdInPort {

    private val log = LoggerFactory.getLogger(this::class.java)

    override fun query(id: Int): SWCharacter {
        log.info("Getting character with ID {}", id)
        return getCharacterByIdOutPort.get(id).also { log.info("Found character {}", it) }
    }

}
