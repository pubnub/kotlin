package com.pubnub.api.managers.subscription.utils;

import lombok.Getter;
import retrofit2.Response;

public class ResponseHolder<T> {
    @Getter
    private final Response<T> response;

    @Getter
    private final Exception exception;

    public ResponseHolder(final Response<T> response) {
        this.response = response;
        this.exception = null;
    }

    public ResponseHolder(final Exception exception) {
        this.response = null;
        this.exception = exception;
    }
}
