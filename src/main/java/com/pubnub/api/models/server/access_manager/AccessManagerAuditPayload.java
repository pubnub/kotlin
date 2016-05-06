package com.pubnub.api.models.server.access_manager;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pubnub.api.models.consumer.access_manager.PNAccessManagerKeyData;
import lombok.Getter;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class AccessManagerAuditPayload {

    private String level;
    @JsonProperty("subscribe_key")
    private String subscribeKey;

    private String channel;

    @JsonProperty("channel-group")
    private String channelGroup;

    @JsonProperty("auths")
    private Map<String, PNAccessManagerKeyData> authKeys;


}
