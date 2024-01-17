package com.pubnub.api.endpoints.message_actions;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNubException;
import com.pubnub.api.endpoints.ValidatingEndpoint;
import com.pubnub.api.endpoints.remoteaction.IdentityMappingEndpoint;
import com.pubnub.api.models.consumer.message_actions.PNAddMessageActionResult;
import com.pubnub.api.models.consumer.message_actions.PNMessageAction;
import lombok.Setter;
import lombok.experimental.Accessors;

import static com.pubnub.api.builder.PubNubErrorBuilder.PNERROBJ_CHANNEL_MISSING;
import static com.pubnub.api.builder.PubNubErrorBuilder.PNERROBJ_MESSAGE_ACTION_MISSING;
import static com.pubnub.api.builder.PubNubErrorBuilder.PNERROBJ_MESSAGE_ACTION_TYPE_MISSING;
import static com.pubnub.api.builder.PubNubErrorBuilder.PNERROBJ_MESSAGE_ACTION_VALUE_MISSING;
import static com.pubnub.api.builder.PubNubErrorBuilder.PNERROBJ_MESSAGE_TIMETOKEN_MISSING;

@Setter
@Accessors(chain = true, fluent = true)
public class AddMessageAction extends ValidatingEndpoint<PNAddMessageActionResult> {

    private String channel;
    private PNMessageAction messageAction;

    public AddMessageAction(com.pubnub.internal.PubNub pubnub) {
        super(pubnub);
    }

    @Override
    protected Endpoint<PNAddMessageActionResult> createAction() {
        return new IdentityMappingEndpoint<>(pubnub.addMessageAction(channel, messageAction));
    }

    @Override
    protected void validateParams() throws PubNubException {
        if (channel == null || channel.isEmpty()) {
            throw new PubNubException(PNERROBJ_CHANNEL_MISSING);
        }
        if (messageAction == null) {
            throw new PubNubException(PNERROBJ_MESSAGE_ACTION_MISSING);
        }
        if (messageAction.getMessageTimetoken() == 0) {
            throw new PubNubException(PNERROBJ_MESSAGE_TIMETOKEN_MISSING);
        }
        if (messageAction.getType().isEmpty()) {
            throw new PubNubException(PNERROBJ_MESSAGE_ACTION_TYPE_MISSING);
        }
        if (messageAction.getValue() == null || messageAction.getValue().isEmpty()) {
            throw new PubNubException(PNERROBJ_MESSAGE_ACTION_VALUE_MISSING);
        }
    }
}
