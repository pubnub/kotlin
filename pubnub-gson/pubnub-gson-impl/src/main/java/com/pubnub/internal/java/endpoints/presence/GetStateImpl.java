package com.pubnub.internal.java.endpoints.presence;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.java.endpoints.presence.GetState;
import com.pubnub.api.models.consumer.presence.PNGetStateResult;
import com.pubnub.internal.java.endpoints.PassthroughEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Setter
@Accessors(chain = true, fluent = true)
public class GetStateImpl extends PassthroughEndpoint<PNGetStateResult> implements GetState {

    private List<String> channels = new ArrayList<>();
    private List<String> channelGroups = new ArrayList<>();
    private String uuid;

    public GetStateImpl(PubNub pubnub) {
        super(pubnub);
        uuid = pubnub.getConfiguration().getUuid();
    }

    @Override
    @NotNull
    protected Endpoint<PNGetStateResult> createRemoteAction() {
        return pubnub.getPresenceState(
                channels,
                channelGroups,
                uuid
        );
    }
}
