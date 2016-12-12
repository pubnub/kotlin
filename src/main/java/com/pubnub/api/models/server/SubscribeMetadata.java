package com.pubnub.api.models.server;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class SubscribeMetadata {

    @SerializedName("t")
    private Long timetoken;

    @SerializedName("r")
    private String region;

}
