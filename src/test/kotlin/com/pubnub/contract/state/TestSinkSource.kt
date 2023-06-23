package com.pubnub.contract.state

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import com.pubnub.api.eventengine.EffectInvocation
import com.pubnub.api.eventengine.Event
import com.pubnub.api.eventengine.QueueSinkSource
import com.pubnub.api.eventengine.Sink
import com.pubnub.api.eventengine.Source
import java.util.concurrent.LinkedBlockingQueue

class TestSinkSource<T>(
    private val testSink: MutableList<Pair<String, String>>,
    private val sinkSource: QueueSinkSource<T> = QueueSinkSource(LinkedBlockingQueue())
) : Sink<T>, Source<T> by sinkSource {

    private val snakeCaseStrategy: SnakeCaseStrategy = SnakeCaseStrategy()
    override fun add(t: T) {
        testSink.add(t.type() to t.name())
        sinkSource.add(t)
    }

    override fun take(): T {
        return sinkSource.take()
    }

    private fun T.name() = this!!::class.simpleName!!.toSnakeCase().uppercase()

    private fun String.toSnakeCase(): String = snakeCaseStrategy.translate(this)

    private fun T.type(): String = when (this) {
        is Event -> "event"
        is EffectInvocation -> "invocation"
        else -> "unknown"
    }
}
