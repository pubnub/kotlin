package com.pubnub.api

//actual fun <T> Endpoint<T>.async(action: (Result<T>) -> Unit) {
//    async { result: com.pubnub.api.v2.callbacks.Result<T> ->
//        result.onSuccess { value ->
//            action(Result.success(value))
//        }.onFailure { exception ->
//            action(Result.failure(exception))
//        }
//    }
//}