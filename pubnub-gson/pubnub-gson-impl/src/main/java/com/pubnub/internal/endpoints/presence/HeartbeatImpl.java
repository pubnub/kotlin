package com.pubnub.internal.endpoints.presence;

import com.pubnub.api.endpoints.presence.Heartbeat;
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
public class HeartbeatImpl extends IdentityMappingEndpoint<Boolean> implements Heartbeat {

    private List<String> channels;
    private List<String> channelGroups;
    private Object state;

    public HeartbeatImpl(PubNubCore pubnub) {
        super(pubnub);
        channels = new ArrayList<>();
        channelGroups = new ArrayList<>();
    }

    @Override
    @NotNull
    protected EndpointInterface<Boolean> createAction() {
        return new HeartbeatEndpoint(pubnub, channels, channelGroups, state);
    }
}
