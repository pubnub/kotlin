package com.pubnub.api

actual interface Endpoint<OUTPUT> {
    fun asyncInternal(action: (Result<OUTPUT>) -> Unit)
}

actual fun <T> Endpoint<T>.async(action: (Result<T>) -> Unit) = asyncInternal(action)