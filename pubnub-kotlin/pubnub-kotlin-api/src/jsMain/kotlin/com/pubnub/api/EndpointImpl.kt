package com.pubnub.api

import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import kotlin.js.Promise

open class EndpointImpl<T,U>(private val promiseFactory: () -> Promise<T>, private val responseMapping: (T) -> U):
    Endpoint<U> {

    override fun async(callback: Consumer<Result<U>>) {
        promiseFactory().then(
            onFulfilled = { response: T ->
                callback.accept(Result.success(responseMapping(response)))
            },
            onRejected = { throwable ->
                callback.accept(Result.failure(throwable))
            }
        )
    }
}