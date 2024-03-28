package com.pubnub.internal.eventengine

internal interface Source<T> {
    fun take(): T
}
