package com.pubnub.api.models.consumer.history;

import com.google.gson.JsonElement;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class PNHistoryItemResult {

    @Getter private Long timetoken;
    @Getter private JsonElement entry;

}
