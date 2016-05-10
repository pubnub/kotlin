package com.pubnub.api.models.consumer.pubsub;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PNMessageResult {

    private JsonNode message;

    private String subscribedChannel;
    private String actualChannel;
    private Long timetoken;
    private Object userMetadata;

}
