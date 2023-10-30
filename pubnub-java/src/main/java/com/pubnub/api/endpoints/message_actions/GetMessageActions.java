package com.pubnub.api.endpoints.message_actions;

import com.pubnub.api.PubNubError;
import com.pubnub.api.PubNubException;
import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.models.consumer.message_actions.PNGetMessageActionsResult;
import com.pubnub.internal.BasePubNub.PubNubImpl;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Accessors(chain = true, fluent = true)
public class GetMessageActions extends Endpoint<PNGetMessageActionsResult> {

    private String channel;
    private Long start;
    private Long end;
    private Integer limit;

    public GetMessageActions(PubNubImpl pubnub) {
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
