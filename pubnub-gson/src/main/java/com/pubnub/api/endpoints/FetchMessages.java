package com.pubnub.api.endpoints;

import com.pubnub.api.models.consumer.history.PNFetchMessagesResult;

public interface FetchMessages extends Endpoint<PNFetchMessagesResult> {
    FetchMessages channels(java.util.List<String> channels);

    FetchMessages maximumPerChannel(Integer maximumPerChannel);

    FetchMessages start(Long start);

    FetchMessages end(Long end);

    FetchMessages includeMeta(boolean includeMeta);

    FetchMessages includeMessageActions(boolean includeMessageActions);

    FetchMessages includeMessageType(boolean includeMessageType);

    FetchMessages includeUUID(boolean includeUUID);
}
