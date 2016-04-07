package com.pubnub.api.core.models.HereNow;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class HereNowChannelData {

    private String channelName;
    private int occupancy;
    private List<HereNowOccupantData> occupants;

    public HereNowChannelData() {
        occupants = new ArrayList<>();
    }

}
