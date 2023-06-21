package com.pubnub.api.subscribe.eventengine.event

import com.pubnub.api.eventengine.EventSource
import java.util.concurrent.LinkedBlockingQueue

class EventSourceImpl(
    val queue: LinkedBlockingQueue<Event>
) : EventSource {
    override fun take(): Event {
        return queue.take()
    }
}
