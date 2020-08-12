package com.pubnub.api

import retrofit2.Call

/**
 * Custom exception wrapper for errors occurred during execution or processing of a PubNub API operation.
 *
 * @property errorMessage The error message received from the server, if any.
 * @property pubnubError The appropriate matching PubNub error.
 * @property jso The error json received from the server, if any.
 * @property statusCode HTTP status code.
 * @property affectedCall A reference to the affected call. Useful for calling [retry][Endpoint.retry].
 */
data class PubNubException(
    var errorMessage: String? = null,
    var pubnubError: PubNubError? = null,
    var jso: String? = null,
    var statusCode: Int = 0,
    var affectedCall: Call<*>? = null
) : Exception(errorMessage) {

    internal constructor(pubnubError: PubNubError) : this(
        errorMessage = pubnubError.message,
        pubnubError = pubnubError
    )
}
