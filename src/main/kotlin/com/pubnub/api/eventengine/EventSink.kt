package com.pubnub.api.eventengine

import com.pubnub.api.subscribe.eventengine.event.Event

interface EventSink {
    fun put(event: Event)
}
