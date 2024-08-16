package com.pubnub.internal.java.endpoints.message_actions;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.java.endpoints.message_actions.AddMessageAction;
import com.pubnub.api.models.consumer.message_actions.PNAddMessageActionResult;
import com.pubnub.api.models.consumer.message_actions.PNMessageAction;
import com.pubnub.internal.java.endpoints.IdentityMappingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import static com.pubnub.api.java.builder.PubNubErrorBuilder.PNERROBJ_CHANNEL_MISSING;
import static com.pubnub.api.java.builder.PubNubErrorBuilder.PNERROBJ_MESSAGE_ACTION_MISSING;
import static com.pubnub.api.java.builder.PubNubErrorBuilder.PNERROBJ_MESSAGE_ACTION_VALUE_MISSING;
import static com.pubnub.api.java.builder.PubNubErrorBuilder.PNERROBJ_MESSAGE_TIMETOKEN_MISSING;

@Setter
@Accessors(chain = true, fluent = true)
public class AddMessageActionImpl extends IdentityMappingEndpoint<PNAddMessageActionResult> implements AddMessageAction {

    private String channel;
    private PNMessageAction messageAction;

    public AddMessageActionImpl(PubNub pubnub) {
        super(pubnub);
    }

    @Override
    @NotNull
    protected Endpoint<PNAddMessageActionResult> createAction() {
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
