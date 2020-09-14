package com.pubnub.api.models.consumer.access_manager;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class PNAccessManagerKeyData {

    @SerializedName("r")
    private boolean readEnabled;

    @SerializedName("w")
    private boolean writeEnabled;

    @SerializedName("m")
    private boolean manageEnabled;

    @SerializedName("d")
    private boolean deleteEnabled;

    @SerializedName("g")
    private boolean getEnabled;

    @SerializedName("u")
    private boolean updateEnabled;

    @SerializedName("j")
    private boolean joinEnabled;

}
