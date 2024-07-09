package com.pubnub.api

import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.kmp.PNFuture
import kotlin.js.Promise

open class EndpointImpl<T, U>(private val promiseFactory: () -> Promise<T>, private val responseMapping: (T) -> U) :
    PNFuture<U> {
    override fun async(callback: Consumer<Result<U>>) {
        try {
            promiseFactory().then(
                onFulfilled = { response: T ->
                    try {
                        callback.accept(Result.success(responseMapping(response)))
                    } catch (e: Exception) {
                        callback.accept(Result.failure(e))
                    }
                },
                onRejected = { throwable ->
                    callback.accept(Result.failure(throwable))
                }
            ).catch {
                callback.accept(Result.failure(it))
            }
        } catch (e: Exception) {
            callback.accept(Result.failure(e))
        }
    }
}
