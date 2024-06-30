package com.korbei.weather

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse(
    val latitude: Double,
    val longitude: Double,
    @SerialName("generationtime_ms")
    val generationtimeMs: Double,
    @SerialName("utc_offset_seconds")
    val utcOffsetSeconds: Int,
    val timezone: String,
    @SerialName("timezone_abbreviation")
    val timezoneAbbreviation: String,
    val elevation: Double,
    @SerialName("hourly_units")
    val hourlyUnits: HourlyUnits,
    val hourly: Hourly
) {
    fun toDataMap() = hourly.time.zip(hourly.temperature2m).groupBy {
        it.first.split("T")[0]
    }
}

@Serializable
data class HourlyUnits(
    val time: String,
    @SerialName("temperature_2m")
    val temperature2m: String
)

@Serializable
data class Hourly(
    val time: List<String>,
    @SerialName("temperature_2m")
    val temperature2m: List<Double>
)
