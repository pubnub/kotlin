package com.pubnub.api.models.consumer.presence;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PNSetStateResult {

    private JsonNode state;

}
