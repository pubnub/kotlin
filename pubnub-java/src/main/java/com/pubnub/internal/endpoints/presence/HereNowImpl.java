package com.pubnub.internal.endpoints.presence;


import com.pubnub.api.endpoints.presence.HereNow;
import com.pubnub.api.models.consumer.presence.PNHereNowResult;
import com.pubnub.internal.InternalPubNubClient;
import com.pubnub.internal.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Setter
@Accessors(chain = true, fluent = true)
public class HereNowImpl extends DelegatingEndpoint<PNHereNowResult> implements HereNow {
    private List<String> channels = new ArrayList<>();
    private List<String> channelGroups = new ArrayList<>();
    private boolean includeState = false;
    private boolean includeUUIDs = true;

    public HereNowImpl(InternalPubNubClient pubnub) {
        super(pubnub);
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
