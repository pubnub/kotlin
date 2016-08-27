package com.pubnub.api.models.consumer.pubsub;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PNPresenceEventResult {

    private String event;

    private String uuid;
    private Long timestamp;
    private Integer occupancy;
    private JsonNode state;

    @Deprecated
    private String subscribedChannel;
    @Deprecated
    private String actualChannel;

    private String channel;
    private String channelGroup;

    private Long timetoken;
    private Object userMetadata;

}
