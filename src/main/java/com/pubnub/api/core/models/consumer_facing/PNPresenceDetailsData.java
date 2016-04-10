package com.pubnub.api.core.models.consumer_facing;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PNPresenceDetailsData {

    private String uuid;
    private Long timestamp;
    private Integer occupancy;

}
