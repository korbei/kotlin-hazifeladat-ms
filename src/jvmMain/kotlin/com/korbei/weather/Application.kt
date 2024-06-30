package com.korbei.weather

import com.korbei.weather.routes.apiRoutes
import com.korbei.weather.services.WeatherService
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    routing {
        val httpClient = createHttpClient()
        val weatherService = WeatherService(httpClient)
        apiRoutes(weatherService)
        staticResources("/", "static")
    }
}

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }
}
