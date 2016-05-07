package com.pubnub.api.models.server.access_manager;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.pubnub.api.models.consumer.access_manager.PNAccessManagerKeyData;
import com.pubnub.api.models.consumer.access_manager.PNAccessManagerKeysData;
import lombok.Getter;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class AccessManagerGrantPayload {

    private String level;
    private int ttl;
    @JsonProperty("subscribe_key")
    private String subscribeKey;

    private Map<String, PNAccessManagerKeysData> channels;

    @JsonProperty("channel-groups")
    private JsonNode channelGroups;

    @JsonProperty("auths")
    private Map<String, PNAccessManagerKeyData> authKeys;

    private String channel;

}
