package com.pubnub.api.endpoints.presence;

import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.models.consumer.presence.PNGetStateResult;
import com.pubnub.internal.BasePubNub.PubNubImpl;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Accessors(chain = true, fluent = true)
public class GetState extends Endpoint<PNGetStateResult> {

    @Setter
    private List<String> channels;
    @Setter
    private List<String> channelGroups;
    @Setter
    private String uuid;

    public GetState(PubNubImpl pubnub) {
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
