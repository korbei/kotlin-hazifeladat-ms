package com.korbei.weather.services

import com.korbei.weather.ApiResponse
import com.korbei.weather.bodyOrThrow
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.ContentType.*
import kotlinx.coroutines.runBlocking

class WeatherService(
   private val httpClient: HttpClient
) {

    fun getForecastData(): ApiResponse = runBlocking {
        httpClient.get() {
            accept(Application.Json)
        }.bodyOrThrow()
    }
}
