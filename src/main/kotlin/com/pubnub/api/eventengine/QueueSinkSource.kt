package com.pubnub.api.eventengine

import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue

interface SinkSource<T> : Sink<T>, Source<T>

class QueueSinkSource<T>(private val queue: BlockingQueue<T> = LinkedBlockingQueue()) : SinkSource<T> {
    override fun take(): T {
        return queue.take()
    }

    override fun add(item: T) {
        queue.add(item)
    }
}
