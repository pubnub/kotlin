package com.pubnub.api.core.models.consumer_facing;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    Map<String, PNAccessManagerKeysData> channelGroups;
}
