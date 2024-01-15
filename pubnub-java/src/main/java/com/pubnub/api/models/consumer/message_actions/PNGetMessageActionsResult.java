package com.pubnub.api.models.consumer.message_actions;

import com.google.gson.annotations.SerializedName;
import com.pubnub.api.models.consumer.PNBoundedPage;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class PNGetMessageActionsResult {

    @SerializedName("data")
    private final List<PNMessageAction> actions;
    @SerializedName("more")
    private final PNBoundedPage page;
}
