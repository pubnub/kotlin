package com.pubnub.api.models.server;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class SubscribeMetadata {

    @SerializedName("t")
    private final Long timetoken;

    @SerializedName("r")
    private final String region;

}
