package com.pubnub.api

import kotlin.js.Promise

actual interface Endpoint<OUTPUT> {
    actual fun async(action: (Result<OUTPUT>) -> Unit)
}

internal fun <T, U> Endpoint(promiseFactory: () -> Promise<T>, responseMapping: (T) -> U): Endpoint<U> =
    object : Endpoint<U> {
        override fun async(action: (Result<U>) -> Unit) {
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
