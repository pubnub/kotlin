package com.pubnub.api.core.models.consumer_facing;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class PNAccessManagerAuditData {

    String level;
    @JsonProperty("subscribe_key")
    String subscribeKey;

    String channel;

    @JsonProperty("channel-group")
    String channelGroup;

    @JsonProperty("auths")
    Map<String, PNAccessManagerKeyData> authKeys;


}
