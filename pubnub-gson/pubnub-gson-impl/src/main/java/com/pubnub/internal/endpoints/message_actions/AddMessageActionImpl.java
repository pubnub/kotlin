package com.pubnub.internal.endpoints.message_actions;

import com.pubnub.api.PubNubException;
import com.pubnub.api.endpoints.message_actions.AddMessageAction;
import com.pubnub.api.models.consumer.message_actions.PNAddMessageActionResult;
import com.pubnub.api.models.consumer.message_actions.PNMessageAction;
import com.pubnub.internal.EndpointInterface;
import com.pubnub.internal.PubNubCore;
import com.pubnub.internal.endpoints.IdentityMappingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import static com.pubnub.api.builder.PubNubErrorBuilder.PNERROBJ_CHANNEL_MISSING;
import static com.pubnub.api.builder.PubNubErrorBuilder.PNERROBJ_MESSAGE_ACTION_MISSING;
import static com.pubnub.api.builder.PubNubErrorBuilder.PNERROBJ_MESSAGE_ACTION_VALUE_MISSING;
import static com.pubnub.api.builder.PubNubErrorBuilder.PNERROBJ_MESSAGE_TIMETOKEN_MISSING;

@Setter
@Accessors(chain = true, fluent = true)
public class AddMessageActionImpl extends IdentityMappingEndpoint<PNAddMessageActionResult> implements AddMessageAction {

    private String channel;
    private PNMessageAction messageAction;

    public AddMessageActionImpl(PubNubCore pubnub) {
        super(pubnub);
    }

    @Override
    @NotNull
    protected EndpointInterface<PNAddMessageActionResult> createAction() {
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
        if (messageAction.getMessageTimetoken() == 0) {
            throw new PubNubException(PNERROBJ_MESSAGE_TIMETOKEN_MISSING);
        }
        if (messageAction.getValue().isEmpty()) {
            throw new PubNubException(PNERROBJ_MESSAGE_ACTION_VALUE_MISSING);
        }
    }
}
