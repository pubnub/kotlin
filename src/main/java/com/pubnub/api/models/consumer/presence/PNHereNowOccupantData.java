package com.pubnub.api.models.consumer.presence;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PNHereNowOccupantData {
    private String uuid;
    private JsonNode state;
}
