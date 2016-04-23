package com.pubnub.api.core.models.consumer_facing;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class PNAccessManagerGrantData {

    String level;
    int ttl;
    @JsonProperty("subscribe_key")
    String subscribeKey;

    Map<String, PNAccessManagerKeysData> channels;

    @JsonProperty("channel-groups")
    JsonNode channelGroups;

    @JsonProperty("auths")
    Map<String, PNAccessManagerKeyData> authKeys;

    String channel;

}
