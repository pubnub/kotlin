package com.pubnub.api.endpoints;

import com.pubnub.api.models.consumer.history.PNDeleteMessagesResult;

public interface DeleteMessages extends Endpoint<PNDeleteMessagesResult>{
    DeleteMessages channels(java.util.List<String> channels);

    DeleteMessages start(Long start);

    DeleteMessages end(Long end);
}
