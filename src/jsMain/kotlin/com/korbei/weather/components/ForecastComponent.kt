package com.korbei.weather.components

import com.korbei.weather.ApiResponse
import kotlinx.html.*
import kotlinx.html.js.onClickFunction

const val CANVAS_ID = "forecast-chart"

fun FlowContent.forecastComponent(data: ApiResponse? = null, onDateSelect: (String) -> Unit = {}) {
    div("forecast-container") {
        data?.let {
            val weatherData = data.toDataMap()

            div("flex justify-content-center gap-5") {
                weatherData.keys.forEach { key ->
                    div("date-selector") {
                        text(key)
                        onClickFunction = {
                            onDateSelect(key)
                        }
                    }
                }
            }
            canvas {
                id = CANVAS_ID
            }

        } ?: div("message") {
            text("No data")
        }
    }
}
