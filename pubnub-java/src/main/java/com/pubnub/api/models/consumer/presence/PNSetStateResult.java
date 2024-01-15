package com.pubnub.api.models.consumer.presence;

import com.google.gson.JsonElement;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class PNSetStateResult {

    private JsonElement state;

}
