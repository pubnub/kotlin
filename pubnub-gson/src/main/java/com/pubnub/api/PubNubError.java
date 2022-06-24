package com.pubnub.api;


import com.google.gson.JsonElement;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * PubNubError object is passed to errorCallback. It contains details of error,
 * like error code, error string, and optional message
 *
 * @author PubNub
 */
@Getter
@Builder
@ToString
public class PubNubError {

    private int errorCode;
    private int errorCodeExtended;
    private JsonElement errorObject;
    /**
     * includes a message from the thrown exception (if any.)
     */
    private String message;
    /**
     * PubNub supplied explanation of the error.
     */
    private String errorString;

}

