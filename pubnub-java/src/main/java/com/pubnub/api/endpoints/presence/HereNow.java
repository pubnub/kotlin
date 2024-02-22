package com.pubnub.api.endpoints.presence;


import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.models.consumer.presence.PNHereNowResult;
import com.pubnub.internal.InternalPubNubClient;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Accessors(chain = true, fluent = true)
public class HereNow extends Endpoint<PNHereNowResult> {
    @Setter
    private List<String> channels;
    @Setter
    private List<String> channelGroups;
    @Setter
    private boolean includeState = false;
    @Setter
    private boolean includeUUIDs = true;

    public HereNow(InternalPubNubClient pubnub) {
        super(pubnub);
        channels = new ArrayList<>();
        channelGroups = new ArrayList<>();
    }

    @Override
    protected com.pubnub.internal.Endpoint<?, PNHereNowResult> createAction() {
        return pubnub.hereNow(
                channels,
                channelGroups,
                includeState,
                includeUUIDs
        );
    }
}
