@file:JsModule("chart.js/auto")
@file:JsNonModule
package com.korbei.weather.external.chart

import org.w3c.dom.Element

@JsName("Chart")
external class Chart(canvas: Element, config: ChartConfig) {
    var data: ChartData

    fun update()
}

external interface ChartConfig {
    var type: String
    var data: ChartData
}

external interface ChartData {
    var labels: Array<String>
    var datasets: Array<Dataset>
}

external interface Dataset {
    var label: String
    var data: Array<Any>
    var fill: Boolean
    var borderColor: String
    var tension: Double
    var pointStyle: Any?
}
