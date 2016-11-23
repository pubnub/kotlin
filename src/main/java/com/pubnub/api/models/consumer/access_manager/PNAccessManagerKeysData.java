package com.pubnub.api.models.consumer.access_manager;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Map;

@Getter
public class PNAccessManagerKeysData {

    @JsonProperty("auths")
    private Map<String, PNAccessManagerKeyData> authKeys;

}
