package com.pubnub.api.models.server.access_manager;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pubnub.api.models.consumer.access_manager.PNAccessManagerKeyData;
import lombok.Getter;

import java.util.Map;

@Getter
public class AccessManagerAuditPayload {

    @JsonProperty("level")
    private String level;

    @JsonProperty("subscribe_key")
    private String subscribeKey;

    @JsonProperty("channel")
    private String channel;

    @JsonProperty("channel-group")
    private String channelGroup;

    @JsonProperty("auths")
    private Map<String, PNAccessManagerKeyData> authKeys;


}
