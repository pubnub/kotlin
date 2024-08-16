package com.pubnub.internal.eventengine

internal interface Sink<T> {
    fun add(item: T)
}
