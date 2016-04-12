package com.pubnub.api.core.models.consumer_facing;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PNHistoryResult extends PNResult {

    private List<PNHistoryItemResult> messages;
    private Long startTimeToken;
    private Long endTimeToken;

    public PNHistoryResult() {
       this.messages = new ArrayList<>();
    }

}
