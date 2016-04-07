package com.pubnub.api.core.models;

import lombok.Getter;
import lombok.Setter;

@Setter
public class HistoryItemData {

    @Getter private Long timetoken;
    @Getter private Object entry;

}
