package com.pubnub.api.endpoints.message_actions;

import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.models.consumer.message_actions.PNGetMessageActionsResult;

public interface GetMessageActions extends Endpoint<PNGetMessageActionsResult> {
    GetMessageActions channel(String channel);

    GetMessageActions start(Long start);

    GetMessageActions end(Long end);

    GetMessageActions limit(Integer limit);
}
