package com.pubnub.api.endpoints;

import com.pubnub.api.models.consumer.history.PNMessageCountResult;

public interface MessageCounts extends Endpoint<PNMessageCountResult> {
    MessageCounts channels(java.util.List<String> channels);

    MessageCounts channelsTimetoken(java.util.List<Long> channelsTimetoken);
}
