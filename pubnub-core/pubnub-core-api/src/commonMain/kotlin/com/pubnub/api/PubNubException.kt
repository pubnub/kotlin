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
expect class PubNubException(pubnubError: PubNubError) : Exception {
    constructor(errorMessage: String?)
}