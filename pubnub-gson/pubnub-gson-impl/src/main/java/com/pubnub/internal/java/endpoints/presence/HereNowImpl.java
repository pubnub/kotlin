package com.pubnub.internal.java.endpoints.presence;


import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.java.endpoints.presence.HereNow;
import com.pubnub.api.models.consumer.presence.PNHereNowResult;
import com.pubnub.internal.java.endpoints.PassthroughEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Setter
@Accessors(chain = true, fluent = true)
public class HereNowImpl extends PassthroughEndpoint<PNHereNowResult> implements HereNow {
    public static final int MAX_CHANNEL_OCCUPANTS_LIMIT = 1000;
    private List<String> channels = new ArrayList<>();
    private List<String> channelGroups = new ArrayList<>();
    private boolean includeState = false;
    private boolean includeUUIDs = true;
    private int limit = MAX_CHANNEL_OCCUPANTS_LIMIT;
    private Integer offset = null;

    public HereNowImpl(PubNub pubnub) {
        super(pubnub);
    }

    @Override
    @NotNull
    protected Endpoint<PNHereNowResult> createRemoteAction() {
        return pubnub.hereNow(
                channels,
                channelGroups,
                includeState,
                includeUUIDs,
                limit,
                offset
        );
    }
}
