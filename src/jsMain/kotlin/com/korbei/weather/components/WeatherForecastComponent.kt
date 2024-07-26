package com.korbei.weather.components

import com.korbei.weather.ApiResponse
import com.korbei.weather.external.chart.Chart
import com.korbei.weather.external.chart.ChartConfig
import com.korbei.weather.external.chart.ChartData
import com.korbei.weather.external.chart.Dataset
import kotlinx.browser.document
import kotlinx.html.*
import kotlinx.html.js.onClickFunction
import org.w3c.dom.MutationObserver
import org.w3c.dom.MutationObserverInit
import org.w3c.dom.Node

const val CANVAS_ID = "chart-canvas"

class WeatherForecastComponent {

    lateinit var data: ApiResponse
    private lateinit var chart: Chart

    fun render(consumer: TagConsumer<*>) {
        consumer.div("forecast-container") {

            val weatherData = data.toDataMap()

            div("flex justify-content-center gap-5") {
                weatherData.keys.forEach { key ->
                    div("date-selector") {
                        text(key)
                        onClickFunction = {
                            chart.data = createChartConfig(data.toDataMap()[key]!!, key).data
                            chart.update()
                        }
                    }
                }
            }
            canvas {
                id = CANVAS_ID
            }
        }

        initChart(data)
    }

    private fun initChart(forecastData: ApiResponse) {
        val mutationObserver = MutationObserver { mutationRecords, mutationObserver ->
            mutationRecords.forEach {
                if (it.type == "childList") {
                    val canvas = document.getElementById(CANVAS_ID)
                    val forecastDataMap = forecastData.toDataMap()
                    val firstDay = forecastDataMap.keys.first()
                    chart = Chart(canvas!!, createChartConfig(forecastDataMap[firstDay]!!, firstDay))
                    mutationObserver.disconnect()
                }
            }
        }

        mutationObserver.observe(
            document.querySelector("body")!! as Node,
            MutationObserverInit(childList = true, attributes = false)
        )
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
                        data =
                            DoubleArray(weatherData.map { it.second }.size) { _ -> averageTemp }.toList().toTypedArray()
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

fun DIV.weatherForecastComponent(block: WeatherForecastComponent.() -> Unit) {
    WeatherForecastComponent().apply(block).render(consumer)
}
