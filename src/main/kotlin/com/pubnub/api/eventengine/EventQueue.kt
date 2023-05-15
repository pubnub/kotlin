package com.pubnub.api.eventengine

import com.pubnub.api.subscribe.eventengine.event.Event

interface EventQueue {
    fun add(event: Event)
    fun take(): Event
}
