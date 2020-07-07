package com.pubnub.api.managers

import com.pubnub.api.enums.PNOperationType
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Date
import java.util.Locale
import java.util.Timer
import java.util.TimerTask
import kotlin.collections.set

internal class TelemetryManager {

    private companion object {
        private const val MAX_FRACTION_DIGITS = 3
        private const val TIMESTAMP_DIVIDER = 1000
        private const val MAXIMUM_LATENCY_DATA_AGE = 60.0
        private const val CLEAN_UP_INTERVAL = 1
        private const val CLEAN_UP_INTERVAL_MULTIPLIER = 1000
    }

    private var timer: Timer? = Timer()
    private val latencies: HashMap<String, MutableList<Map<String, Double>>> = HashMap()
    private val numberFormat by lazy {
        NumberFormat.getNumberInstance(Locale.US).apply {
            maximumFractionDigits = MAX_FRACTION_DIGITS
            roundingMode = RoundingMode.HALF_UP
            isGroupingUsed = false
        }
    }

    init {
        startCleanUpTimer()
    }

    @Synchronized
    fun operationsLatency(): Map<String, String> {
        val operationLatencies = HashMap<String, String>()
        latencies.entries.forEach {
            val latencyKey = "l_${it.key}"
            val endpointAverageLatency = averageLatencyFromData(it.value)
            if (endpointAverageLatency > 0.0f) {
                operationLatencies[latencyKey] = numberFormat.format(endpointAverageLatency)
            }
        }
        return operationLatencies
    }

    private fun startCleanUpTimer() {
        val interval = (CLEAN_UP_INTERVAL * CLEAN_UP_INTERVAL_MULTIPLIER).toLong()

        stopCleanUpTimer()
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                cleanUpTelemetryData()
            }
        }, interval, interval)
    }

    internal fun stopCleanUpTimer() {
        this.timer?.cancel()
    }

    @Synchronized
    private fun cleanUpTelemetryData() {
        val currentDate = Date().time / TIMESTAMP_DIVIDER.toDouble()

        latencies.forEach { (endpoint, _) ->
            val outdatedLatencies = mutableListOf<Map<String, Double>>()
            val operationLatencies = latencies[endpoint]
            operationLatencies?.run {
                forEach { latencyInformation ->
                    if (currentDate - latencyInformation["d"]!! > MAXIMUM_LATENCY_DATA_AGE) {
                        outdatedLatencies.add(latencyInformation)
                    }
                }
                if (outdatedLatencies.isNotEmpty()) {
                    removeAll(outdatedLatencies)
                }
                if (operationLatencies.isEmpty()) {
                    latencies.remove(endpoint)
                }
            }
        }
    }

    private fun averageLatencyFromData(endpointLatencies: List<Map<String, Double>>): Double {
        var totalLatency = 0.0
        endpointLatencies.forEach {
            it["l"]?.let { l: Double ->
                totalLatency += l
            }
        }
        return totalLatency / endpointLatencies.size
    }

    @Synchronized
    internal fun storeLatency(latency: Long, type: PNOperationType) {
        type.queryParam?.let { queryParam: String ->
            if (latency > 0) {
                val storeDate = Date().time / (TIMESTAMP_DIVIDER.toDouble())

                if (latencies[queryParam] == null) {
                    latencies[queryParam] = ArrayList()
                }

                latencies[queryParam]?.let {
                    latencies[queryParam] = it

                    val latencyEntry = java.util.HashMap<String, Double>()
                    latencyEntry["d"] = storeDate
                    latencyEntry["l"] = latency.toDouble() / TIMESTAMP_DIVIDER
                    it.add(latencyEntry)
                }
            }
        }
    }
}
