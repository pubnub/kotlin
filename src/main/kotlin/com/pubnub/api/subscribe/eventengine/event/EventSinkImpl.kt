package com.pubnub.api.subscribe.eventengine.event

import com.pubnub.api.eventengine.EventSink
import java.util.concurrent.LinkedBlockingQueue

class EventSinkImpl(
    val queue: LinkedBlockingQueue<Event>

) : EventSink {
    override fun add(event: Event) {
        queue.add(event)
    }
}
