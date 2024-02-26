package com.pubnub.api.endpoints.message_actions;

import com.pubnub.api.PubNubError;
import com.pubnub.api.PubNubException;
import com.pubnub.api.models.consumer.message_actions.PNGetMessageActionsResult;
import com.pubnub.internal.InternalPubNubClient;
import com.pubnub.internal.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Accessors(chain = true, fluent = true)
public class GetMessageActions extends DelegatingEndpoint<PNGetMessageActionsResult> {

    private String channel;
    private Long start;
    private Long end;
    private Integer limit;

    public GetMessageActions(InternalPubNubClient pubnub) {
        super(pubnub);
    }

    @Override
    protected void validateParams() throws PubNubException {
        if (channel == null || channel.isEmpty()) {
            throw new PubNubException(PubNubError.CHANNEL_MISSING);
        }
    }

    @Override
    protected com.pubnub.internal.Endpoint<?, PNGetMessageActionsResult> createAction() {
        return pubnub.getMessageActions(channel, start, end, limit);
    }
}
