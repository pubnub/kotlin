package com.pubnub.api.models.server;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class HistoryForChannelsEnvelope {

    @JsonProperty("channels")
    private Map<String, List<HistoryForChannelsItem>> channels;
}
