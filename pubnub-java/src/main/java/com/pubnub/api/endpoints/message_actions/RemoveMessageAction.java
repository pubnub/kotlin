package com.pubnub.api.endpoints.message_actions;

import com.pubnub.api.PubNubError;
import com.pubnub.api.PubNubException;
import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.models.consumer.message_actions.PNRemoveMessageActionResult;
import com.pubnub.internal.BasePubNub.PubNubImpl;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Accessors(chain = true, fluent = true)
public class RemoveMessageAction extends Endpoint<PNRemoveMessageActionResult> {

    private String channel;
    private Long messageTimetoken;
    private Long actionTimetoken;

    public RemoveMessageAction(PubNubImpl pubnub) {
        super(pubnub);
    }

    @Override
    protected com.pubnub.internal.Endpoint<?, PNRemoveMessageActionResult> createAction() {
        return pubnub.removeMessageAction(channel, messageTimetoken, actionTimetoken);
    }

    @Override
    protected void validateParams() throws PubNubException {
        if (channel == null || channel.isEmpty()) {
            throw new PubNubException(PubNubError.CHANNEL_MISSING);
        }
        if (messageTimetoken == null) {
            throw new PubNubException(PubNubError.MESSAGE_TIMETOKEN_MISSING);
        }
        if (actionTimetoken == null) {
            throw new PubNubException(PubNubError.MESSAGE_ACTION_TIMETOKEN_MISSING);
        }
    }
}
