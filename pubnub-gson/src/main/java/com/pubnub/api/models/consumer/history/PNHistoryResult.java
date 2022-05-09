package com.pubnub.api.models.consumer.history;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@ToString
public class PNHistoryResult {

    private List<PNHistoryItemResult> messages;
    private Long startTimetoken;
    private Long endTimetoken;

}
