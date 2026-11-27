@file:Suppress("ktlint")

package com.pubnub.docs.logging.samples

// The services the custom logger sample talks to. The docs sample leaves them out ("not part of the sample"). Here they
// record every call, so that a test can prove what the logger forwarded.

class MonitoringService {
    fun reportError(
        message: Any,
        stackTrace: List<String>?,
        errorType: String?,
        instanceId: String?,
        timestamp: String,
        location: String?
    ) {
        errors.add(message.toString())
    }

    fun reportNetworkFailure(url: String, method: String, instanceId: String?) {
        networkFailures.add("$method $url")
    }

    fun logWarning(it: String) {
        warnings.add(it)
    }

    companion object {
        val errors = java.util.concurrent.CopyOnWriteArrayList<String>()
        val networkFailures = java.util.concurrent.CopyOnWriteArrayList<String>()
        val warnings = java.util.concurrent.CopyOnWriteArrayList<String>()

        fun clear() {
            errors.clear()
            networkFailures.clear()
            warnings.clear()
        }
    }
}

class AnalyticsService {
    fun trackConfigChange(instanceId: String?, details: Map<String, Any>?) {
        configChanges.add(instanceId.orEmpty())
    }

    companion object {
        val configChanges = java.util.concurrent.CopyOnWriteArrayList<String>()

        fun clear() = configChanges.clear()
    }
}
