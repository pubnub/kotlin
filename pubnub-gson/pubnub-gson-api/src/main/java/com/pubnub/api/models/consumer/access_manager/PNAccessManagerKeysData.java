package com.pubnub.api.models.consumer.access_manager;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@ToString
public class PNAccessManagerKeysData {

    @SerializedName("auths")
    private Map<String, PNAccessManagerKeyData> authKeys;

}
