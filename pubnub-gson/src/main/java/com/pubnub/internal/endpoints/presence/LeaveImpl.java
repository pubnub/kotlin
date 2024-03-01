package com.pubnub.internal.endpoints.presence;

import com.pubnub.internal.PubNubCore;
import com.pubnub.internal.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Setter
@Accessors(chain = true, fluent = true)
public class LeaveImpl extends DelegatingEndpoint<Boolean> implements com.pubnub.api.endpoints.presence.Leave {
    private List<String> channels = new ArrayList<>();
    private List<String> channelGroups = new ArrayList<>();

    public LeaveImpl(PubNubCore pubnub) {
        super(pubnub);
    }

    @Override
    protected com.pubnub.internal.EndpointCore<?, Boolean> createAction() {
        LeaveEndpoint leave = new LeaveEndpoint(pubnub);
        leave.setChannels(channels);
        leave.setChannelGroups(channelGroups);
        return leave;
    }
}
