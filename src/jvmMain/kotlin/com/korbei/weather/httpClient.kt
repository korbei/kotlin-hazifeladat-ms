package com.korbei.weather

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.java.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.cache.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

fun createHttpClient(): HttpClient = HttpClient(Java) {
    defaultRequest {
        url {
            url("https://api.open-meteo.com/v1/forecast")
            with(parameters) {
                append("latitude", "47.4984")
                append("longitude", "19.0404")
                append("hourly", "temperature_2m")
                append("timezone", "auto")
            }
        }
    }

    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
        })
    }

    install(HttpCache)

    BrowserUserAgent()
}

suspend inline fun <reified T> HttpResponse.bodyOrThrow(): T {
    if(status.isSuccess()) {
        return body()
    }
    throw Exception("Status: ${status.value} message: ${bodyAsText()}")
}
