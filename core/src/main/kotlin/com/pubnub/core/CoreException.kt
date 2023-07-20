package com.pubnub.core

import java.util.ServiceLoader

abstract class CoreException(errorMessage: String? = null) : Exception(errorMessage)

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



