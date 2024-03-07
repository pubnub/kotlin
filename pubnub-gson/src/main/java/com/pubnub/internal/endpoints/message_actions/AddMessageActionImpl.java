package com.pubnub.internal.endpoints.message_actions;

import com.pubnub.api.PubNubException;
import com.pubnub.api.endpoints.message_actions.AddMessageAction;
import com.pubnub.api.models.consumer.message_actions.PNAddMessageActionResult;
import com.pubnub.api.models.consumer.message_actions.PNMessageAction;
import com.pubnub.internal.PubNubCore;
import com.pubnub.internal.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;

import static com.pubnub.api.builder.PubNubErrorBuilder.PNERROBJ_CHANNEL_MISSING;
import static com.pubnub.api.builder.PubNubErrorBuilder.PNERROBJ_MESSAGE_ACTION_MISSING;
import static com.pubnub.api.builder.PubNubErrorBuilder.PNERROBJ_MESSAGE_ACTION_VALUE_MISSING;
import static com.pubnub.api.builder.PubNubErrorBuilder.PNERROBJ_MESSAGE_TIMETOKEN_MISSING;

@Setter
@Accessors(chain = true, fluent = true)
public class AddMessageActionImpl extends DelegatingEndpoint<PNAddMessageActionResult> implements AddMessageAction {

    private String channel;
    private PNMessageAction messageAction;

    public AddMessageActionImpl(PubNubCore pubnub) {
        super(pubnub);
    }

    @Override
    protected com.pubnub.internal.EndpointCore<?, PNAddMessageActionResult> createAction() {
        return pubnub.addMessageAction(channel, messageAction);
    }

    @Override
    protected void validateParams() throws PubNubException {
        if (channel == null) {
            throw new PubNubException(PNERROBJ_CHANNEL_MISSING);
        }
        if (messageAction == null) {
            throw new PubNubException(PNERROBJ_MESSAGE_ACTION_MISSING);
        }
        if (messageAction.getMessageTimetoken() == null || messageAction.getMessageTimetoken() == 0) {
            throw new PubNubException(PNERROBJ_MESSAGE_TIMETOKEN_MISSING);
        }
        if (messageAction.getValue().isEmpty()) {
            throw new PubNubException(PNERROBJ_MESSAGE_ACTION_VALUE_MISSING);
        }
    }
}
