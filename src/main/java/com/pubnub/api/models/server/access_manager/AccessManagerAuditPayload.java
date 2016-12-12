package com.pubnub.api.models.server.access_manager;

import com.google.gson.annotations.SerializedName;
import com.pubnub.api.models.consumer.access_manager.PNAccessManagerKeyData;
import lombok.Getter;

import java.util.Map;

@Getter
public class AccessManagerAuditPayload {

    @SerializedName("level")
    private String level;

    @SerializedName("subscribe_key")
    private String subscribeKey;

    @SerializedName("channel")
    private String channel;

    @SerializedName("channel-group")
    private String channelGroup;

    @SerializedName("auths")
    private Map<String, PNAccessManagerKeyData> authKeys;


}
