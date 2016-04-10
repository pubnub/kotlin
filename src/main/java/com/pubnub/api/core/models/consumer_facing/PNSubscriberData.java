package com.pubnub.api.core.models.consumer_facing;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PNSubscriberData {

    private String subscribedChannel;
    private String actualChannel;
    private Long timetoken;
    private Object userMetadata;
}
