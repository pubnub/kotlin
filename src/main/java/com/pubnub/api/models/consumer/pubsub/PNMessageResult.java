package com.pubnub.api.models.consumer.pubsub;

import com.google.gson.JsonElement;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class PNMessageResult {

    private JsonElement message;

    @Deprecated
    private String subscribedChannel;
    @Deprecated
    private String actualChannel;

    private String channel;
    private String subscription;

    private Long timetoken;
    private JsonElement userMetadata;

    private String publisher;
}

