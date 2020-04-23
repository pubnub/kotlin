package com.pubnub.api

enum class PubNubError(val code: Int, val message: String) {

    TIMEOUT(
        100,
        "Timeout Occurred"
    ),

    CONNECT_EXCEPTION(
        102,
        "Connect Exception. Please verify if network is reachable"
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

    GROUP_MISSING(
        136,
        "Group Missing"
    ),

    SUBSCRIBE_KEY_MISSING(
        138,
        "Subscribe Key not configured"
    ),

    PUBLISH_KEY_MISSING(
        139,
        "Publish Key not configured"
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
    ),

    PUSH_TYPE_MISSING(
        143,
        "Push Type Missing."
    ),

    DEVICE_ID_MISSING(
        144,
        "Device ID Missing"
    ),

    TIMETOKEN_MISSING(
        145,
        "Timetoken Missing."
    ),

    CHANNELS_TIMETOKEN_MISMATCH(
        146,
        "Channels and timetokens are not equal in size."
    ),

    USER_MISSING(
        147,
        "User is missing"
    ),

    USER_ID_MISSING(
        148,
        "User ID is missing"
    ),

    USER_NAME_MISSING(
        149,
        "User name is missing"
    ),

    MESSAGE_ACTION_MISSING(
        158,
        "Message action is missing."
    ),

    MESSAGE_ACTION_TYPE_MISSING(
        159,
        "Message action type is missing."
    ),

    MESSAGE_ACTION_VALUE_MISSING(
        160,
        "Message action value is missing."
    ),

    MESSAGE_TIMETOKEN_MISSING(
        161,
        "Message timetoken is missing."
    ),

    MESSAGE_ACTION_TIMETOKEN_MISSING(
        162,
        "Message action timetoken is missing."
    ),

    PUSH_TOPIC_MISSING(
        164,
        "Push notification topic is missing. Required only if push type is APNS2."
    ),

    UNINITIALIZED_PROPERTY_ACCESS(
        998,
        "Uninitialized Property Access"
    ),

    X(
        999,
        "X ERROR"
    );

    override fun toString(): String {
        return "PubNubError(name=$name, code=$code, message='$message')"
    }


}