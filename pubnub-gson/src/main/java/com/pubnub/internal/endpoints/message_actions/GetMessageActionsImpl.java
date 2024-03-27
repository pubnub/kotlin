package com.pubnub.internal.endpoints.message_actions;

import com.pubnub.api.PubNubError;
import com.pubnub.api.PubNubException;
import com.pubnub.api.endpoints.message_actions.GetMessageActions;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.models.consumer.PNBoundedPage;
import com.pubnub.api.models.consumer.message_actions.PNGetMessageActionsResult;
import com.pubnub.internal.PubNubCore;
import com.pubnub.internal.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Accessors(chain = true, fluent = true)
public class GetMessageActionsImpl extends DelegatingEndpoint<PNGetMessageActionsResult> implements GetMessageActions {

    private String channel;
    private Long start;
    private Long end;
    private Integer limit;

    public GetMessageActionsImpl(PubNubCore pubnub) {
        super(pubnub);
    }

    @Override
    protected void validateParams() throws PubNubException {
        if (channel == null) {
            throw new PubNubException(PubNubError.CHANNEL_MISSING);
        }
    }

    @Override
    protected ExtendedRemoteAction<PNGetMessageActionsResult> createAction() {
        return pubnub.getMessageActions(channel, new PNBoundedPage(start, end, limit));
    }
}
