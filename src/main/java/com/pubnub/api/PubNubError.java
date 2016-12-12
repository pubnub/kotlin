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

    private int errorCode;
    private int errorCodeExtended;
    private JsonNode errorObject;
    /**
     * includes a message from the thrown exception (if any.)
     */
    private String message;
    /**
     * PubNub supplied explanation of the error.
     */
    private String errorString;

}

