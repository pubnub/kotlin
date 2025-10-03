package com.pubnub.docs.logging.customLogger

class MonitoringService {
    fun reportError(
        message: Any,
        stackTrace: List<String>?,
        errorType: String?,
        instanceId: String?,
        timestamp: String,
        location: String?
    ) {
        // no need to implement
    }

    fun reportNetworkFailure(url: String, method: String, instanceId: String?) {
        // no need to implement
    }

    fun logWarning(it: String) {
        // no need to implement
    }
}
