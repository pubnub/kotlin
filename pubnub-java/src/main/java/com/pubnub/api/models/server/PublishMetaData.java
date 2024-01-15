package com.pubnub.api.models.server;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class PublishMetaData {

    @SerializedName("t")
    private Long publishTimetoken;

    @SerializedName("r")
    private Integer region;

}
