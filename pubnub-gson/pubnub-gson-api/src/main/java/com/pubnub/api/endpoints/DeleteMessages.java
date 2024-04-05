package com.pubnub.api.endpoints;

import com.pubnub.api.models.consumer.history.PNDeleteMessagesResult;

import java.util.List;

public interface DeleteMessages extends Endpoint<PNDeleteMessagesResult> {
    DeleteMessages channels(List<String> channels);

    DeleteMessages start(Long start);

    DeleteMessages end(Long end);
}
