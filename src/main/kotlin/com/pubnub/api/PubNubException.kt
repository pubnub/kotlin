package com.pubnub.api

import retrofit2.Call


data class PubNubException(
    var errorMessage: String? = null,
    var pubnubError: PubNubError? = null,
    var jso: String? = null,
    var statusCode: Int = 0,
    var affectedCall: Call<*>? = null
) : Exception(errorMessage) {

    constructor(pubnubError: PubNubError) : this(
        errorMessage = pubnubError.message,
        pubnubError = pubnubError
    )
}