package com.pubnub.core

import java.util.ServiceLoader

interface PubNubException

interface PubNubExceptionFactory {
    fun createPubNubException(message: String, cause: Throwable? = null): PubNubException
}

val pubNubExceptionFactory: PubNubExceptionFactory by lazy { ServiceLoader
        .load(PubNubExceptionFactory::class.java).first()
}

fun PubNubException(string: String): PubNubException {
    return pubNubExceptionFactory.createPubNubException(string)
}


