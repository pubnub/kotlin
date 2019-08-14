package com.pubnub.api.models.consumer.pubsub;

import com.google.gson.JsonElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@AllArgsConstructor
@Getter
@ToString
public class MessageResult {

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

    public MessageResult(MessageResult messageResult) {
        this.message = messageResult.message;
        this.subscribedChannel = messageResult.subscribedChannel;
        this.actualChannel = messageResult.actualChannel;
        this.channel = messageResult.channel;
        this.subscription = messageResult.subscription;
        this.timetoken = messageResult.timetoken;
        this.userMetadata = messageResult.userMetadata;
        this.publisher = messageResult.publisher;
    }
}

