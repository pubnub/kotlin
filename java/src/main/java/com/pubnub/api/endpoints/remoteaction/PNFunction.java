package com.pubnub.api.endpoints.remoteaction;

public interface PNFunction<INPUT, OUTPUT> {
    OUTPUT invoke(INPUT input);
}

