package com.pubnub.internal.managers

import com.pubnub.internal.PNConfigurationCore
import com.pubnub.internal.models.server.SubscribeMessage

internal class DuplicationManager(private val config: PNConfigurationCore) {
    private val hashHistory: ArrayList<String> = ArrayList()

    private fun getKey(message: SubscribeMessage) =
        with(message) {
            "${publishMetaData?.publishTimetoken}-${payload.hashCode()}"
        }

    @Synchronized
    fun isDuplicate(message: SubscribeMessage) = hashHistory.contains(getKey(message))

    @Synchronized
    fun addEntry(message: SubscribeMessage) {
        if (hashHistory.size >= config.maximumMessagesCacheSize) {
            hashHistory.removeAt(0)
        }
        hashHistory.add(getKey(message))
    }

    @Synchronized
    fun clearHistory() = hashHistory.clear()
}
