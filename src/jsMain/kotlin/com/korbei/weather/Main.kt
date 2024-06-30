package com.korbei.weather

import com.korbei.weather.services.WeatherService
import com.korbei.weather.views.WeatherView
import io.ktor.client.*
import io.ktor.client.engine.js.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.serialization.json.Json

val httpClient = HttpClient(Js) {
    install(ContentNegotiation) { json(Json) }
}

val weatherService = WeatherService(httpClient)
val weatherView = WeatherView(weatherService)

fun main() {
    window.onload = {
        val main = document.querySelector("body")!!
        weatherView.render(main)
    }
}
