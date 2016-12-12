package com.pubnub.api.models.consumer.presence;

import com.google.gson.JsonElement;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PNSetStateResult {

    private JsonElement state;

}
