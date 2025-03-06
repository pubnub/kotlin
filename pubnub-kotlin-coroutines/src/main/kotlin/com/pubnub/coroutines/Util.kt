package com.pubnub.coroutines

import com.pubnub.kmp.PNFuture
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

suspend fun <T> PNFuture<T>.await(): T {
    val t = suspendCancellableCoroutine { cont ->
        async { result ->
            result.onSuccess {
                cont.resume(it)
            }.onFailure {
                cont.resumeWithException(it)
            }
        }
    }
    return t
}
