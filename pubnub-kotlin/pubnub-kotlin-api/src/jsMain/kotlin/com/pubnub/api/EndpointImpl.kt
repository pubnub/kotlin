package com.pubnub.api

import kotlin.js.Promise

open class EndpointImpl<T,U>(private val promiseFactory: () -> Promise<T>, private val responseMapping: (T) -> U):
    Endpoint<U> {
    override fun asyncInternal(action: (Result<U>) -> Unit) {
        promiseFactory().then(
            onFulfilled = { response: T ->
                action(Result.success(responseMapping(response)))
            },
            onRejected = { throwable ->
                action(Result.failure(throwable))
            }
        )
    }
}