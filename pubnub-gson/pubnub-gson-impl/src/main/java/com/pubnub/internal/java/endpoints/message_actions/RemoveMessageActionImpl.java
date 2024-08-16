package com.pubnub.internal.java.endpoints.message_actions;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubError;
import com.pubnub.api.PubNubException;
import com.pubnub.api.java.endpoints.message_actions.RemoveMessageAction;
import com.pubnub.api.models.consumer.message_actions.PNRemoveMessageActionResult;
import com.pubnub.internal.java.endpoints.IdentityMappingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Accessors(chain = true, fluent = true)
public class RemoveMessageActionImpl extends IdentityMappingEndpoint<PNRemoveMessageActionResult> implements RemoveMessageAction {

    private String channel;
    private Long messageTimetoken;
    private Long actionTimetoken;

    public RemoveMessageActionImpl(PubNub pubnub) {
        super(pubnub);
    }

    @Override
    protected Endpoint<PNRemoveMessageActionResult> createAction() {
        return pubnub.removeMessageAction(channel, messageTimetoken, actionTimetoken);
    }

    @Override
    protected void validateParams() throws PubNubException {
        if (channel == null) {
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
