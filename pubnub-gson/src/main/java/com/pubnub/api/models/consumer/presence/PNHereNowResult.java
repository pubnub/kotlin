package com.pubnub.api.models.consumer.presence;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@Builder
@ToString
public class PNHereNowResult {
    private int totalChannels;
    private int totalOccupancy;
    private Map<String, PNHereNowChannelData> channels;

}
