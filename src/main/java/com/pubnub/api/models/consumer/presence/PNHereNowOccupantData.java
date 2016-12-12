package com.pubnub.api.models.consumer.presence;

import com.google.gson.JsonElement;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PNHereNowOccupantData {
    private String uuid;
    private JsonElement state;
}
