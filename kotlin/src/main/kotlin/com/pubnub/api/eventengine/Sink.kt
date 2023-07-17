package com.pubnub.api.eventengine

interface Sink<T> {
    fun add(item: T)
}
