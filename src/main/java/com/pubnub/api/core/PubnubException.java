package com.pubnub.api.core;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;

@Builder
public class PubnubException extends Exception {
    private String errormsg = "";
    private PubnubError pubnubError = PubnubError.PNERROBJ_PUBNUB_ERROR;
    private JsonNode jso;
    private String response;
    private int statusCode;
}