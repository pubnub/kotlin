package com.pubnub.api;

import com.google.gson.JsonElement;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import retrofit2.Call;

@Builder
@Getter
@ToString
public class PubNubException extends Exception {
    private String errormsg;
    private PubNubError pubnubError;
    private JsonElement jso;
    private String response;
    private int statusCode;

    @Getter(AccessLevel.NONE)
    @ToString.Exclude
    private Call affectedCall;
}
