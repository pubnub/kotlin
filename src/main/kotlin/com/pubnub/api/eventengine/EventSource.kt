package com.pubnub.api.eventengine

import com.pubnub.api.subscribe.eventengine.event.Event

interface EventSource {
    fun take(): Event
}
