package com.pubnub.api.endpoints.objects_api;

import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.builder.PubNubErrorBuilder;
import com.pubnub.api.managers.RetrofitManager;
import com.pubnub.api.managers.TelemetryManager;
import com.pubnub.api.managers.token_manager.TokenManager;

public abstract class ChannelEnpoint<INPUT, OUTPUT> extends ObjectApiEndpoint<INPUT, OUTPUT> {
    protected String channel;

    protected ChannelEnpoint(final String channel,
                             final PubNub pubnubInstance,
                             final TelemetryManager telemetry,
                             final RetrofitManager retrofitInstance,
                             final CompositeParameterEnricher compositeParameterEnricher,
                             final TokenManager tokenManager) {
        super(pubnubInstance, telemetry, retrofitInstance, compositeParameterEnricher, tokenManager);
        this.channel = channel;
    }

    @Override
    protected void validateParams() throws PubNubException {
        super.validateParams();
        if (channel == null || channel.isEmpty()) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_CHANNEL_MISSING).build();
        }
    }
}

