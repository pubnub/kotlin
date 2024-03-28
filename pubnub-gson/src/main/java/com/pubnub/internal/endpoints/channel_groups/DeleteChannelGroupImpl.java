package com.pubnub.internal.endpoints.channel_groups;

import com.pubnub.api.PubNubException;
import com.pubnub.api.builder.PubNubErrorBuilder;
import com.pubnub.api.endpoints.channel_groups.DeleteChannelGroup;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsDeleteGroupResult;
import com.pubnub.internal.PubNubCore;
import com.pubnub.internal.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Accessors(chain = true, fluent = true)
public class DeleteChannelGroupImpl extends DelegatingEndpoint<PNChannelGroupsDeleteGroupResult> implements DeleteChannelGroup {
    private String channelGroup;

    public DeleteChannelGroupImpl(PubNubCore pubnub) {
        super(pubnub);
    }

    @Override
    protected ExtendedRemoteAction<PNChannelGroupsDeleteGroupResult> createAction() {
        return pubnub.deleteChannelGroup(channelGroup);
    }

    @Override
    protected void validateParams() throws PubNubException {
        if (channelGroup == null) {
            throw new PubNubException(PubNubErrorBuilder.PNERROBJ_GROUP_MISSING);
        }
    }
}
