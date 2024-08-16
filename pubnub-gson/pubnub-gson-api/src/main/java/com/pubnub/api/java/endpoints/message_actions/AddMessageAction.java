package com.pubnub.api.java.endpoints.message_actions;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.models.consumer.message_actions.PNAddMessageActionResult;
import com.pubnub.api.models.consumer.message_actions.PNMessageAction;

public interface AddMessageAction extends Endpoint<PNAddMessageActionResult> {
    AddMessageAction channel(String channel);

    AddMessageAction messageAction(PNMessageAction messageAction);
}
