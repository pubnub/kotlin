package com.pubnub.api.v2.callbacks

actual fun interface Consumer<T> {
    actual fun accept(p: T)
}
