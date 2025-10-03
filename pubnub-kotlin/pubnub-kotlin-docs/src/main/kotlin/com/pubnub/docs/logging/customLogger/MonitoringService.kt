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
    }

    fun reportNetworkFailure(url: String, method: String, instanceId: String?) {
    }

    fun logWarning(it: String) {
    }
}
