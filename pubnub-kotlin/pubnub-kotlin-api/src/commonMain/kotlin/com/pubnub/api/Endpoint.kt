package com.pubnub.api

import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result

expect interface Endpoint<OUTPUT> {
    fun async(callback: Consumer<Result<OUTPUT>>)
}