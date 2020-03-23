package com.pubnub.api

enum class PubNubError(val code: Int, val message: String) {

    TIMEOUT(
        100,
        "Timeout Occurred"
    ),

    JSON_ERROR(
        121,
        "JSON Error while processing API response"
    ),

    PARSING_ERROR(
        126,
        "Parsing Error"
    ),

    CONNECTION_NOT_SET(
        133,
        "PubNub Connection not set"
    ),

    SUBSCRIBE_KEY_MISSING(
        138,
        "Subscribe Key not configured"
    ),

    PUBLISH_KEY_MISSING(
        139,
        "Publish Key not configured"
    ),

    CONNECT_EXCEPTION(
        102,
        "Connect Exception. Please verify if network is reachable"
    ),

    SUBSCRIBE_TIMEOUT(
        130,
        "Subscribe Timeout"
    ),

    HTTP_ERROR(
        103,
        "HTTP Error. Please check network connectivity. Please contact support with error details if the issue persists."
    ),

    MESSAGE_MISSING(
        142,
        "Message Missing"
    ),

    CHANNEL_MISSING(
        132,
        "Channel Missing"
    ),

    CRYPTO_ERROR(
        135,
        "Error while encrypting/decrypting message. Please contact support with error details."
    ),

    STATE_MISSING(
        140,
        "State Missing."
    ),

    CHANNEL_AND_GROUP_MISSING(
        141,
        "Channel and Group Missing."
    );

    override fun toString(): String {
        return "PubNubError(name=$name, code=$code, message='$message')"
    }


}