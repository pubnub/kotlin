package com.pubnub.internal.endpoints.presence;

import com.pubnub.api.endpoints.presence.GetState;
import com.pubnub.api.models.consumer.presence.PNGetStateResult;
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
public class GetStateImpl extends IdentityMappingEndpoint<PNGetStateResult> implements GetState {

    private List<String> channels = new ArrayList<>();
    private List<String> channelGroups = new ArrayList<>();
    private String uuid;

    public GetStateImpl(PubNubCore pubnub) {
        super(pubnub);
        uuid = pubnub.getConfiguration().getUuid();
    }

    @Override
    @NotNull
    protected EndpointInterface<PNGetStateResult> createAction() {
        return pubnub.getPresenceState(
                channels,
                channelGroups,
                uuid
        );
    }
}
