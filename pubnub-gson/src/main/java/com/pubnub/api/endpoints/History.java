package com.pubnub.api.endpoints;

import com.pubnub.api.models.consumer.history.PNHistoryResult;

public interface History extends Endpoint<PNHistoryResult> {
    History channel(String channel);

    History start(Long start);

    History end(Long end);

    History reverse(boolean reverse);

    History count(int count);

    History includeTimetoken(boolean includeTimetoken);

    History includeMeta(boolean includeMeta);
}
