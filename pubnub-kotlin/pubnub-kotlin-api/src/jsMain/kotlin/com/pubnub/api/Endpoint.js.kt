package com.pubnub.api

import kotlin.js.Promise

actual interface Endpoint<OUTPUT> {
    fun asyncInternal(action: (Result<OUTPUT>) -> Unit)
}

internal fun <T, U> Endpoint(promiseFactory: () -> Promise<T>, responseMapping: (T) -> U): Endpoint<U> =
    object : Endpoint<U> {
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

actual fun <T> Endpoint<T>.async(action: (Result<T>) -> Unit) = asyncInternal(action)