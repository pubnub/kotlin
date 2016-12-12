package com.pubnub.api.models.server;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HistoryForChannelsItem {

    @SerializedName("message")
    @Getter private JsonElement message;

    @SerializedName("timetoken")
    @Getter private Long timeToken;

}
