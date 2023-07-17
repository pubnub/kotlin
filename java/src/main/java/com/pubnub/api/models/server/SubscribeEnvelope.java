package com.pubnub.api.models.server;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class SubscribeEnvelope {

    @SerializedName("m")
    private final List<SubscribeMessage> messages;

    @SerializedName("t")
    private final SubscribeMetadata metadata;

}
