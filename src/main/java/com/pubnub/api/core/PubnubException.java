package com.pubnub.api.core;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;

@Builder
public class PubNubException extends Exception {
    private String errormsg = "";
    private PubNubError pubnubError;
    private JsonNode jso;
    private String response;
    private int statusCode;
}