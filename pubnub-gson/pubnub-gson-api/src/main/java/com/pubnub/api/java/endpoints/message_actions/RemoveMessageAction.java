package com.pubnub.api.java.endpoints.message_actions;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.models.consumer.message_actions.PNRemoveMessageActionResult;

public interface RemoveMessageAction extends Endpoint<PNRemoveMessageActionResult> {
    RemoveMessageAction channel(String channel);

    RemoveMessageAction messageTimetoken(Long messageTimetoken);

    RemoveMessageAction actionTimetoken(Long actionTimetoken);
}
