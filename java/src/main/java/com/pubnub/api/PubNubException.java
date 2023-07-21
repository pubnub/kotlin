package com.pubnub.api;

import com.google.gson.JsonElement;
import com.pubnub.core.CoreException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import retrofit2.Call;

@Getter
@ToString
public class PubNubException extends CoreException {
    private final String errormsg;
    private final PubNubError pubnubError;
    private final JsonElement jso;
    private final String response;
    private final int statusCode;

    @Builder
    public PubNubException(final String errormsg,
                           final PubNubError pubnubError,
                           final JsonElement jso,
                           final String response,
                           final int statusCode,
                           final Call affectedCall,
                           final Throwable cause) {
        super(cause, errormsg);
        this.errormsg = errormsg;
        this.pubnubError = pubnubError;
        this.jso = jso;
        this.response = response;
        this.statusCode = statusCode;
        this.affectedCall = affectedCall;
    }

    @Override
    @ToString.Include
    public Throwable getCause() {
        return super.getCause();
    }

    @Override
    public String getMessage() {
        return errormsg;
    }

    @Getter(AccessLevel.NONE)
    @ToString.Exclude
    private final Call affectedCall;
}

