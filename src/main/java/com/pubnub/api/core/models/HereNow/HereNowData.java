package com.pubnub.api.core.models.HereNow;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class HereNowData {
	private int totalChannels;
    private int totalOccupancy;
    private Map<String, HereNowChannelData> channels;

    public HereNowData() {
        channels = new HashMap<String, HereNowChannelData>();
    }

}
