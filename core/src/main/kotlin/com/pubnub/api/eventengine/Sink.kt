package com.pubnub.api.eventengine

internal interface Sink<T> {
    fun add(item: T)
}
