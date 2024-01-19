package com.pubnub.api.endpoints.presence;


import com.pubnub.api.Endpoint;
import com.pubnub.api.endpoints.ValidatingEndpoint;
import com.pubnub.api.endpoints.remoteaction.IdentityMappingEndpoint;
import com.pubnub.api.models.consumer.presence.PNHereNowResult;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Accessors(chain = true, fluent = true)
public class HereNow extends ValidatingEndpoint<PNHereNowResult> {
    @Setter
    private List<String> channels;
    @Setter
    private List<String> channelGroups;
    @Setter
    private boolean includeState = false;
    @Setter
    private boolean includeUUIDs = true;

    public HereNow(com.pubnub.internal.PubNub pubnub) {
        super(pubnub);
        channels = new ArrayList<>();
        channelGroups = new ArrayList<>();
    }

    @Override
    protected Endpoint<PNHereNowResult> createAction() {
        return new IdentityMappingEndpoint<>(pubnub.hereNow(
                channels,
                channelGroups,
                includeState,
                includeUUIDs
        ));
    }
}
