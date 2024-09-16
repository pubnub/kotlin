package com.pubnub.internal.java.endpoints.presence;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.java.endpoints.presence.Leave;
import com.pubnub.internal.PubNubImpl;
import com.pubnub.internal.endpoints.presence.LeaveEndpoint;
import com.pubnub.internal.java.endpoints.PassthroughEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Setter
@Accessors(chain = true, fluent = true)
public class LeaveImpl extends PassthroughEndpoint<Boolean> implements Leave {
    private List<String> channels = new ArrayList<>();
    private List<String> channelGroups = new ArrayList<>();

    public LeaveImpl(PubNub pubnub) {
        super(pubnub);
    }

    @Override
    @NotNull
    protected Endpoint<Boolean> createRemoteAction() {
        LeaveEndpoint leave = new LeaveEndpoint((PubNubImpl) pubnub);
        leave.setChannels(channels);
        leave.setChannelGroups(channelGroups);
        return leave;
    }
}
