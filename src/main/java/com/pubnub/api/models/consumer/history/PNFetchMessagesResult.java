package com.pubnub.api.models.consumer.history;

import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@ToString
public class PNFetchMessagesResult {
    private Map<String, List<PNMessageResult>> channels;
}
