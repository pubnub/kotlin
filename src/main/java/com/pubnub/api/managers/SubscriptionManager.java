package com.pubnub.api.managers;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SubscriptionManager {

    private List<String> subscribedChannels;
    private List<String> subscribedChannelGroups;

    public SubscriptionManager() {
        this.subscribedChannelGroups = new ArrayList<String>();
        this.subscribedChannels = new ArrayList<String>();
    }

    public void addChannel(String channel, boolean includePresence){
        this.subscribedChannels.add(channel);
    }

    public void addChannelGroup(String channelGroup, boolean includePresence) {
        this.subscribedChannelGroups.add(channelGroup);
    }

    public void removeChannel(String channel) {
        this.subscribedChannels.remove(channel);
    }

    public void removeChannelGroup(String channelGroup) {
        this.subscribedChannelGroups.remove(channelGroup);
    }

}
