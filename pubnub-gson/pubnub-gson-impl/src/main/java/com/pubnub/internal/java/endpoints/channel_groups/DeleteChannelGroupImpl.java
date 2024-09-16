package com.pubnub.internal.java.endpoints.channel_groups;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.java.builder.PubNubErrorBuilder;
import com.pubnub.api.java.endpoints.channel_groups.DeleteChannelGroup;
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsDeleteGroupResult;
import com.pubnub.internal.java.endpoints.PassthroughEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Accessors(chain = true, fluent = true)
public class DeleteChannelGroupImpl extends PassthroughEndpoint<PNChannelGroupsDeleteGroupResult> implements DeleteChannelGroup {
    private String channelGroup;

    public DeleteChannelGroupImpl(PubNub pubnub) {
        super(pubnub);
    }

    @Override
    protected Endpoint<PNChannelGroupsDeleteGroupResult> createRemoteAction() {
        return pubnub.deleteChannelGroup(channelGroup);
    }

    @Override
    protected void validateParams() throws PubNubException {
        if (channelGroup == null) {
            throw new PubNubException(PubNubErrorBuilder.PNERROBJ_GROUP_MISSING);
        }
    }
}
