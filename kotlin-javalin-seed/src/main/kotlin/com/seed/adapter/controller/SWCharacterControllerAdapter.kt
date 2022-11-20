package com.seed.adapter.controller

import com.seed.adapter.controller.exception.BadRequestControllerException
import com.seed.adapter.controller.model.SWCharacterControllerModel
import com.seed.application.port.`in`.GetCharacterByIdInPort
import com.seed.config.ApiError
import com.seed.extension.message
import io.javalin.apibuilder.ApiBuilder
import io.javalin.http.Context
import org.slf4j.LoggerFactory

class SWCharacterControllerAdapter(
    private val getCharacterByIdInPort: GetCharacterByIdInPort
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    fun get(ctx: Context) {
        val id =
            ctx.pathParamAsClass<Int>("id")
                .check({ i: Int -> i > 0 }, "must be greater than 0")
                .getOrThrow { throw BadRequestControllerException(ApiError.BAD_REQUEST.errorCode, it.message()) }

        log.info("Getting character with ID {}", id)
        val response = SWCharacterControllerModel.from(getCharacterByIdInPort.query(id))
        log.info("Response to GET /characters/{}: {}", id, response)
        ctx.json(response)
    }

    fun getAll(ctx: Context) {
        ctx.json(emptyList<Void>())
    }

    companion object {
        fun routes(getCharacterByIdInPort: GetCharacterByIdInPort) {
            val adapter = SWCharacterControllerAdapter(getCharacterByIdInPort)
            ApiBuilder.path("/characters") {
                ApiBuilder.get { ctx -> adapter.getAll(ctx) }
                ApiBuilder.get("{id}") { ctx -> adapter.get(ctx) }
            }
        }
    }

}
