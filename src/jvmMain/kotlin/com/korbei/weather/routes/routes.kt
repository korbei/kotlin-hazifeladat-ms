package com.korbei.weather.routes

import com.korbei.weather.services.WeatherService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.apiRoutes(weatherService: WeatherService) {
    get("api/forecast") {
        call.respond(weatherService.getForecastData())
    }
}

