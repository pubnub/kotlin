package com.pubnub.api.models.server;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class OriginationMetaData {

    @SerializedName("t")
    private Long timetoken;

    @SerializedName("r")
    private Integer region;

}
