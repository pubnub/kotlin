package com.pubnub.api.java.models.consumer.objects_api.membership;

import com.google.gson.JsonElement;
import com.pubnub.api.models.consumer.pubsub.PubSubResult;
import com.pubnub.api.models.consumer.pubsub.objects.ObjectResult;
import lombok.Getter;

@Getter
public class PNMembershipResult implements ObjectResult<PNMembership>, PubSubResult {
    private final String event;
    private final PNMembership data;
    private final String channel;
    private final String subscription;
    private final Long timetoken;
    private final JsonElement userMetadata;
    private final String publisher;

    public PNMembershipResult(String event, PNMembership data, String channel, String subscription, Long timetoken, JsonElement userMetadata, String publisher) {
        this.event = event;
        this.data = data;
        this.channel = channel;
        this.subscription = subscription;
        this.timetoken = timetoken;
        this.userMetadata = userMetadata;
        this.publisher = publisher;
    }

}
