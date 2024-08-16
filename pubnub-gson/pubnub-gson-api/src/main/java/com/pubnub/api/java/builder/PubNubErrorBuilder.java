package com.pubnub.api.java.builder;

import com.pubnub.api.PubNubError;


public final class PubNubErrorBuilder {

    private PubNubErrorBuilder() {
    }

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

    public static final PubNubError PNERROBJ_TTL_MISSING = PubNubError.TTL_MISSING;

    public static final PubNubError PNERROBJ_STATE_MUST_BE_JSON_OBJECT = PubNubError.STATE_MUST_BE_JSON_OBJECT;

    public static final PubNubError PNERROBJ_USERID_CAN_NOT_BE_DIFFERENT_FROM_IN_CONFIGURATION_WHEN_WITHHEARTBEAT_TRUE = PubNubError.USERID_CAN_NOT_BE_DIFFERENT_FROM_IN_CONFIGURATION_WHEN_WITHHEARTBEAT_TRUE;
}
