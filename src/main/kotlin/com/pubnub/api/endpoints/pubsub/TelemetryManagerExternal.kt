package com.pubnub.api.endpoints.pubsub

import com.pubnub.api.enums.PNOperationType
import java.util.Date

interface TelemetryManagerExternal {
    fun operationsLatency(currentDate: Long = Date().time): Map<String, String>
    fun storeLatency(latency: Long, type: PNOperationType, currentDate: Long = Date().time)
}
