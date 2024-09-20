@file:JvmName("PNFutures")

package com.pubnub.kmp

import com.pubnub.api.PubNubException
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.api.v2.callbacks.mapCatching
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.locks.ReentrantLock
import kotlinx.atomicfu.locks.reentrantLock
import kotlinx.atomicfu.locks.withLock
import kotlin.jvm.JvmName

private class CompletablePNFuture<T> : PNFuture<T> {
    private val reentrantLock: ReentrantLock = reentrantLock()
    private lateinit var result: Result<T>
    private var callback: Consumer<Result<T>>? = null

    fun complete(result: Result<T>) {
        reentrantLock.withLock {
            if (!this::result.isInitialized) {
                this.result = result
                callback?.accept(result)
            } else {
                error("This CompletablePNFuture has already completed with result $result")
            }
        }
    }

    override fun async(callback: Consumer<Result<T>>) {
        reentrantLock.withLock {
            if (this.callback != null) {
                error("Only one callback is supported for CompletablePNFuture.")
            }
            if (!this::result.isInitialized) {
                this.callback = callback
            } else {
                callback.accept(result)
            }
        }
    }
}

fun <T> PubNubException.asFuture(): PNFuture<T> = PNFuture { callback ->
    callback.accept(Result.failure(this@asFuture))
}

fun <T> T.asFuture(): PNFuture<T> = PNFuture { callback ->
    callback.accept(Result.success(this@asFuture))
}

fun <T> Result<T>.asFuture(): PNFuture<T> = PNFuture { callback ->
    callback.accept(this@asFuture)
}

fun <T, U> PNFuture<T>.then(action: (T) -> U): PNFuture<U> = PNFuture { callback ->
    this@then.async { it: Result<T> ->
        callback.accept(it.mapCatching(action))
    }
}

fun <T, U> PNFuture<T>.thenAsync(action: (T) -> PNFuture<U>): PNFuture<U> = PNFuture { callback ->
    this@thenAsync.async { firstFutureResult: Result<T> ->
        val intermediateResult: Result<PNFuture<U>> = firstFutureResult.mapCatching(action)
        intermediateResult.onFailure {
            callback.accept(Result.failure(it))
        }.onSuccess { secondFuture: PNFuture<U> ->
            secondFuture.async(callback)
        }
    }
}

fun <T> PNFuture<T>.remember(): PNFuture<T> = CompletablePNFuture<T>().also { completableFuture ->
    this@remember.async {
        completableFuture.complete(it)
    }
}

/**
 * Execute a second PNFuture after this PNFuture completes successfully,
 * and return the *original* value of this PNFuture after the second PNFuture completes successfully.
 *
 * Failures are propagated to the resulting PNFuture.
 */
fun <T> PNFuture<T>.alsoAsync(action: (T) -> PNFuture<*>): PNFuture<T> =
    this@alsoAsync.thenAsync { outerResult: T ->
        action(outerResult).then { _ ->
            outerResult
        }
    }

fun <T> PNFuture<T>.catch(action: (Exception) -> Result<T>): PNFuture<T> = PNFuture<T> { callback ->
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

fun <T> Collection<PNFuture<T>>.awaitAll(): PNFuture<List<T>> = PNFuture { callback ->
    if (isEmpty()) {
        callback.accept(Result.success(emptyList()))
        return@PNFuture
    }
    val counter = atomic(0)
    val failed = atomic(false)
    val array = Array<Any?>(size) { null }
    forEachIndexed { index, future ->
        future.async { res ->
            res.onSuccess { value ->
                array[index] = value
                val counterIncremented = counter.incrementAndGet()
                if (counterIncremented == size) {
                    callback.accept(Result.success(array.toList() as List<T>))
                }
            }.onFailure { exception ->
                val failedWasSetPreviously = failed.getAndSet(true)
                if (!failedWasSetPreviously) {
                    callback.accept(Result.failure(exception))
                }
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
fun <T, U> awaitAll(
    future1: PNFuture<T>,
    future2: PNFuture<U>
): PNFuture<Pair<T, U>> = listOf(future1 as PNFuture<Any?>, future2 as PNFuture<Any?>).awaitAll().then { it: List<Any?> ->
    Pair(it[0] as T, it[1] as U)
}

@Suppress("UNCHECKED_CAST")
fun <T, U, X> awaitAll(
    future1: PNFuture<T>,
    future2: PNFuture<U>,
    future3: PNFuture<X>
): PNFuture<Triple<T, U, X>> = listOf(future1 as PNFuture<Any?>, future2 as PNFuture<Any?>, future3 as PNFuture<Any?>).awaitAll().then {
        it: List<Any?> ->
    Triple(it[0] as T, it[1] as U, it[2] as X)
}
