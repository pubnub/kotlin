package com.pubnub.contract.subscribe.eventEngine.state

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import com.pubnub.api.eventengine.EffectInvocation
import com.pubnub.api.eventengine.Event
import com.pubnub.api.eventengine.QueueSinkSource
import com.pubnub.api.eventengine.Sink
import com.pubnub.api.eventengine.Source

class TestSinkSource<T>(
    private val testSink: MutableList<Pair<String, String>>,
    private val sinkSource: QueueSinkSource<T> = QueueSinkSource()
) : Sink<T>, Source<T> by sinkSource {

    private val snakeCaseStrategy: SnakeCaseStrategy = SnakeCaseStrategy()
    override fun add(item: T) {
        testSink.add(item.type() to item.name())
        sinkSource.add(item)
    }

    private fun T.name() = this!!::class.simpleName!!.toSnakeCase().uppercase()

    private fun String.toSnakeCase(): String = snakeCaseStrategy.translate(this)

    private fun T.type(): String = when (this) {
        is Event -> "event"
        is EffectInvocation -> "invocation"
        else -> "unknown"
    }
}
