package com.pubnub.api

import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import platform.Foundation.NSData
import platform.Foundation.NSError
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.dataUsingEncoding

actual interface Endpoint<OUTPUT> {
    actual fun async(callback: Consumer<Result<OUTPUT>>)
}

fun <T,U> Consumer<Result<U>>.onSuccessHandler(mapper: (T) -> U) : (T) -> Unit {
    return fun(input: T) { accept(Result.success(mapper(input))) }
}

fun <T> Consumer<Result<T>>.onFailureHandler(mapper: (NSError?) -> Throwable = { error: NSError? ->
    PubNubException(errorMessage = error?.localizedDescription)
} ): (NSError?) -> Unit {
    return { error: NSError? ->
        accept(Result.failure(mapper(error)))
    }
}

fun String.toNSData(): NSData? {
    return (this as NSString).dataUsingEncoding(NSUTF8StringEncoding)
}
