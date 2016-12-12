package com.pubnub.api.models.server;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class FetchMessagesEnvelope {

    @SerializedName("channels")
    private Map<String, List<HistoryForChannelsItem>> channels;
}
