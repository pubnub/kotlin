package com.pubnub.api.models.consumer.presence;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PNHereNowOccupantData {
    private String uuid;
    private Object state;
}
