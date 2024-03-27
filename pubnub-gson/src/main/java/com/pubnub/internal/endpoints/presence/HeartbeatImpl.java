package com.pubnub.internal.endpoints.presence;

import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.internal.PubNubCore;
import com.pubnub.internal.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Accessors(chain = true, fluent = true)
public class HeartbeatImpl extends DelegatingEndpoint<Boolean> implements com.pubnub.api.endpoints.presence.Heartbeat {

    @Setter
    private List<String> channels;
    @Setter
    private List<String> channelGroups;
    @Setter
    private Object state;

    public HeartbeatImpl(PubNubCore pubnub) {
        super(pubnub);
        channels = new ArrayList<>();
        channelGroups = new ArrayList<>();
    }

    @Override
    protected ExtendedRemoteAction<Boolean> createAction() {
        return new HeartbeatEndpoint(pubnub, channels, channelGroups, state);
    }
}
