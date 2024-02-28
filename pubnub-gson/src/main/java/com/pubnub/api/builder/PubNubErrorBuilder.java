package com.pubnub.api.builder;

import com.pubnub.api.PubNubError;


public final class PubNubErrorBuilder {

    private PubNubErrorBuilder() {
    }

    // Error Codes

    /**
     *
     */
    public static final int PNERR_PUBNUB_ERROR = 101;

    /**
     * Connect Exception . Network Unreachable.
     */
    public static final int PNERR_CONNECT_EXCEPTION = 102;

    /**
     * Please check network connectivity.
     */
    public static final int PNERR_HTTP_ERROR = 103;

    /**
     * Secret key not configured
     */
    public static final int PNERR_SECRET_KEY_MISSING = 114;

    // internal error codes

    /**
     * JSON Error while processing API response. Please contact support with
     * error details.
     */
    public static final int PNERR_JSON_ERROR = 121;

    /**
     * PubNub server returned HTTP 502 internal server error status code. Please
     * contact support with error details.
     */
    public static final int PNERR_INTERNAL_ERROR = 125;

    /**
     * Parsing Error .
     */
    public static final int PNERR_PARSING_ERROR = 126;

    /**
     * Subscribe Timeout .
     */
    public static final int PNERR_HTTP_SOCKET_TIMEOUT = 130;

    /**
     * Invalid arguments provided to API
     */
    public static final int PNERR_INVALID_ARGUMENTS = 131;

    /**
     * Channel missing
     */
    public static final int PNERR_CHANNEL_MISSING = 132;

    /**
     * PubNub connection not set on sender
     */
    public static final int PNERR_CONNECTION_NOT_SET = 133;


    /**
     * Crypto Error
     */
    public static final int PNERR_CRYPTO_ERROR = 135;

    /**
     * Group missing
     */
    public static final int PNERR_GROUP_MISSING = 136;

    /**
     * Subscribe Key missing
     */
    public static final int PNERR_SUBSCRIBE_KEY_MISSING = 138;

    /**
     * Publish Key missing
     */
    public static final int PNERR_PUBLISH_KEY_MISSING = 139;

    /**
     * State missing
     */
    public static final int PNERR_STATE_MISSING = 140;

    /**
     * Channel and Group missing
     */
    public static final int PNERR_CHANNEL_AND_GROUP_MISSING = 141;

    /**
     * Message missing
     */
    public static final int PNERR_MESSAGE_MISSING = 142;

    /**
     * Push TYpe missing
     */
    public static final int PNERR_PUSH_TYPE_MISSING = 143;

    /**
     * Device ID missing
     */
    public static final int PNERR_DEVICE_ID_MISSING = 144;

    /**
     * Timetoken missing
     */
    public static final int PNERR_TIMETOKEN_MISSING = 145;

    /**
     * Timetoken missing
     */
    public static final int PNERR_CHANNELS_TIMETOKEN_MISMATCH = 146;

    /**
     * UUID missing
     */
    public static final int PNERR_UUID_MISSING = 147;

    /**
     * Resources missing
     */
    public static final int PNERR_RESOURCES_MISSING = 153;

    /**
     * TTL missing
     */
    public static final int PNERR_TTL_MISSING = 154;

    /**
     * Invalid access token
     */
    public static final int PNERR_INVALID_ACCESS_TOKEN = 157;

    /**
     * Message action missing
     */
    public static final int PNERR_MESSAGE_ACTION_MISSING = 158;

    /**
     * Message action type missing
     */
    public static final int PNERR_MESSAGE_ACTION_TYPE_MISSING = 159;

    /**
     * Message action value missing
     */
    public static final int PNERR_MESSAGE_ACTION_VALUE_MISSING = 160;

    /**
     * Message timetoken missing
     */
    public static final int PNERR_MESSAGE_TIMETOKEN_MISSING = 161;

    /**
     * Message action timetoken missing
     */
    public static final int PNERR_MESSAGE_ACTION_TIMETOKEN_MISSING = 162;

    /**
     * Retrieving message actions for multiple channels
     */
    public static final int PNERR_HISTORY_MESSAGE_ACTIONS_MULTIPLE_CHANNELS = 163;

    /**
     * Push topic missing
     */
    public static final int PNERR_PUSH_TOPIC_MISSING = 164;

    /**
     * No more pages to load after last one
     */
    public static final int PNERR_PAGINATION_NEXT_OUT_OF_BOUNDS = 165;

    /**
     * No pages to load before first one
     */
    public static final int PNERR_PAGINATION_PREV_OUT_OF_BOUNDS = 166;

    /**
     * Payload too large
     */
    public static final int PNERR_PAYLOAD_TOO_LARGE = 167;

    /**
     * Token missing
     */
    public static final int PNERR_TOKEN_MISSING = 168;

    /**
     * UUID can't be null nor empty
     */
    public static final int PNERR_UUID_NULL_OR_EMPTY = 169;

    /**
     * UserId can't be null nor empty
     */
    public static final int PNERR_USERID_NULL_OR_EMPTY = 170;

    /**
     * SpaceId can't be null nor empty
     */
    public static final int PNERR_SPACEID_NULL_OR_EMPTY = 171;

    /**
     * State must be a JSON object.
     */
    public static final int PNERR_STATE_MUST_BE_JSON_OBJECT = 172;

    /**
     * UserId can't be different from UserId in configuration when flag withHeartbeat is set to true.
     */
    public static final int PNERR_USERID_CAN_NOT_BE_DIFFERENT = 173;

    /**
     * Used when crypto is configured but the message was not encrypted.
     */
    public static final int PNERR_CRYPTO_IS_CONFIGURED_BUT_MESSAGE_IS_NOT_ENCRYPTED = 174;

    // Error Objects

    public static final PubNubError PNERROBJ_INTERNAL_ERROR = PubNubError.INTERNAL_ERROR;


    public static final PubNubError PNERROBJ_JSON_ERROR = PubNubError.JSON_ERROR;


    public static final PubNubError PNERROBJ_CONNECT_EXCEPTION = PubNubError.CONNECT_EXCEPTION;


    public static final PubNubError PNERROBJ_HTTP_ERROR = PubNubError.HTTP_ERROR;


    public static final PubNubError PNERROBJ_PARSING_ERROR = PubNubError.PARSING_ERROR;

    public static final PubNubError PNERROBJ_SECRET_KEY_MISSING = PubNubError.SECRET_KEY_MISSING;

    public static final PubNubError PNERROBJ_SUBSCRIBE_KEY_MISSING = PubNubError.SUBSCRIBE_KEY_MISSING;

    public static final PubNubError PNERROBJ_PUBLISH_KEY_MISSING = PubNubError.PUBLISH_KEY_MISSING;

    public static final PubNubError PNERROBJ_SOCKET_TIMEOUT = PubNubError.TIMEOUT;

    public static final PubNubError PNERROBJ_INVALID_ARGUMENTS = PubNubError.INVALID_ARGUMENTS;

    public static final PubNubError PNERROBJ_CHANNEL_MISSING = PubNubError.CHANNEL_MISSING;

    public static final PubNubError PNERROBJ_STATE_MISSING = PubNubError.STATE_MISSING;

    public static final PubNubError PNERROBJ_MESSAGE_MISSING = PubNubError.MESSAGE_MISSING;

    public static final PubNubError PNERROBJ_PUSH_TYPE_MISSING = PubNubError.PUSH_TYPE_MISSING;

    public static final PubNubError PNERROBJ_DEVICE_ID_MISSING = PubNubError.DEVICE_ID_MISSING;

    public static final PubNubError PNERROBJ_GROUP_MISSING = PubNubError.GROUP_MISSING;

    public static final PubNubError PNERROBJ_CHANNEL_AND_GROUP_MISSING = PubNubError.CHANNEL_AND_GROUP_MISSING;

    public static final PubNubError PNERROBJ_CRYPTO_ERROR = PubNubError.CRYPTO_ERROR;

    public static final PubNubError PNERROBJ_TIMETOKEN_MISSING = PubNubError.TIMETOKEN_MISSING;

    public static final PubNubError PNERROBJ_CHANNELS_TIMETOKEN_MISMATCH = PubNubError.CHANNELS_TIMETOKEN_MISMATCH;

    public static final PubNubError PNERROBJ_UUID_MISSING = PubNubError.UUID_NULL_OR_EMPTY;

    public static final PubNubError PNERROBJ_RESOURCES_MISSING = PubNubError.RESOURCES_MISSING;

    public static final PubNubError PNERROBJ_INVALID_ACCESS_TOKEN = PubNubError.INVALID_ACCESS_TOKEN;

    public static final PubNubError PNERROBJ_MESSAGE_ACTION_MISSING = PubNubError.MESSAGE_ACTION_MISSING;

    public static final PubNubError PNERROBJ_MESSAGE_ACTION_TYPE_MISSING = PubNubError.MESSAGE_ACTION_TYPE_MISSING;

    public static final PubNubError PNERROBJ_MESSAGE_ACTION_VALUE_MISSING = PubNubError.MESSAGE_ACTION_VALUE_MISSING;

    public static final PubNubError PNERROBJ_MESSAGE_TIMETOKEN_MISSING = PubNubError.MESSAGE_TIMETOKEN_MISSING;

    public static final PubNubError PNERROBJ_MESSAGE_ACTION_TIMETOKEN_MISSING = PubNubError.MESSAGE_ACTION_TIMETOKEN_MISSING;

    public static final PubNubError PNERROBJ_HISTORY_MESSAGE_ACTIONS_MULTIPLE_CHANNELS = PubNubError.HISTORY_MESSAGE_ACTIONS_MULTIPLE_CHANNELS;

    public static final PubNubError PNERROBJ_PUSH_TOPIC_MISSING = PubNubError.PUSH_TOPIC_MISSING;

    public static final PubNubError PNERROBJ_TOKEN_MISSING = PubNubError.TOKEN_MISSING;

    public static final PubNubError PNERROBJ_UUID_NULL_OR_EMPTY = PubNubError.UUID_NULL_OR_EMPTY;

    public static final PubNubError PNERROBJ_USERID_NULL_OR_EMPTY = PubNubError.USERID_NULL_OR_EMPTY;

    public static final PubNubError PNERROBJ_PNERR_CRYPTO_IS_CONFIGURED_BUT_MESSAGE_IS_NOT_ENCRYPTED = PubNubError.CRYPTO_IS_CONFIGURED_BUT_MESSAGE_IS_NOT_ENCRYPTED;
}
