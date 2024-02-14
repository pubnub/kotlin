package com.pubnub.contract.subscribe.eventEngine.state

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import com.pubnub.internal.eventengine.EffectInvocation
import com.pubnub.internal.eventengine.Event
import com.pubnub.internal.eventengine.QueueSinkSource
import com.pubnub.internal.eventengine.SinkSource
import com.pubnub.internal.eventengine.Source

internal class TestSinkSource<T>(
    private val testSink: MutableList<Pair<String, String>>,
    private val sinkSource: QueueSinkSource<T> = QueueSinkSource()
) : SinkSource<T>, Source<T> by sinkSource {

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
