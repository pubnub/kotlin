package com.pubnub.api.eventengine

import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue

class QueueSinkSource<T>(private val queue: BlockingQueue<T> = LinkedBlockingQueue()) : Sink<T>, Source<T> {
    override fun take(): T {
        return queue.take()
    }

    override fun add(item: T) {
        queue.add(item)
    }
}
