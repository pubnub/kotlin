package com.pubnub.internal

import com.pubnub.api.PubNubError

internal data class PubNubRetryableException(
    val errorMessage: String? = null,
    val pubnubError: PubNubError? = null,
    val statusCode: Int = 0,
) : Exception(errorMessage)
