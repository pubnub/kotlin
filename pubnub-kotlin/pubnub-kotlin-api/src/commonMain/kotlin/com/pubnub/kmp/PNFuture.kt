package com.pubnub.kmp

import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result

fun interface PNFuture<out OUTPUT> {
    fun async(callback: Consumer<Result<@UnsafeVariance OUTPUT>>)
}
