package com.pubnub.api.core.models;

import com.pubnub.api.core.enums.SubscriptionType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SubscriptionItem {

    private String name;
    private SubscriptionType type;
    private boolean withPresence;
    private Object state;

}
