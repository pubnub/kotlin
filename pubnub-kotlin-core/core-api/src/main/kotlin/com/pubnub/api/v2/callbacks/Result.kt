/*
 * Copyright 2010-2018 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

@file:JvmName("Results")

// @file:Suppress("UNCHECKED_CAST", "RedundantVisibilityModifier")

package com.pubnub.api.v2.callbacks

import com.pubnub.api.PubNubException
import java.util.function.Consumer
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * A discriminated union that encapsulates a successful outcome with a value of type [T]
 * or a failure with an arbitrary [Throwable] exception.
 */
class Result<out T>
    @PublishedApi
    internal constructor(
        @PublishedApi
        @get:JvmSynthetic
        internal val value: Any?,
    ) {
        // discovery

        /**
         * Returns `true` if this instance represents a successful outcome.
         * In this case [isFailure] returns `false`.
         */
        public val isSuccess: Boolean get() = value !is Failure

        /**
         * Returns `true` if this instance represents a failed outcome.
         * In this case [isSuccess] returns `false`.
         */
        public val isFailure: Boolean get() = value is Failure

        // value & exception retrieval

        /**
         * Returns the encapsulated value if this instance represents [success][Result.isSuccess] or `null`
         * if it is [failure][Result.isFailure].
         *
         * This function is a shorthand for `getOrElse { null }` (see [getOrElse]) or
         * `fold(onSuccess = { it }, onFailure = { null })` (see [fold]).
         */
        public inline fun getOrNull(): T? =
            when {
                isFailure -> null
                else -> value as T
            }

        /**
         * Returns the encapsulated [Throwable] exception if this instance represents [failure][isFailure] or `null`
         * if it is [success][isSuccess].
         *
         * This function is a shorthand for `fold(onSuccess = { null }, onFailure = { it })` (see [fold]).
         */
        public fun exceptionOrNull(): PubNubException? =
            when (value) {
                is Failure -> value.exception as PubNubException
                else -> null
            }

        /**
         * Returns a string `Success(v)` if this instance represents [success][Result.isSuccess]
         * where `v` is a string representation of the value or a string `Failure(x)` if
         * it is [failure][isFailure] where `x` is a string representation of the exception.
         */
        public override fun toString(): String =
            when (value) {
                is Failure -> value.toString() // "Failure($exception)"
                else -> "Success($value)"
            }

        @OptIn(ExperimentalContracts::class)
        public inline fun onFailure(action: Consumer<PubNubException>): Result<T> {
            exceptionOrNull()?.let { action.accept(it) }
            return this
        }

        @Suppress("UNCHECKED_CAST")
        inline fun onSuccess(action: Consumer<in T>): Result<T> {
            if (isSuccess) action.accept(value as T)
            return this
        }

        // companion with constructors

        /**
         * Companion object for [Result] class that contains its constructor functions
         * [success] and [failure].
         */
        public companion object {
            /**
             * Returns an instance that encapsulates the given [value] as successful value.
             */
            @JvmStatic
            public fun <T> success(value: T): Result<T> = Result(value)

            /**
             * Returns an instance that encapsulates the given [Throwable] [exception] as failure.
             */
            @JvmStatic
            public fun <T> failure(exception: PubNubException): Result<T> = Result(createFailure(exception))
        }

        internal class Failure(
            @JvmField
            val exception: PubNubException,
        ) {
            override fun equals(other: Any?): Boolean = other is Failure && exception == other.exception

            override fun hashCode(): Int = exception.hashCode()

            override fun toString(): String = "Failure($exception)"
        }
    }

/**
 * Creates an instance of internal marker [Result.Failure] class to
 * make sure that this class is not exposed in ABI.
 */
@PublishedApi
internal fun createFailure(exception: PubNubException): Any = Result.Failure(exception)

/**
 * Throws exception if the result is failure. This internal function minimizes
 * inlined bytecode for [getOrThrow] and makes sure that in the future we can
 * add some exception-augmenting logic here (if needed).
 */
@PublishedApi
internal fun Result<*>.throwOnFailure() {
    if (value is Result.Failure) throw value.exception
}

/**
 * Calls the specified function [block] and returns its encapsulated result if invocation was successful,
 * catching any [Throwable] exception that was thrown from the [block] function execution and encapsulating it as a failure.
 */
public inline fun <R> runCatching(block: () -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (e: Throwable) {
        Result.failure(PubNubException.from(e))
    }
}

/**
 * Calls the specified function [block] with `this` value as its receiver and returns its encapsulated result if invocation was successful,
 * catching any [Throwable] exception that was thrown from the [block] function execution and encapsulating it as a failure.
 */
public inline fun <T, R> T.runCatching(block: T.() -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (e: Throwable) {
        Result.failure(PubNubException.from(e))
    }
}

// -- extensions ---

/**
 * Returns the encapsulated value if this instance represents [success][Result.isSuccess] or throws the encapsulated [Throwable] exception
 * if it is [failure][Result.isFailure].
 *
 * This function is a shorthand for `getOrElse { throw it }` (see [getOrElse]).
 */
public inline fun <T> Result<T>.getOrThrow(): T {
    throwOnFailure()
    return value as T
}

/**
 * Returns the encapsulated value if this instance represents [success][Result.isSuccess] or the
 * result of [onFailure] function for the encapsulated [Throwable] exception if it is [failure][Result.isFailure].
 *
 * Note, that this function rethrows any [Throwable] exception thrown by [onFailure] function.
 *
 * This function is a shorthand for `fold(onSuccess = { it }, onFailure = onFailure)` (see [fold]).
 */
@OptIn(ExperimentalContracts::class)
public inline fun <R, T : R> Result<T>.getOrElse(onFailure: (exception: PubNubException) -> R): R {
    contract {
        callsInPlace(onFailure, InvocationKind.AT_MOST_ONCE)
    }
    return when (val exception = exceptionOrNull()) {
        null -> value as T
        else -> onFailure(exception)
    }
}

/**
 * Returns the encapsulated value if this instance represents [success][Result.isSuccess] or the
 * [defaultValue] if it is [failure][Result.isFailure].
 *
 * This function is a shorthand for `getOrElse { defaultValue }` (see [getOrElse]).
 */
public inline fun <R, T : R> Result<T>.getOrDefault(defaultValue: R): R {
    if (isFailure) return defaultValue
    return value as T
}

/**
 * Returns the result of [onSuccess] for the encapsulated value if this instance represents [success][Result.isSuccess]
 * or the result of [onFailure] function for the encapsulated [Throwable] exception if it is [failure][Result.isFailure].
 *
 * Note, that this function rethrows any [Throwable] exception thrown by [onSuccess] or by [onFailure] function.
 */
@OptIn(ExperimentalContracts::class)
public inline fun <R, T> Result<T>.fold(
    onSuccess: (value: T) -> R,
    onFailure: (exception: PubNubException) -> R,
): R {
    contract {
        callsInPlace(onSuccess, InvocationKind.AT_MOST_ONCE)
        callsInPlace(onFailure, InvocationKind.AT_MOST_ONCE)
    }
    return when (val exception = exceptionOrNull()) {
        null -> onSuccess(value as T)
        else -> onFailure(exception)
    }
}

// transformation

/**
 * Returns the encapsulated result of the given [transform] function applied to the encapsulated value
 * if this instance represents [success][Result.isSuccess] or the
 * original encapsulated [Throwable] exception if it is [failure][Result.isFailure].
 *
 * Note, that this function rethrows any [Throwable] exception thrown by [transform] function.
 * See [mapCatching] for an alternative that encapsulates exceptions.
 */
@OptIn(ExperimentalContracts::class)
public inline fun <R, T> Result<T>.map(transform: (value: T) -> R): Result<R> {
    contract {
        callsInPlace(transform, InvocationKind.AT_MOST_ONCE)
    }
    return when {
        isSuccess -> Result.success(transform(value as T))
        else -> Result(value)
    }
}

/**
 * Returns the encapsulated result of the given [transform] function applied to the encapsulated value
 * if this instance represents [success][Result.isSuccess] or the
 * original encapsulated [Throwable] exception if it is [failure][Result.isFailure].
 *
 * This function catches any [Throwable] exception thrown by [transform] function and encapsulates it as a failure.
 * See [map] for an alternative that rethrows exceptions from `transform` function.
 */
public inline fun <R, T> Result<T>.mapCatching(transform: (value: T) -> R): Result<R> {
    return when {
        isSuccess -> runCatching { transform(value as T) }
        else -> Result(value)
    }
}
//
// /**
// * Returns the encapsulated result of the given [transform] function applied to the encapsulated [Throwable] exception
// * if this instance represents [failure][Result.isFailure] or the
// * original encapsulated value if it is [success][Result.isSuccess].
// *
// * Note, that this function rethrows any [Throwable] exception thrown by [transform] function.
// * See [recoverCatching] for an alternative that encapsulates exceptions.
// */
// @OptIn(ExperimentalContracts::class)
// public inline fun <R, T : R> Result<T>.recover(transform: (exception: Throwable) -> R): Result<R> {
//    contract {
//        callsInPlace(transform, InvocationKind.AT_MOST_ONCE)
//    }
//    return when (val exception = exceptionOrNull()) {
//        null -> this
//        else -> Result.success(transform(exception))
//    }
// }
//
// /**
// * Returns the encapsulated result of the given [transform] function applied to the encapsulated [Throwable] exception
// * if this instance represents [failure][Result.isFailure] or the
// * original encapsulated value if it is [success][Result.isSuccess].
// *
// * This function catches any [Throwable] exception thrown by [transform] function and encapsulates it as a failure.
// * See [recover] for an alternative that rethrows exceptions.
// */
// public inline fun <R, T : R> Result<T>.recoverCatching(transform: (exception: Throwable) -> R): Result<R> {
//    return when (val exception = exceptionOrNull()) {
//        null -> this
//        else -> runCatching { transform(exception) }
//    }
// }

// "peek" onto value/exception and pipe
//
// /**
// * Performs the given [action] on the encapsulated [Throwable] exception if this instance represents [failure][Result.isFailure].
// * Returns the original `Result` unchanged.
// */
// @OptIn(ExperimentalContracts::class)
// @JvmSynthetic
// public inline fun <T> Result<T>.onFailure(action: (exception: PubNubException) -> Unit): Result<T> {
//    contract {
//        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
//    }
//    exceptionOrNull()?.let { action(it) }
//    return this
// }
//
// /**
// * Performs the given [action] on the encapsulated value if this instance represents [success][Result.isSuccess].
// * Returns the original `Result` unchanged.
// */
// @OptIn(ExperimentalContracts::class)
// @JvmSynthetic
// public inline fun <T> Result<T>.onSuccess(action: (value: T) -> Unit): Result<T> {
//    contract {
//        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
//    }
//    if (isSuccess) action(value as T)
//    return this
// }

// -------------------
