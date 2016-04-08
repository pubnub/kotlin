package com.pubnub.api.core.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PNSubscriberData {

    private String subscribedChannel;
    private String actualChannel;
    private Long timetoken;
    private Object userMetadata;
    private Long currentTimetoken;
    private Long lastTimeToken;
    private List<String> subscribedChannels;
    private List<String> subscribedChannelGroups;

}
