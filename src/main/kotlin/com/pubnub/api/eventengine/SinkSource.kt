package com.pubnub.api.eventengine

import com.pubnub.api.subscribe.eventengine.event.Event
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue

typealias EventSink = Sink<Event>
typealias EventSource = Source<Event>
typealias EffectSink<T> = Sink<T>
typealias EffectSource<T> = Source<T>

interface Source<T> {
    fun take(): T
}

interface Sink<T> {
    fun add(t: T)
}

class QueueSinkSource<T>(private val queue: BlockingQueue<T> = LinkedBlockingQueue()) : Sink<T>, Source<T> {
    override fun take(): T {
        return queue.take()
    }

    override fun add(t: T) {
        queue.add(t)
    }
}
