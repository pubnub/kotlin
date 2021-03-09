package com.pubnub.api.managers

import com.pubnub.api.PNConfiguration
import com.pubnub.api.models.server.SubscribeMessage

internal class DuplicationManager(private val config: PNConfiguration) {

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
