package com.pubnub.api

import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result

actual interface Endpoint<OUTPUT> {
    actual fun async(callback: Consumer<Result<OUTPUT>>)
}

//actual fun <T> Endpoint<T>.async(action: (Result<T>) -> Unit) = asyncInternal(action)