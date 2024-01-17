package com.pubnub.api.endpoints.message_actions;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNubError;
import com.pubnub.api.PubNubException;
import com.pubnub.api.endpoints.ValidatingEndpoint;
import com.pubnub.api.endpoints.remoteaction.IdentityMappingEndpoint;
import com.pubnub.api.models.consumer.message_actions.PNGetMessageActionsResult;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Accessors(chain = true, fluent = true)
public class GetMessageActions extends ValidatingEndpoint<PNGetMessageActionsResult> {

    private String channel;
    private Long start;
    private Long end;
    private Integer limit;

    public GetMessageActions(com.pubnub.internal.PubNub pubnub) {
        super(pubnub);
    }

    @Override
    protected void validateParams() throws PubNubException {
        if (channel == null || channel.isEmpty()) {
            throw new PubNubException(PubNubError.CHANNEL_MISSING);
        }
    }

    @Override
    protected Endpoint<PNGetMessageActionsResult> createAction() {
        return new IdentityMappingEndpoint<>(pubnub.getMessageActions(channel, start, end, limit));
    }
}
