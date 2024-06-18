package com.pubnub.api

import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.kmp.PNFuture
import kotlin.js.Promise

open class EndpointImpl<T,U>(private val promiseFactory: () -> Promise<T>, private val responseMapping: (T) -> U):
    PNFuture<U> {

    override fun async(callback: Consumer<Result<U>>) {
        promiseFactory().then(
            onFulfilled = { response: T ->
                callback.accept(Result.success(responseMapping(response)))
            },
            onRejected = { throwable ->
                println(JSON.stringify(throwable))
                callback.accept(Result.failure(throwable))
            }
        )
    }
}