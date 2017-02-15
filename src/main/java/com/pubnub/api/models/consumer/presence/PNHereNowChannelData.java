package com.pubnub.api.models.consumer.presence;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@ToString
public class PNHereNowChannelData {

    private String channelName;
    private int occupancy;
    private List<PNHereNowOccupantData> occupants;

}
