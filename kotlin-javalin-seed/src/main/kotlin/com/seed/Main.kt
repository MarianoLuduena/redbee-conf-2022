package com.seed

import com.seed.adapter.controller.SWCharacterControllerAdapter
import com.seed.application.port.`in`.GetCharacterByIdInPort
import com.seed.config.ExceptionHandler
import com.seed.config.IoC
import io.javalin.Javalin

fun main() {
    val port = System.getenv("SERVLET_PORT")?.takeIf { it.isNotBlank() }?.toIntOrNull() ?: 8080
    val app = Javalin.create().start(port)

    app.routes { SWCharacterControllerAdapter.routes(IoC.get(GetCharacterByIdInPort::class.java)) }
        .exception(Exception::class.java, ExceptionHandler())
}
