package com.pubnub.api

import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import okhttp3.Request
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
actual data class PubNubException(
    val errorMessage: String? = null,
    val pubnubError: PubNubError? = null,
    val jso: String? = null,
    actual val statusCode: Int = 0,
    val affectedCall: Call<*>? = null,
    val retryAfterHeaderValue: Int? = null,
    val affectedChannels: List<String> = emptyList(),
    val affectedChannelGroups: List<String> = emptyList(),
    override val cause: Throwable? = null,
    val requestInfo: RequestInfo? = null,
    val remoteAction: ExtendedRemoteAction<*>? = null,
) : Exception(errorMessage, cause) {
    data class RequestInfo(
        val tlsEnabled: Boolean,
        val origin: String,
        val uuid: String?,
        val authKey: String?,
        val clientRequest: Request,
    )

    @JvmOverloads
    actual constructor(errorMessage: String?, cause: Throwable?) : this(errorMessage, pubnubError = null, cause = cause)

    @JvmOverloads
    actual constructor(pubnubError: PubNubError, cause: Throwable?) : this(
        errorMessage = pubnubError.message,
        pubnubError = pubnubError,
        cause = cause
    )

    constructor(pubnubError: PubNubError, message: String) : this(
        errorMessage = message,
        pubnubError = pubnubError,
    )

    // test only
    actual constructor(errorMessage: String?, statusCode: Int, cause: Throwable?) : this(
        statusCode = statusCode,
        errorMessage = errorMessage,
        cause = cause,
        pubnubError = null
    )

    actual companion object {
        actual fun from(e: Throwable): PubNubException =
            if (e is PubNubException) {
                e
            } else {
                PubNubException(e.message, cause = e)
            }
    }
}
