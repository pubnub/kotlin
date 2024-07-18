package com.pubnub.kmp

import cocoapods.PubNubSwift.PubNubErrorObjC
import com.pubnub.api.PubNubException
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSError

fun <T, U> Consumer<Result<U>>.onSuccessHandler(mapper: (T) -> U): (T) -> Unit {
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

fun <T, X, Y, U> Consumer<Result<U>>.onSuccessHandler3(mapper: (T, X, Y) -> U): (T, X, Y) -> Unit {
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

fun <T, X, Y> Consumer<Result<Y>>.onSuccessHandler2(mapper: (T, X) -> Y): (T, X) -> Unit {
    return { input: T, secondInput: X ->
        accept(
            try {
                Result.success(mapper(input, secondInput))
            } catch (e: Exception) {
                Result.failure(e)
            }
        )
    }
}

fun <T> Consumer<Result<T>>.onSuccessReturnValue(value: T): () -> Unit {
    return {
        accept(Result.success(value))
    }
}

@OptIn(ExperimentalForeignApi::class)
fun <T> Consumer<Result<T>>.onFailureHandler(
    mapper: (NSError?) -> Throwable = { error: NSError? ->
        if (error is PubNubErrorObjC) {
            PubNubException(error.statusCode().toInt(), error.localizedDescription, null)
        } else {
            PubNubException(errorMessage = error?.localizedDescription)
        }
    }
): (NSError?) -> Unit {
    return { error: NSError? ->
        accept(Result.failure(mapper(error)))
    }
}
