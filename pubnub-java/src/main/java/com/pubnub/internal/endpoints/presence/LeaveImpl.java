package com.pubnub.internal.endpoints.presence;

import com.pubnub.internal.InternalPubNubClient;
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

    public LeaveImpl(InternalPubNubClient pubnub) {
        super(pubnub);
    }

    @Override
    protected com.pubnub.internal.Endpoint<?, Boolean> createAction() {
        Leave leave = new Leave(pubnub);
        leave.setChannels(channels);
        leave.setChannelGroups(channelGroups);
        return leave;
    }
}
