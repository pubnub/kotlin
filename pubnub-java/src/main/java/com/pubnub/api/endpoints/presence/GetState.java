package com.pubnub.api.endpoints.presence;

import com.pubnub.internal.endpoints.DelegatingEndpoint;
import com.pubnub.api.models.consumer.presence.PNGetStateResult;
import com.pubnub.internal.InternalPubNubClient;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Accessors(chain = true, fluent = true)
public class GetState extends DelegatingEndpoint<PNGetStateResult> {

    @Setter
    private List<String> channels;
    @Setter
    private List<String> channelGroups;
    @Setter
    private String uuid;

    public GetState(InternalPubNubClient pubnub) {
        super(pubnub);
        channels = new ArrayList<>();
        channelGroups = new ArrayList<>();
        uuid = pubnub.getConfiguration().getUuid();
    }

    @Override
    protected com.pubnub.internal.Endpoint<?, PNGetStateResult> createAction() {
        return pubnub.getPresenceState(
                channels,
                channelGroups,
                uuid
        );
    }
}
