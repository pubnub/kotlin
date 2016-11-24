package com.pubnub.api.models.server;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class FetchMessagesEnvelope {

    @JsonProperty("channels")
    private Map<String, List<HistoryForChannelsItem>> channels;
}
