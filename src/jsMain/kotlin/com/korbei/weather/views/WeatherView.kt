package com.korbei.weather.views

import com.korbei.weather.ApiResponse
import com.korbei.weather.components.CANVAS_ID
import com.korbei.weather.components.forecastComponent
import com.korbei.weather.external.chart.Chart
import com.korbei.weather.external.chart.ChartConfig
import com.korbei.weather.external.chart.ChartData
import com.korbei.weather.external.chart.Dataset
import com.korbei.weather.services.WeatherService
import kotlinx.browser.document
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.html.dom.create
import kotlinx.html.js.main
import org.w3c.dom.Element
import kotlin.coroutines.EmptyCoroutineContext

class WeatherView(private val weatherService: WeatherService) {
    private val scope =  CoroutineScope(EmptyCoroutineContext)
    private var job: Job? = null
    private lateinit var chart: Chart

    fun render(parentElement: Element) {
        job = scope.launch {
            val forecastData = weatherService.getForecastData()
            parentElement.append(document.create.main {
                forecastComponent(forecastData) { selectedDate ->
                    chart.data = createChartConfig(forecastData.toDataMap()[selectedDate]!!, selectedDate).data
                    chart.update()
                }
            })
            initChart(forecastData)
        }
    }

    private fun initChart(forecastData: ApiResponse) {
        val canvas = document.getElementById(CANVAS_ID)
        val forecastDataMap = forecastData.toDataMap()
        val firstDay = forecastDataMap.keys.first()
        chart = Chart(canvas!!, createChartConfig(forecastDataMap[firstDay]!!, firstDay))
    }

    private fun createChartConfig(weatherData: List<Pair<String, Double>>, chartLabel: String): ChartConfig {
        val averageTemp = weatherData.map { it.second }.average()
        return jsConfig<ChartConfig>().apply {
            type = "line"
            data = jsConfig<ChartData>().apply {
                labels = weatherData.map { it.first }.toTypedArray()
                datasets = arrayOf(
                    jsConfig<Dataset>().apply {
                        label = chartLabel
                        data = weatherData.map { it.second }.toTypedArray()
                        fill = false
                        borderColor = "rgb(75, 192, 192)"
                        tension = 0.1
                    },
                    jsConfig<Dataset>().apply {
                        label = "average temp: ${toFixed(averageTemp, 2)} °C"
                        data = DoubleArray(weatherData.map { it.second }.size) { _ -> averageTemp }.toList().toTypedArray()
                        fill = false
                        borderColor = "#1aee77"
                        pointStyle = false
                        tension = 0.1
                    })
            }
        }
    }

    private fun toFixed(value: Double, digits: Int): Double = js("value.toFixed(digits)")

    private fun <T> jsConfig(): T = js("{}")
}
