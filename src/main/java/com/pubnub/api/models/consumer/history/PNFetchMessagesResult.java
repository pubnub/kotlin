package com.pubnub.api.models.consumer.history;

import com.google.gson.annotations.SerializedName;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class PNFetchMessagesResult {
    @SerializedName("channels")
    private Map<String, List<PNMessageResult>> channels;


}
