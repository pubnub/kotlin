package com.pubnub.kmp

import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result

fun interface PNFuture<OUTPUT> {
    fun async(callback: Consumer<Result<OUTPUT>>)
}