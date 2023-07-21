package com.pubnub.core

import java.util.ServiceLoader

abstract class CoreException  @JvmOverloads constructor(throwable: Throwable? = null, errorMessage: String? = null) : Exception(errorMessage, throwable) {
    // https://youtrack.jetbrains.com/issue/KT-31420
    @Suppress("INAPPLICABLE_JVM_NAME")
    @get:JvmName("getErrormsg")
    abstract val errorMessage: String?
    abstract val pubnubError: CoreError?
    abstract val statusCode: Int
}

interface PubNubExceptionFactory {
    fun createPubNubException(message: String, cause: Throwable? = null): CoreException

    fun createPubNubException(coreError: CoreError): CoreException
}

val pubNubExceptionFactory: PubNubExceptionFactory by lazy { ServiceLoader
        .load(PubNubExceptionFactory::class.java).first()
}

fun PubNubException(string: String): CoreException {
    return pubNubExceptionFactory.createPubNubException(string)
}

fun PubNubException(coreError: CoreError): CoreException {
    return pubNubExceptionFactory.createPubNubException(coreError)
}



