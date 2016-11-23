package com.pubnub.api.models.server.access_manager;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.pubnub.api.models.consumer.access_manager.PNAccessManagerKeyData;
import com.pubnub.api.models.consumer.access_manager.PNAccessManagerKeysData;
import lombok.Getter;

import java.util.Map;

@Getter
public class AccessManagerGrantPayload {

    @JsonProperty("level")
    private String level;

    private int ttl;

    @JsonProperty("subscribe_key")
    private String subscribeKey;

    @JsonProperty("channels")
    private Map<String, PNAccessManagerKeysData> channels;

    @JsonProperty("channel-groups")
    private JsonNode channelGroups;

    @JsonProperty("auths")
    private Map<String, PNAccessManagerKeyData> authKeys;

    @JsonProperty("channel")
    private String channel;

}
