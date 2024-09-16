package com.pubnub.internal.java.endpoints.presence;

import com.pubnub.api.Endpoint;
import com.pubnub.api.java.endpoints.presence.Heartbeat;
import com.pubnub.internal.PubNubImpl;
import com.pubnub.internal.endpoints.presence.HeartbeatEndpoint;
import com.pubnub.internal.java.endpoints.PassthroughEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Setter
@Accessors(chain = true, fluent = true)
public class HeartbeatImpl extends PassthroughEndpoint<Boolean> implements Heartbeat {

    private List<String> channels;
    private List<String> channelGroups;
    private Object state;

    public HeartbeatImpl(PubNubImpl pubnub) {
        super(pubnub);
        channels = new ArrayList<>();
        channelGroups = new ArrayList<>();
    }

    @Override
    @NotNull
    protected Endpoint<Boolean> createRemoteAction() {
        return new HeartbeatEndpoint((PubNubImpl) pubnub, channels, channelGroups, state);
    }
}
