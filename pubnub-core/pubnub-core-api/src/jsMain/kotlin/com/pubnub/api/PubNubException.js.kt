package com.pubnub.api

/**
 * Custom exception wrapper for errors occurred during execution or processing of a PubNub API operation.
 *
 * @property errorMessage The error message received from the server, if any.
 * @property pubnubError The appropriate matching PubNub error.
 * @property jso The error json received from the server, if any.
 * @property statusCode HTTP status code.
 * @property affectedCall A reference to the affected call. Useful for calling [retry][Endpoint.retry].
 */
actual class PubNubException(
    actual val statusCode: Int = 0,
    errorMessage: String?,
    cause: Throwable?
) : Exception(errorMessage, cause) {

    actual constructor(errorMessage: String?, cause: Throwable?): this(statusCode = 0, errorMessage, cause)
    actual constructor(pubnubError: PubNubError, cause: Throwable?) : this(statusCode = 0, pubnubError.message, cause)

    actual companion object {
        actual fun from(e: Throwable): PubNubException {
            return if (e is PubNubException) {
                e
            } else {
                val statusCode = (e.asDynamic().status?.statusCode as? Int) ?: 0
                PubNubException(statusCode, e.message, e)
            }
        }
    }
}