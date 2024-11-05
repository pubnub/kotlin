package com.pubnub.api.java.models.consumer.access_manager;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

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

    @NotNull
    static PNAccessManagerKeyData from(@NotNull com.pubnub.api.models.consumer.access_manager.PNAccessManagerKeyData data) {
        return PNAccessManagerKeyData.builder()
                .readEnabled(data.getReadEnabled())
                .writeEnabled(data.getWriteEnabled())
                .manageEnabled(data.getManageEnabled())
                .deleteEnabled(data.getDeleteEnabled())
                .getEnabled(data.getGetEnabled())
                .updateEnabled(data.getUpdateEnabled())
                .joinEnabled(data.getJoinEnabled())
                .build();
    }
}
