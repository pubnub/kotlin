package com.pubnub.api.models.server;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.util.List;

@Getter
public class SubscribeEnvelope {

    @SerializedName("m")
    private List<SubscribeMessage> messages;

    @SerializedName("t")
    private SubscribeMetadata metadata;

}
