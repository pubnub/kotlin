package com.pubnub.api.eventengine

interface Source<T> {
    fun take(): T
}
