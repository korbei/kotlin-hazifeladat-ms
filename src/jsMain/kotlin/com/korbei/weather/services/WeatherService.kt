package com.korbei.weather.services

import com.korbei.weather.ApiResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class WeatherService(private val httpClient: HttpClient) {

    suspend fun getForecastData() = coroutineScope {
        val forecastRequest: Deferred<ApiResponse> = async { httpClient.get("/api/forecast").body() }

        forecastRequest.await()
    }
}
