package com.pubnub.api.core.models;

import com.pubnub.api.core.enums.SubscriptionType;
import lombok.Data;

@Data
public class SubscriptionItem {

    private String name;
    private SubscriptionType type;
    private boolean withPresence;
    private Object state;

}
