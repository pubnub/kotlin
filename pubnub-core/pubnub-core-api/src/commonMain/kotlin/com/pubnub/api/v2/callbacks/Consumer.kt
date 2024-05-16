package com.pubnub.api.v2.callbacks

expect fun interface Consumer<T> {
    fun accept(p: T)
}