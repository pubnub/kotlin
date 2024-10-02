package com.pubnub.api.java.builder;

import java.util.List;

public interface SubscribeBuilder extends PubSubBuilder {

    SubscribeBuilder withPresence();

    SubscribeBuilder withTimetoken(Long timetokenInstance);

    SubscribeBuilder channels(List<String> channels);

    SubscribeBuilder channelGroups(List<String> channelGroups);
}
