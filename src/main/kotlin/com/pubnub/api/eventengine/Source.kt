package com.pubnub.api.eventengine

internal interface Source<T> {
    fun take(): T
}
