package com.pubnub.api.core.models;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class HistoryData {

    private List<HistoryItemData> items;
    private Long startTimeToken;
    private Long endTimeToken;

    public HistoryData() {
       this.items = new ArrayList<>();
    }

}
