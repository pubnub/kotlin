package com.pubnub.api;


import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.managers.RetrofitManager;
import com.pubnub.api.managers.TelemetryManager;
import com.pubnub.api.managers.token_manager.TokenManager;
import lombok.extern.java.Log;

@Log
public abstract class Endpoint<Input, Output> extends MoreAbstractEndpoint<Input, Output, PNOperationType> {
    public Endpoint(PubNub pubnubInstance,
                    TelemetryManager telemetry,
                    RetrofitManager retrofitInstance,
                    TokenManager tokenManager) {
        super(pubnubInstance, telemetry, retrofitInstance, tokenManager);
    }

    public Endpoint(PubNub pubnub) {
        super(pubnub);
    }

    @Override
    protected PNOperationType operationType() {
        return getOperationType();
    }
}
