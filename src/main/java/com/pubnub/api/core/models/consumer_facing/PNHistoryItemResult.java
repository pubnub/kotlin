package com.pubnub.api.core.models.consumer_facing;

import lombok.Getter;
import lombok.Setter;

@Setter
public class PNHistoryItemResult {

    @Getter private Long timetoken;
    @Getter private Object entry;

}
