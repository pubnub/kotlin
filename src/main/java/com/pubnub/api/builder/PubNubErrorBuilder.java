package com.pubnub.api.builder;

import com.pubnub.api.PubNubError;


public final class PubNubErrorBuilder {

    // Error Codes
    /**
     * Timeout Error .
     */
    public static final int PNERR_TIMEOUT = 100;

    /**
     *
     */
    public static final int PNERR_PUBNUB_ERROR = 101;

    /**
     * Connect Exception . Network Unreachable.
     */
    public static final int PNERR_CONNECT_EXCEPTION = 102;

    /**
     * Please check network connectivity. Please contact support with error
     * details if issue persists.
     */
    public static final int PNERR_HTTP_ERROR = 103;

    /**
     * Client Timeout .
     */
    public static final int PNERR_CLIENT_TIMEOUT = 104;

    /**
     * An ULS singature error occurred . Please contact support with error
     * details.
     */
    public static final int PNERR_ULSSIGN_ERROR = 105;

    /**
     * Please verify if network is reachable
     */
    public static final int PNERR_NETWORK_ERROR = 106;

    /**
     * PubNub Exception .
     */
    public static final int PNERR_PUBNUB_EXCEPTION = 108;

    /**
     * Disconnect .
     */
    public static final int PNERR_DISCONNECT = 109;

    /**
     * Disconnect and Resubscribe Received .
     */
    public static final int PNERR_DISCONN_AND_RESUB = 110;

    /**
     * Gateway Timeout
     */
    public static final int PNERR_GATEWAY_TIMEOUT = 111;

    /**
     * PubNub server returned HTTP 403 forbidden status code. Happens when wrong
     * authentication key is used .
     */
    public static final int PNERR_FORBIDDEN = 112;
    /**
     * PubNub server returned HTTP 401 unauthorized status code Happens when
     * authentication key is missing .
     */
    public static final int PNERR_UNAUTHORIZED = 113;

    /**
     * Secret key not configured
     */
    public static final int PNERR_SECRET_KEY_MISSING = 114;

    // internal error codes

    /**
     * Error while encrypting message to be published to PubNub Cloud . Please
     * contact support with error details.
     */
    public static final int PNERR_ENCRYPTION_ERROR = 115;

    /**
     * Decryption Error . Please contact support with error details.
     */
    public static final int PNERR_DECRYPTION_ERROR = 116;

    /**
     * Invalid Json . Please contact support with error details.
     */
    public static final int PNERR_INVALID_JSON = 117;

    /**
     * Unable to open input stream . Please contact support with error details.
     */
    public static final int PNERR_GETINPUTSTREAM = 118;

    /**
     * Malformed URL . Please contact support with error details .
     */
    public static final int PNERR_MALFORMED_URL = 119;

    /**
     * Error in opening URL . Please contact support with error details.
     */
    public static final int PNERR_URL_OPEN = 120;

    /**
     * JSON Error while processing API response. Please contact support with
     * error details.
     */
    public static final int PNERR_JSON_ERROR = 121;

    /**
     * Protocol Exception . Please contact support with error details.
     */
    public static final int PNERR_PROTOCOL_EXCEPTION = 122;

    /**
     * Unable to read input stream . Please contact support with error details.
     */
    public static final int PNERR_READINPUT = 123;

    /**
     * Bad gateway . Please contact support with error details.
     */
    public static final int PNERR_BAD_GATEWAY = 124;

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
     * Bad Request . Please contact support with error details.
     */
    public static final int PNERR_BAD_REQUEST = 127;

    public static final int PNERR_HTTP_RC_ERROR = 128;
    /**
     * PubNub server or intermediate server returned HTTP 404 unauthorized
     * status code
     *
     */
    public static final int PNERR_NOT_FOUND = 129;

    /**
     * Subscribe Timeout .
     */
    public static final int PNERR_HTTP_SUBSCRIBE_TIMEOUT = 130;

    /**
     * Invalid arguments provided to API
     *
     */
    public static final int PNERR_INVALID_ARGUMENTS = 131;

    /**
     * Channel missing
     *
     */
    public static final int PNERR_CHANNEL_MISSING = 132;

    /**
     * PubNub connection not set on sender
     *
     */
    public static final int PNERR_CONNECTION_NOT_SET = 133;

    /**
     * Error while parsing group name
     */
    public static final int PNERR_CHANNEL_GROUP_PARSING_ERROR = 134;

    /**
     * Crypto Error
     */
    public static final int PNERR_CRYPTO_ERROR = 135;

    /**
     * Group missing
     *
     */
    public static final int PNERR_GROUP_MISSING = 136;

    /**
     * Auth Keys missing
     *
     */
    public static final int PNERR_AUTH_KEYS_MISSING = 137;

    /**
     * Subscribe Key missing
     *
     */
    public static final int PNERR_SUBSCRIBE_KEY_MISSING = 138;

    /**
     * Publish Key missing
     *
     */
    public static final int PNERR_PUBLISH_KEY_MISSING = 139;

    /**
     * State missing
     *
     */
    public static final int PNERR_STATE_MISSING = 140;

    /**
     * Channel and Group missing
     *
     */
    public static final int PNERR_CHANNEL_AND_GROUP_MISSING = 141;

    /**
     * Message missing
     *
     */
    public static final int PNERR_MESSAGE_MISSING = 142;

    /**
     * Push TYpe missing
     *
     */
    public static final int PNERR_PUSH_TYPE_MISSING = 143;

    /**
     * Device ID missing
     *
     */
    public static final int PNERR_DEVICE_ID_MISSING = 144;

    // Error Objects
    public static final PubNubError PNERROBJ_TIMEOUT = PubNubError.builder()
            .errorCode(PNERR_TIMEOUT)
            .message("Timeout Occurred")
            .build();

    public static final PubNubError PNERROBJ_INTERNAL_ERROR =  PubNubError.builder()
            .errorCode(PNERR_INTERNAL_ERROR)
            .message("Internal Error")
            .build();

    public static final PubNubError PNERROBJ_ENCRYPTION_ERROR =  PubNubError.builder()
            .errorCode(PNERR_ENCRYPTION_ERROR)
            .message("Error while encrypting message to be published to PubNub Cloud. Please contact support with error details.")
            .build();

    public static final PubNubError PNERROBJ_DECRYPTION_ERROR =  PubNubError.builder()
            .errorCode(PNERR_DECRYPTION_ERROR)
            .message("Decryption Error. Please contact support with error details.")
            .build();

    public static final PubNubError PNERROBJ_INVALID_JSON =  PubNubError.builder()
            .errorCode(PNERR_INVALID_JSON)
            .message("Invalid Json. Please contact support with error details.")
            .build();

    public static final PubNubError PNERROBJ_JSON_ERROR =  PubNubError.builder()
            .errorCode(PNERR_JSON_ERROR)
            .message("JSON Error while processing API response. Please contact support with error details.")
            .build();

    public static final PubNubError PNERROBJ_MALFORMED_URL =  PubNubError.builder()
            .errorCode(PNERR_MALFORMED_URL)
            .message("Malformed URL. Please contact support with error details.")
            .build();

    public static final PubNubError PNERROBJ_PUBNUB_ERROR =  PubNubError.builder()
            .errorCode(PNERR_PUBNUB_ERROR)
            .message("PubNub Error")
            .build();

    public static final PubNubError PNERROBJ_URL_OPEN =  PubNubError.builder()
            .errorCode(PNERR_URL_OPEN)
            .message("Error opening url. Please contact support with error details.")
            .build();

    public static final PubNubError PNERROBJ_PROTOCOL_EXCEPTION =  PubNubError.builder()
            .errorCode(PNERR_PROTOCOL_EXCEPTION)
            .message("Protocol Exception. Please contact support with error details.")
            .build();

    public static final PubNubError PNERROBJ_CONNECT_EXCEPTION =  PubNubError.builder()
            .errorCode(PNERR_CONNECT_EXCEPTION)
            .message("Connect Exception. Please verify if network is reachable.")
            .build();

    public static final PubNubError PNERROBJ_HTTP_RC_ERROR =  PubNubError.builder()
            .errorCode(PNERR_HTTP_RC_ERROR)
            .message("Unable to get PnResponse Code. Please contact support with error details.")
            .build();

    public static final PubNubError PNERROBJ_GETINPUTSTREAM =  PubNubError.builder()
            .errorCode(PNERR_GETINPUTSTREAM)
            .message("Unable to get Input Stream Please contact support with error details.")
            .build();

    public static final PubNubError PNERROBJ_READINPUT =  PubNubError.builder()
            .errorCode(PNERR_READINPUT)
            .message("Unable to read Input Stream. Please contact support with error details.")
            .build();

    public static final PubNubError PNERROBJ_BAD_REQUEST =  PubNubError.builder()
            .errorCode(PNERR_BAD_REQUEST)
            .message("Bad request. Please contact support with error details.")
            .build();

    public static final PubNubError PNERROBJ_HTTP_ERROR =  PubNubError.builder()
            .errorCode(PNERR_HTTP_ERROR)
            .message("HTTP Error. Please check network connectivity. Please contact support with error details if issue persists.")
            .build();

    public static final PubNubError PNERROBJ_BAD_GATEWAY = PubNubError.builder()
            .errorCode(PNERR_BAD_GATEWAY)
            .message("Bad Gateway. Please contact support with error details.")
            .build();

    public static final PubNubError PNERROBJ_CLIENT_TIMEOUT = PubNubError.builder()
            .errorCode(PNERR_CLIENT_TIMEOUT)
            .message("Client Timeout")
            .build();

    public static final PubNubError PNERROBJ_GATEWAY_TIMEOUT = PubNubError.builder()
            .errorCode(PNERR_GATEWAY_TIMEOUT)
            .message("Gateway Timeout")
            .build();

    public static final PubNubError PNERROBJ_5023_INTERNAL_ERROR = PubNubError.builder()
            .errorCode(PNERR_INTERNAL_ERROR)
            .message("Internal Server Error. Please contact support with error details.")
            .build();

    public static final PubNubError PNERROBJ_PARSING_ERROR = PubNubError.builder()
            .errorCode(PNERR_PARSING_ERROR)
            .message("Parsing Error")
            .build();

    public static final PubNubError PNERROBJ_PUBNUB_EXCEPTION =  PubNubError.builder()
            .errorCode(PNERR_PUBNUB_EXCEPTION)
            .message("PubNub Exception")
            .build();

    public static final PubNubError PNERROBJ_DISCONNECT =  PubNubError.builder()
            .errorCode(PNERR_DISCONNECT)
            .message("Disconnect")
            .build();

    public static final PubNubError PNERROBJ_DISCONN_AND_RESUB =  PubNubError.builder()
            .errorCode(PNERR_DISCONN_AND_RESUB)
            .message("Disconnect and Resubscribe")
            .build();

    public static final PubNubError PNERROBJ_FORBIDDEN =  PubNubError.builder()
            .errorCode(PNERR_FORBIDDEN)
            .message("Authentication Failure. Incorrect Authentication Key")
            .build();

    public static final PubNubError PNERROBJ_UNAUTHORIZED =  PubNubError.builder()
            .errorCode(PNERR_UNAUTHORIZED)
            .message("Authentication Failure. Authentication Key is missing")
            .build();

    public static final PubNubError PNERROBJ_SECRET_KEY_MISSING =  PubNubError.builder()
            .errorCode(PNERR_SECRET_KEY_MISSING)
            .message("ULS configuration failed. Secret Key not configured.")
            .build();

    public static final PubNubError PNERROBJ_SUBSCRIBE_KEY_MISSING =  PubNubError.builder()
            .errorCode(PNERR_SUBSCRIBE_KEY_MISSING)
            .message("ULS configuration failed. Subscribe Key not configured.")
            .build();

    public static final PubNubError PNERROBJ_PUBLISH_KEY_MISSING =  PubNubError.builder()
            .errorCode(PNERR_PUBLISH_KEY_MISSING)
            .message("ULS configuration failed. Publish Key not configured.")
            .build();

    public static final PubNubError PNERROBJ_ULSSIGN_ERROR =  PubNubError.builder()
            .errorCode(PNERR_ULSSIGN_ERROR)
            .message("Invalid Signature. Please contact support with error details.")
            .build();

    public static final PubNubError PNERROBJ_5075_NETWORK_ERROR =  PubNubError.builder()
            .errorCode(PNERR_NETWORK_ERROR)
            .message("Network Error. Please verify if network is reachable.")
            .build();

    public static final PubNubError PNERROBJ_NOT_FOUND_ERROR =  PubNubError.builder()
            .errorCode(PNERR_NOT_FOUND)
            .message("Page Not Found Please verify if network is reachable. Please contact support with error details.")
            .build();

    public static final PubNubError PNERROBJ_SUBSCRIBE_TIMEOUT =  PubNubError.builder()
            .errorCode(PNERR_HTTP_SUBSCRIBE_TIMEOUT)
            .message("Subscribe Timeout.")
            .build();

    public static final PubNubError PNERROBJ_INVALID_ARGUMENTS =  PubNubError.builder()
            .errorCode(PNERR_INVALID_ARGUMENTS)
            .message("INVALID ARGUMENTS.")
            .build();

    public static final PubNubError PNERROBJ_CHANNEL_MISSING =  PubNubError.builder()
            .errorCode(PNERR_CHANNEL_MISSING)
            .message("Channel Missing.")
            .build();

    public static final PubNubError PNERROBJ_STATE_MISSING =  PubNubError.builder()
            .errorCode(PNERR_STATE_MISSING)
            .message("State Missing.")
            .build();

    public static final PubNubError PNERROBJ_MESSAGE_MISSING =  PubNubError.builder()
            .errorCode(PNERR_MESSAGE_MISSING)
            .message("Message Missing.")
            .build();

    public static final PubNubError PNERROBJ_PUSH_TYPE_MISSING =  PubNubError.builder()
            .errorCode(PNERR_PUSH_TYPE_MISSING)
            .message("Push Type Missing.")
            .build();

    public static final PubNubError PNERROBJ_DEVICE_ID_MISSING =  PubNubError.builder()
            .errorCode(PNERR_DEVICE_ID_MISSING)
            .message("Device Id Missing.")
            .build();

    public static final PubNubError PNERROBJ_CONNECTION_NOT_SET =  PubNubError.builder()
            .errorCode(PNERR_CONNECTION_NOT_SET)
            .message("PubNub Connection not set")
            .build();

    public static final PubNubError PNERROBJ_GROUP_MISSING =  PubNubError.builder()
            .errorCode(PNERR_GROUP_MISSING)
            .message("Group Missing.")
            .build();

    public static final PubNubError PNERROBJ_CHANNEL_AND_GROUP_MISSING =  PubNubError.builder()
            .errorCode(PNERR_CHANNEL_AND_GROUP_MISSING)
            .message("Channel and Group Missing.")
            .build();

    public static final PubNubError PNERROBJ_AUTH_KEYS_MISSING =  PubNubError.builder()
            .errorCode(PNERR_AUTH_KEYS_MISSING)
            .message("Auth Keys Missing.")
            .build();

    public static final PubNubError PNERROBJ_CHANNEL_GROUP_PARSING_ERROR =  PubNubError.builder()
            .errorCode(PNERR_CHANNEL_GROUP_PARSING_ERROR)
            .message("Channel group name is invalid")
            .build();

    public static final PubNubError PNERROBJ_CRYPTO_ERROR =  PubNubError.builder()
            .errorCode(PNERR_CRYPTO_ERROR)
            .message("Error while encrypting/decrypting message. Please contact support with error details.")
            .build();

    private PubNubErrorBuilder() {

    }

    public static PubNubError createCryptoError(int code, String message) {
        return PubNubError.builder()
                .errorCode(PNERR_CRYPTO_ERROR)
                .errorCodeExtended(code)
                .message("Error while encrypting/decrypting message. Please contact support with error details. - ".concat(message))
                .build();
    }

}
