package com.seed.adapter.rest

import com.seed.adapter.rest.client.RestClient
import com.seed.adapter.rest.model.SWCharacterRestModel
import com.seed.application.port.out.GetCharacterByIdOutPort
import com.seed.config.Config
import com.seed.domain.SWCharacter

class StarWarsRestAdapter(
    private val config: Config,
    private val restClient: RestClient
) : GetCharacterByIdOutPort {

    override fun get(id: Int): SWCharacter {
        return restClient.get(
            config.swCharacterRepository.getByIdUri.replace("{id}", id.toString()),
            SWCharacterRestModel::class.java,
            config.swCharacterRepository.getByIdTimeout
        ).toDomain()
    }

}
