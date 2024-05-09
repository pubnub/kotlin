package com.pubnub.api

actual interface Endpoint<OUTPUT> {
    actual fun async(action: (Result<OUTPUT>) -> Unit)
}