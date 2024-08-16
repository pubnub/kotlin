package com.pubnub.api.java.models.consumer.objects_api.channel;

import com.google.gson.JsonElement;
import com.pubnub.api.models.consumer.pubsub.PubSubResult;
import com.pubnub.api.models.consumer.pubsub.objects.ObjectResult;
import lombok.Getter;

@Getter
public class PNChannelMetadataResult implements ObjectResult<PNChannelMetadata>, PubSubResult {

    private final String event;
    private final String channel;
    private final String subscription;
    private final Long timetoken;
    private final JsonElement userMetadata;
    private final String publisher;
    private final PNChannelMetadata data;

    public PNChannelMetadataResult(String event, String channel, String subscription, Long getTimetoken, JsonElement userMetadata, String publisher, PNChannelMetadata data) {
        this.event = event;
        this.channel = channel;
        this.subscription = subscription;
        this.timetoken = getTimetoken;
        this.userMetadata = userMetadata;
        this.publisher = publisher;
        this.data = data;
    }
}
