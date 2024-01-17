package com.pubnub.api.endpoints.presence;

import com.pubnub.api.Endpoint;
import com.pubnub.api.endpoints.ValidatingEndpoint;
import com.pubnub.api.endpoints.remoteaction.IdentityMappingEndpoint;
import com.pubnub.api.models.consumer.presence.PNGetStateResult;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Accessors(chain = true, fluent = true)
public class GetState extends ValidatingEndpoint<PNGetStateResult> {

    @Setter
    private List<String> channels;
    @Setter
    private List<String> channelGroups;
    @Setter
    private String uuid;

    public GetState(com.pubnub.internal.PubNub pubnub) {
        super(pubnub);
        channels = new ArrayList<>();
        channelGroups = new ArrayList<>();
        uuid = pubnub.getConfiguration().getUuid();
    }

    @Override
    protected Endpoint<PNGetStateResult> createAction() {
        return new IdentityMappingEndpoint<>(pubnub.getPresenceState(
                channels,
                channelGroups,
                uuid
        ));
    }
}
