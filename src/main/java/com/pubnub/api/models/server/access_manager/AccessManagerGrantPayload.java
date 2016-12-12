package com.pubnub.api.models.server.access_manager;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import com.pubnub.api.models.consumer.access_manager.PNAccessManagerKeyData;
import com.pubnub.api.models.consumer.access_manager.PNAccessManagerKeysData;
import lombok.Getter;

import java.util.Map;

@Getter
public class AccessManagerGrantPayload {

    @SerializedName("level")
    private String level;

    private int ttl;

    @SerializedName("subscribe_key")
    private String subscribeKey;

    @SerializedName("channels")
    private Map<String, PNAccessManagerKeysData> channels;

    @SerializedName("channel-groups")
    private JsonElement channelGroups;

    @SerializedName("auths")
    private Map<String, PNAccessManagerKeyData> authKeys;

    @SerializedName("channel")
    private String channel;

}
