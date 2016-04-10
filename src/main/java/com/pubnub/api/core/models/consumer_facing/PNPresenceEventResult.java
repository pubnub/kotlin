package com.pubnub.api.core.models.consumer_facing;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PNPresenceEventResult extends PNResult {

    private PNPresenceEventData data;

}
