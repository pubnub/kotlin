package com.pubnub.api.managers

import com.pubnub.api.enums.PNOperationType
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import kotlin.collections.set

class TelemetryManager {

    private companion object {
        private const val MAX_FRACTION_DIGITS = 3
        private const val TIMESTAMP_DIVIDER = 1_000
        private const val MAXIMUM_LATENCY_DATA_AGE = 60.0
    }

    private val latencies: ConcurrentMap<String, MutableList<Latency>> = ConcurrentHashMap()
    private val numberFormat by lazy {
        NumberFormat.getNumberInstance(Locale.US).apply {
            maximumFractionDigits = MAX_FRACTION_DIGITS
            roundingMode = RoundingMode.HALF_UP
            isGroupingUsed = false
        }
    }

    internal fun operationsLatency(currentDate: Long = Date().time): Map<String, String> {
        cleanUpTelemetryData(currentDate)
        val operationLatencies = HashMap<String, String>()
        latencies.forEach { key, latencies ->
            val latencyKey = "l_$key"
            val endpointAverageLatency = averageLatencyFromData(latencies)
            if (endpointAverageLatency > 0.0f) {
                operationLatencies[latencyKey] = numberFormat.format(endpointAverageLatency)
            }
        }
        return operationLatencies
    }

    private fun cleanUpTelemetryData(currentDate: Long = Date().time) {
        val date = currentDate / TIMESTAMP_DIVIDER.toDouble()

        // remove outdated latencies, may result in an empty list but no null values
        latencies.replaceAll { _, operationLatencies ->
            val outdated = operationLatencies.filterTo(mutableSetOf()) { it.isOutdated(date) }
            operationLatencies.removeAll(outdated)
            operationLatencies
        }
    }

    private fun averageLatencyFromData(endpointLatencies: List<Latency>): Double {
        val sumOfLatencies = endpointLatencies.sumOf { it.latency }
        return sumOfLatencies / endpointLatencies.size
    }

    internal fun storeLatency(latency: Long, type: PNOperationType, currentDate: Long = Date().time) {
        type.queryParam?.let { queryParam: String ->
            if (latency > 0) {
                val storeDate = currentDate / (TIMESTAMP_DIVIDER.toDouble())
                val calculatedLatency = Latency(
                    date = storeDate,
                    latency = latency.toDouble() / TIMESTAMP_DIVIDER
                )
                latencies.merge(queryParam, mutableListOf(calculatedLatency)) { latencies, _ ->
                    latencies.add(calculatedLatency)
                    latencies
                }
            }
        }
    }

    private data class Latency(val latency: Double, val date: Double) {
        fun isOutdated(currentDate: Double) = currentDate - date > MAXIMUM_LATENCY_DATA_AGE
    }
}
