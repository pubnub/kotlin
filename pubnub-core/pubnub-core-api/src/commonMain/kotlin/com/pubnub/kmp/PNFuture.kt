package com.pubnub.kmp

import com.pubnub.api.PubNubException
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.api.v2.callbacks.mapCatching

interface PNFuture<OUTPUT> {
    fun async(callback: Consumer<Result<OUTPUT>>)
}

fun <T> PubNubException.asFuture(): PNFuture<T> = object : PNFuture<T> {
    override fun async(callback: Consumer<Result<T>>) {
        callback.accept(Result.failure(this@asFuture))
    }
}

fun <T> T.asFuture(): PNFuture<T> = object : PNFuture<T> {
    override fun async(callback: Consumer<Result<T>>) {
        callback.accept(Result.success(this@asFuture))
    }
}

fun <T> Result<T>.asFuture(): PNFuture<T> = object : PNFuture<T> {
    override fun async(callback: Consumer<Result<T>>) {
        callback.accept(this@asFuture)
    }
}

fun <T, U> PNFuture<T>.then(action: (T) -> U): PNFuture<U> = object : PNFuture<U> {
    override fun async(callback: Consumer<Result<U>>) {
        this@then.async { it: Result<T> ->
            callback.accept(it.mapCatching(action))
        }
    }
}

fun <T, U> PNFuture<T>.thenAsync(action: (T) -> PNFuture<U>): PNFuture<U> = object : PNFuture<U> {
    override fun async(callback: Consumer<Result<U>>) {
        this@thenAsync.async { firstFutureResult: Result<T> ->
            val intermediateResult: Result<PNFuture<U>> = firstFutureResult.mapCatching(action)
            intermediateResult.onFailure {
                callback.accept(Result.failure(it))
            }.onSuccess { secondFuture: PNFuture<U> ->
                secondFuture.async { secondFutureResult ->
                    callback.accept(secondFutureResult)
                }
            }
        }
    }
}

fun <T> PNFuture<T>.catch(action: (Exception) -> Result<T>): PNFuture<T> = object : PNFuture<T> {
    override fun async(callback: Consumer<Result<T>>) {
        this@catch.async { result: Result<T> ->
            result.onSuccess {
                callback.accept(result)
            }.onFailure {
                try {
                    callback.accept(action(it))
                } catch (e: Exception) {
                    callback.accept(Result.failure(e))
                }
            }
        }
    }
}