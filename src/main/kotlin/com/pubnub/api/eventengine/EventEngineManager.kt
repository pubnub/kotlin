package com.pubnub.api.eventengine

interface EventEngineManager {
    fun addEventToQueue(event: Event)
    fun start()
    fun stop()
}
