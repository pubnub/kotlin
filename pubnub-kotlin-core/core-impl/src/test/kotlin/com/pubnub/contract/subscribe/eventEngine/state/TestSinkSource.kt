package com.pubnub.contract.subscribe.eventEngine.state

import com.pubnub.internal.eventengine.EffectInvocation
import com.pubnub.internal.eventengine.Event
import com.pubnub.internal.eventengine.QueueSinkSource
import com.pubnub.internal.eventengine.SinkSource
import com.pubnub.internal.eventengine.Source

internal class TestSinkSource<T>(
    private val testSink: MutableList<Pair<String, String>>,
    private val sinkSource: QueueSinkSource<T> = QueueSinkSource(),
) : SinkSource<T>, Source<T> by sinkSource {

    private fun String.toSnakeCase(): String {
        val pattern = "(?<=.)[A-Z]".toRegex()
        return this.replace(pattern, "_$0").lowercase()
    }

    override fun add(item: T) {
        testSink.add(item.type() to item.name())
        sinkSource.add(item)
    }

    private fun T.name() = this!!::class.simpleName!!.toSnakeCase().uppercase()

    private fun T.type(): String =
        when (this) {
            is Event -> "event"
            is EffectInvocation -> "invocation"
            else -> "unknown"
        }
}
