package com.pubnub.api.eventengine

import com.pubnub.api.subscribe.eventengine.event.Event

interface EventHandler {
    fun handleEvent(event: Event)
}
