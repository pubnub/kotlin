package com.pubnub.api.endpoints.objects_api;

import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.builder.PubNubErrorBuilder;
import com.pubnub.api.managers.RetrofitManager;
import com.pubnub.api.managers.TelemetryManager;

public abstract class UUIDEndpoint<SELF extends UUIDEndpoint, INPUT, OUTPUT> extends ObjectApiEndpoint<INPUT, OUTPUT> {
    private String uuid;

    protected UUIDEndpoint(
            final PubNub pubnubInstance,
            final TelemetryManager telemetry,
            final RetrofitManager retrofitInstance,
            final CompositeParameterEnricher compositeParameterEnricher) {
        super(pubnubInstance, telemetry, retrofitInstance, compositeParameterEnricher);
    }

    @Override
    protected void validateParams() throws PubNubException {
        super.validateParams();
        final String effectiveUuid = effectiveUuid();
        if (effectiveUuid == null || effectiveUuid.isEmpty()) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_UUID_MISSING).build();
        }
    }

    public SELF uuid(final String uuid) {
        this.uuid = uuid;
        return (SELF) this;
    }

    protected String effectiveUuid() {
        return (uuid != null) ? uuid : getPubnub().getConfiguration().getUuid();
    }
}

