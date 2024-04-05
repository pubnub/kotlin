package com.pubnub.internal.endpoints.presence;


import com.pubnub.api.endpoints.presence.HereNow;
import com.pubnub.api.models.consumer.presence.PNHereNowResult;
import com.pubnub.internal.EndpointInterface;
import com.pubnub.internal.PubNubCore;
import com.pubnub.internal.endpoints.IdentityMappingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Setter
@Accessors(chain = true, fluent = true)
public class HereNowImpl extends IdentityMappingEndpoint<PNHereNowResult> implements HereNow {
    private List<String> channels = new ArrayList<>();
    private List<String> channelGroups = new ArrayList<>();
    private boolean includeState = false;
    private boolean includeUUIDs = true;

    public HereNowImpl(PubNubCore pubnub) {
        super(pubnub);
    }

    @Override
    @NotNull
    protected EndpointInterface<PNHereNowResult> createAction() {
        return pubnub.hereNow(
                channels,
                channelGroups,
                includeState,
                includeUUIDs
        );
    }
}
