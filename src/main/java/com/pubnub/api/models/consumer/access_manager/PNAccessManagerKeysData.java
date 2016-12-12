package com.pubnub.api.models.consumer.access_manager;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.util.Map;

@Getter
public class PNAccessManagerKeysData {

    @SerializedName("auths")
    private Map<String, PNAccessManagerKeyData> authKeys;

}
