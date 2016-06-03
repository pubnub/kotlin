package com.pubnub.api;


import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Getter;

/**
 * PubNubError object is passed to errorCallback. It contains details of error,
 * like error code, error string, and optional message
 *
 * @author PubNub
 *
 */
@Getter
@Builder
public class PubNubError {

    private final int errorCode;
    private final int errorCodeExtended;
    private final JsonNode errorObject;
    /**
     * includes a message from the thrown exception (if any.)
     */
    private final String message;
    /**
     * PubNub supplied explanation of the error.
     */
    private final String errorString;

}

