package com.pubnub.internal.endpoints.presence;

import com.pubnub.api.endpoints.presence.Leave;
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
public class LeaveImpl extends IdentityMappingEndpoint<Boolean> implements Leave {
    private List<String> channels = new ArrayList<>();
    private List<String> channelGroups = new ArrayList<>();

    public LeaveImpl(PubNubCore pubnub) {
        super(pubnub);
    }

    @Override
    @NotNull
    protected EndpointInterface<Boolean> createAction() {
        LeaveEndpoint leave = new LeaveEndpoint(pubnub);
        leave.setChannels(channels);
        leave.setChannelGroups(channelGroups);
        return leave;
    }
}
