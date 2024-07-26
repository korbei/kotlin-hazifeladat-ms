package com.korbei.weather.views

import com.korbei.weather.components.weatherForecastComponent
import com.korbei.weather.services.WeatherService
import kotlinx.browser.document
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.html.div
import kotlinx.html.dom.create
import kotlinx.html.js.main
import org.w3c.dom.Element
import kotlin.coroutines.EmptyCoroutineContext

class WeatherView(private val weatherService: WeatherService) {
    private val scope =  CoroutineScope(EmptyCoroutineContext)
    private var job: Job? = null

    fun render(parentElement: Element) {
        job = scope.launch {
            val forecastData = weatherService.getForecastData()
            parentElement.append(document.create.main {
                div {
                    weatherForecastComponent {
                        data = forecastData
                    }
                }
            })
        }
    }
}
