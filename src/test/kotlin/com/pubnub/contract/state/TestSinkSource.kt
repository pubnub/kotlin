package com.pubnub.contract.state

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import com.pubnub.api.eventengine.QueueSinkSource
import com.pubnub.api.eventengine.Sink
import com.pubnub.api.eventengine.Source
import java.util.concurrent.LinkedBlockingQueue

class TestSinkSource<T>(
    private val testSink: MutableList<String>,
    private val sinkSource: QueueSinkSource<T> = QueueSinkSource(LinkedBlockingQueue())
) : Sink<T>, Source<T> by sinkSource {

    private val snakeCaseStrategy: SnakeCaseStrategy = SnakeCaseStrategy()
    override fun add(t: T) {
        testSink.add(t!!::class.simpleName!!.toSnakeCase().uppercase())
        sinkSource.add(t)
    }

    override fun take(): T {
        return sinkSource.take()
    }

    private fun String.toSnakeCase(): String = snakeCaseStrategy.translate(this)
}
