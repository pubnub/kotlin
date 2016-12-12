package com.pubnub.api.managers;


import com.pubnub.api.builder.dto.StateOperation;
import com.pubnub.api.builder.dto.SubscribeOperation;
import com.pubnub.api.builder.dto.UnsubscribeOperation;
import com.pubnub.api.models.SubscriptionItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StateManager {

    /**
     * Contains a list of subscribed channels
     */
    private Map<String, SubscriptionItem> channels;
    /**
     * Contains a list of subscribed presence channels.
     */
    private Map<String, SubscriptionItem> presenceChannels;

    /**
     * Contains a list of subscribed channel groups.
     */
    private Map<String, SubscriptionItem> groups;

    /**
     * Contains a list of subscribed presence channel groups.
     */
    private Map<String, SubscriptionItem> presenceGroups;

    public StateManager() {
        this.channels = new HashMap<>();
        this.presenceChannels = new HashMap<>();

        this.groups = new HashMap<>();
        this.presenceGroups = new HashMap<>();
    }


    public synchronized void adaptSubscribeBuilder(SubscribeOperation subscribeOperation) {
        for (String channel : subscribeOperation.getChannels()) {
            SubscriptionItem subscriptionItem = new SubscriptionItem().setName(channel);
            channels.put(channel, subscriptionItem);

            if (subscribeOperation.isPresenceEnabled()) {
                SubscriptionItem presenceSubscriptionItem = new SubscriptionItem().setName(channel);
                presenceChannels.put(channel, presenceSubscriptionItem);
            }

        }

        for (String channelGroup : subscribeOperation.getChannelGroups()) {
            SubscriptionItem subscriptionItem = new SubscriptionItem().setName(channelGroup);
            groups.put(channelGroup, subscriptionItem);

            if (subscribeOperation.isPresenceEnabled()) {
                SubscriptionItem presenceSubscriptionItem = new SubscriptionItem().setName(channelGroup);
                presenceGroups.put(channelGroup, presenceSubscriptionItem);
            }

        }
    }

    public synchronized void adaptStateBuilder(StateOperation stateOperation) {
        for (String channel: stateOperation.getChannels()) {
            SubscriptionItem subscribedChannel = channels.get(channel);

            if (subscribedChannel != null) {
                subscribedChannel.setState(stateOperation.getState());
            }
        }

        for (String channelGroup: stateOperation.getChannelGroups()) {
            SubscriptionItem subscribedChannelGroup = groups.get(channelGroup);

            if (subscribedChannelGroup != null) {
                subscribedChannelGroup.setState(stateOperation.getState());
            }
        }
    }


    public synchronized void adaptUnsubscribeBuilder(UnsubscribeOperation unsubscribeOperation) {
        for (String channel: unsubscribeOperation.getChannels()) {
            this.channels.remove(channel);
            this.presenceChannels.remove(channel);
        }

        for (String channelGroup: unsubscribeOperation.getChannelGroups()) {
            this.groups.remove(channelGroup);
            this.presenceGroups.remove(channelGroup);
        }
    }

    public synchronized Map<String, Object> createStatePayload() {
        Map<String, Object> stateResponse = new HashMap<>();

        for (SubscriptionItem channel: channels.values()) {
            if (channel.getState() != null) {
                stateResponse.put(channel.getName(), channel.getState());
            }
        }

        for (SubscriptionItem channelGroup: groups.values()) {
            if (channelGroup.getState() != null) {
                stateResponse.put(channelGroup.getName(), channelGroup.getState());
            }
        }

        return stateResponse;
    }

    public synchronized List<String> prepareChannelList(boolean includePresence) {
        return prepareMembershipList(channels, presenceChannels, includePresence);
    }

    public synchronized List<String> prepareChannelGroupList(boolean includePresence) {
        return prepareMembershipList(groups, presenceGroups, includePresence);
    }

    public synchronized boolean isEmpty() {
        return (channels.isEmpty() && presenceChannels.isEmpty() && groups.isEmpty() && presenceGroups.isEmpty());
    }

    private synchronized List<String> prepareMembershipList(Map<String, SubscriptionItem> dataStorage, Map<String, SubscriptionItem> presenceStorage, boolean includePresence) {
        List<String> response = new ArrayList<>();

        for (SubscriptionItem channelGroupItem: dataStorage.values()) {
            response.add(channelGroupItem.getName());
        }

        if (includePresence) {
            for (SubscriptionItem presenceChannelGroupItem: presenceStorage.values()) {
                response.add(presenceChannelGroupItem.getName().concat("-pnpres"));
            }
        }


        return response;
    }

}
