package com.pubnub.api.core.models.consumer_facing;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PNPresenceEventData extends PNSubscriberData {

    private String presenceEvent;
    private PNPresenceDetailsData presence;

}
