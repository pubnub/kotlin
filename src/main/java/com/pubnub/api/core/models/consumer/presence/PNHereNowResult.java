package com.pubnub.api.core.models.consumer.presence;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class PNHereNowResult {
	private int totalChannels;
    private int totalOccupancy;
    private Map<String, PNHereNowChannelData> channels;

}
