package com.pubnub.api.models.consumer.presence;

import com.google.gson.JsonElement;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Builder
@Getter
public class PNGetStateResult {

    private Map<String, JsonElement> stateByUUID;

}
