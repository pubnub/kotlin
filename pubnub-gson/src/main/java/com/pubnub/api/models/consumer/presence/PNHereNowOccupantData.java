package com.pubnub.api.models.consumer.presence;

import com.google.gson.JsonElement;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class PNHereNowOccupantData {
    private String uuid;
    private JsonElement state;
}
