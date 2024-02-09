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
    val errorMessage: String? = null,
    val pubnubError: PubNubError? = null,
    val jso: String? = null,
    val statusCode: Int = 0,
    val affectedCall: Call<*>? = null,
    val retryAfterHeaderValue: Int? = null,
    override val cause: Throwable? = null,
) : Exception(errorMessage, cause) {

    internal constructor(pubnubError: PubNubError) : this(
        errorMessage = pubnubError.message,
        pubnubError = pubnubError
    )

    companion object {
        fun from(e: Throwable) : PubNubException = if (e is PubNubException) {
            e
        } else {
            PubNubException(e.message, cause = e)
        }
    }

//    override fun toString(): String {
//        return "PubNubException(errorMessage=$errorMessage, pubnubError=$pubnubError, jso=$jso, statusCode=$statusCode, affectedCall=$affectedCall, retryAfterHeaderValue=$retryAfterHeaderValue)"
//    }
//
//    fun copy(
//        errorMessage: String? = this.errorMessage,
//        pubnubError: PubNubError? = this.pubnubError,
//        jso: String? = this.jso,
//        statusCode: Int = this.statusCode,
//        affectedCall: Call<*>? = this.affectedCall,
//        retryAfterHeaderValue: Int? = this.retryAfterHeaderValue,
//        cause: Throwable? = this.cause,
//    ) = PubNubException(
//        errorMessage,
//        pubnubError,
//        jso,
//        statusCode,
//        affectedCall,
//        retryAfterHeaderValue,
//        cause
//    )
}

internal data class PubNubRetryableException(
    val errorMessage: String? = null,
    val pubnubError: PubNubError? = null,
    val statusCode: Int = 0,
) : Exception(errorMessage)
