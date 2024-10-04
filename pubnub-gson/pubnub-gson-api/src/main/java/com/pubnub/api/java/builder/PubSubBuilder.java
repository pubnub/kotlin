package com.pubnub.api.java.builder;


import java.util.List;

public interface PubSubBuilder {

    PubSubBuilder channels(List<String> channel);

    PubSubBuilder channelGroups(List<String> channelGroup);

    void execute();

}
