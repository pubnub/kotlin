package com.pubnub.api.models.server;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class PublishMetaData {

    @SerializedName("t")
    private Long publishTimetoken;

    @SerializedName("r")
    private Integer region;

}
