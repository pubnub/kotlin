package com.pubnub.kmp

import com.pubnub.api.PubNubException
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import platform.Foundation.NSError

//actual interface Endpoint<OUTPUT> {
//    actual fun async(callback: Consumer<Result<OUTPUT>>)
//}

fun <T,U> Consumer<Result<U>>.onSuccessHandler(mapper: (T) -> U) : (T) -> Unit {
    return { input: T ->
        accept(
            try {
                Result.success(mapper(input))
            } catch (e: Exception) {
                Result.failure(e)
            }
        )
    }
}

fun <T, X, Y, U> Consumer<Result<U>>.onSuccessHandler3(mapper: (T, X, Y) -> U) : (T, X, Y) -> Unit {
    return { input: T, secondInput: X, thirdInput: Y ->
        accept(
            try {
                Result.success(mapper(input, secondInput, thirdInput))
            } catch (e: Exception) {
                Result.failure(e)
            }
        )
    }
}

fun <T> Consumer<Result<T>>.onSuccessReturnValue(value: T) : () -> Unit {
    return {
        accept(Result.success(value))
    }
}

fun <T> Consumer<Result<T>>.onFailureHandler(mapper: (NSError?) -> Throwable = { error: NSError? ->
    PubNubException(errorMessage = error?.localizedDescription)
} ): (NSError?) -> Unit {
    return { error: NSError? ->
        accept(Result.failure(mapper(error)))
    }
}
