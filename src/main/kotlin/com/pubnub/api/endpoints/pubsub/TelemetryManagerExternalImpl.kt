package com.pubnub.api.endpoints.pubsub

import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.managers.TelemetryManager

class TelemetryManagerExternalImpl(val telemetryManager: TelemetryManager) : TelemetryManagerExternal {
    override fun operationsLatency(currentDate: Long): Map<String, String> {
        return telemetryManager.operationsLatency(currentDate)
    }

    override fun storeLatency(latency: Long, type: PNOperationType, currentDate: Long) {
        return telemetryManager.storeLatency(latency, type, currentDate)
    }
}
