package com.pubnub.docs.logging.customlogger

class MonitoringService {
    fun reportError(
        message: Any,
        stackTrace: List<String>?,
        errorType: String?,
        instanceId: String?,
        timestamp: String,
        location: String?
    ) {
        // no need to implement this is not part of sample
    }

    fun reportNetworkFailure(url: String, method: String, instanceId: String?) {
        // no need to implement this is not part of sample
    }

    fun logWarning(it: String) {
        // no need to implement this is not part of sample
    }
}
