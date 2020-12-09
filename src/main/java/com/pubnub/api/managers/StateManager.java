package com.pubnub.api.managers;

import com.pubnub.api.PubNub;
import com.pubnub.api.builder.dto.PresenceOperation;
import com.pubnub.api.builder.dto.StateOperation;
import com.pubnub.api.builder.dto.SubscribeOperation;
import com.pubnub.api.builder.dto.UnsubscribeOperation;
import com.pubnub.api.models.SubscriptionItem;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StateManager {
    @Data
    @AllArgsConstructor
    private static class TemporaryUnavailableItem {
        private String item;
        private Date timestamp;
    }

    static final int MILLIS_IN_SECOND = 1000;

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

    private Map<String, SubscriptionItem> heartbeatChannels;
    private Map<String, SubscriptionItem> heartbeatGroups;

    private List<TemporaryUnavailableItem> temporaryUnavailableChannels = new ArrayList<>();
    private List<TemporaryUnavailableItem> temporaryUnavailableChannelGroups = new ArrayList<>();

    private final PubNub pubnub;

    public StateManager(final PubNub pubnub) {
        this.pubnub = pubnub;
        this.channels = new HashMap<>();
        this.presenceChannels = new HashMap<>();

        this.groups = new HashMap<>();
        this.presenceGroups = new HashMap<>();

        this.heartbeatChannels = new HashMap<>();
        this.heartbeatGroups = new HashMap<>();
    }

    public synchronized void adaptSubscribeBuilder(SubscribeOperation subscribeOperation) {
        for (String channel : subscribeOperation.getChannels()) {
            if (channel == null || channel.length() == 0) {
                continue;
            }

            SubscriptionItem subscriptionItem = new SubscriptionItem().setName(channel);
            channels.put(channel, subscriptionItem);

            if (subscribeOperation.isPresenceEnabled()) {
                SubscriptionItem presenceSubscriptionItem = new SubscriptionItem().setName(channel);
                presenceChannels.put(channel, presenceSubscriptionItem);
            }

        }

        for (String channelGroup : subscribeOperation.getChannelGroups()) {
            if (channelGroup == null || channelGroup.length() == 0) {
                continue;
            }

            SubscriptionItem subscriptionItem = new SubscriptionItem().setName(channelGroup);
            groups.put(channelGroup, subscriptionItem);

            if (subscribeOperation.isPresenceEnabled()) {
                SubscriptionItem presenceSubscriptionItem = new SubscriptionItem().setName(channelGroup);
                presenceGroups.put(channelGroup, presenceSubscriptionItem);
            }

        }
    }

    public synchronized void adaptStateBuilder(StateOperation stateOperation) {
        for (String channel : stateOperation.getChannels()) {
            SubscriptionItem subscribedChannel = channels.get(channel);

            if (subscribedChannel != null) {
                subscribedChannel.setState(stateOperation.getState());
            }
        }

        for (String channelGroup : stateOperation.getChannelGroups()) {
            SubscriptionItem subscribedChannelGroup = groups.get(channelGroup);

            if (subscribedChannelGroup != null) {
                subscribedChannelGroup.setState(stateOperation.getState());
            }
        }
    }


    public synchronized void adaptUnsubscribeBuilder(UnsubscribeOperation unsubscribeOperation) {
        for (String channel : unsubscribeOperation.getChannels()) {
            this.channels.remove(channel);
            this.presenceChannels.remove(channel);
            removeTemporaryUnavailableChannel(channel);
        }

        for (String channelGroup : unsubscribeOperation.getChannelGroups()) {
            this.groups.remove(channelGroup);
            this.presenceGroups.remove(channelGroup);
        }
    }

    public synchronized void adaptPresenceBuilder(PresenceOperation presenceOperation) {
        for (String channel : presenceOperation.getChannels()) {
            if (channel == null || channel.length() == 0) {
                continue;
            }

            if (presenceOperation.isConnected()) {
                SubscriptionItem subscriptionItem = new SubscriptionItem().setName(channel);
                heartbeatChannels.put(channel, subscriptionItem);
            } else {
                heartbeatChannels.remove(channel);
            }

        }

        for (String channelGroup : presenceOperation.getChannelGroups()) {
            if (channelGroup == null || channelGroup.length() == 0) {
                continue;
            }

            if (presenceOperation.isConnected()) {
                SubscriptionItem subscriptionItem = new SubscriptionItem().setName(channelGroup);
                heartbeatGroups.put(channelGroup, subscriptionItem);
            } else {
                heartbeatGroups.remove(channelGroup);
            }

        }
    }

    public synchronized Map<String, Object> createStatePayload() {
        Map<String, Object> stateResponse = new HashMap<>();

        for (SubscriptionItem channel : channels.values()) {
            if (channel.getState() != null) {
                stateResponse.put(channel.getName(), channel.getState());
            }
        }

        for (SubscriptionItem channelGroup : groups.values()) {
            if (channelGroup.getState() != null) {
                stateResponse.put(channelGroup.getName(), channelGroup.getState());
            }
        }

        return stateResponse;
    }

    public synchronized List<String> prepareTargetChannelList(boolean includePresence) {
        return prepareMembershipList(channels, presenceChannels, includePresence);
    }

    public synchronized List<String> prepareTargetChannelGroupList(boolean includePresence) {
        return prepareMembershipList(groups, presenceGroups, includePresence);
    }

    public synchronized List<String> prepareTargetHeartbeatChannelList(boolean includePresence) {
        return prepareMembershipList(heartbeatChannels, presenceChannels, includePresence);
    }

    public synchronized List<String> prepareTargetHeartbeatChannelGroupList(boolean includePresence) {
        return prepareMembershipList(heartbeatGroups, presenceGroups, includePresence);
    }

    public boolean hasAnythingToSubscribe() {
        final List<String> combinedChannels = prepareTargetChannelList(true);
        final List<String> combinedChannelGroups = prepareTargetChannelGroupList(true);

        return !combinedChannels.isEmpty() || !combinedChannelGroups.isEmpty();
    }

    public synchronized void resetTemporaryUnavailableChannelsAndGroups() {
        temporaryUnavailableChannels.clear();
        temporaryUnavailableChannelGroups.clear();
    }

    public synchronized void removeTemporaryUnavailableChannel(String channel) {
        TemporaryUnavailableItem temporaryUnavailableChannelsToBeRemoved = null;
        for (final TemporaryUnavailableItem temporaryUnavailableChannel : temporaryUnavailableChannels) {
            if (temporaryUnavailableChannel.getItem().equals(channel)) {
                temporaryUnavailableChannelsToBeRemoved = temporaryUnavailableChannel;
            }
        }
        if (temporaryUnavailableChannelsToBeRemoved != null) {
            temporaryUnavailableChannels.remove(temporaryUnavailableChannelsToBeRemoved);
        }
    }

    public synchronized void removeTemporaryUnavailableChannelGroup(String channelGroup) {
        TemporaryUnavailableItem temporaryUnavailableChannelGroupsToBeRemoved = null;
        for (final TemporaryUnavailableItem temporaryUnavailableChannelGroup : temporaryUnavailableChannelGroups) {
            if (temporaryUnavailableChannelGroup.getItem().equals(channelGroup)) {
                temporaryUnavailableChannelGroupsToBeRemoved = temporaryUnavailableChannelGroup;
            }
        }
        if (temporaryUnavailableChannelGroupsToBeRemoved != null) {
            temporaryUnavailableChannelGroups.remove(temporaryUnavailableChannelGroupsToBeRemoved);
        }
    }

    public synchronized void addTemporaryUnavailableChannel(String channel) {
        temporaryUnavailableChannels.add(new TemporaryUnavailableItem(channel, new Date()));
    }

    public synchronized void addTemporaryUnavailableChannelGroup(String channelGroupName) {
        temporaryUnavailableChannelGroups.add(new TemporaryUnavailableItem(channelGroupName, new Date()));
    }

    public boolean subscribedToOnlyTemporaryUnavailable() {
        return effectiveChannels().isEmpty() && effectiveChannelGroups().isEmpty();
    }

    public List<String> effectiveChannels() {
        final List<String> effectiveChannelsList = prepareTargetChannelList(true);
        effectiveChannelsList.removeAll(channelsToPostponeSubscription(temporaryUnavailableChannels));
        return effectiveChannelsList;
    }

    public List<String> effectiveChannelGroups() {
        final List<String> effectiveChannelGroupsList = prepareTargetChannelGroupList(true);
        effectiveChannelGroupsList.removeAll(channelGroupsToPostponeSubscription(temporaryUnavailableChannelGroups));
        return effectiveChannelGroupsList;
    }

    private List<String> channelsToPostponeSubscription(final List<TemporaryUnavailableItem> temporaryUnavailableChannels) {
        final List<String> result = new ArrayList<>();

        for (TemporaryUnavailableItem temporaryUnavailableChannel : temporaryUnavailableChannels) {
            if (temporaryUnavailableChannel.getTimestamp()
                    .after(new Date(System.currentTimeMillis() - pubnub.getConfiguration().getConnectTimeout() * MILLIS_IN_SECOND))) {
                result.add(temporaryUnavailableChannel.getItem());
            }
        }
        return result;
    }

    private List<String> channelGroupsToPostponeSubscription(final List<TemporaryUnavailableItem> temporaryUnavailableChannelGroups) {
        final List<String> result = new ArrayList<>();

        for (TemporaryUnavailableItem temporaryUnavailableChannelGroup : temporaryUnavailableChannelGroups) {
            if (temporaryUnavailableChannelGroup.getTimestamp()
                    .after(new Date(System.currentTimeMillis() - pubnub.getConfiguration().getConnectTimeout() * MILLIS_IN_SECOND))) {
                result.add(temporaryUnavailableChannelGroup.getItem());
            }
        }
        return result;
    }

    public synchronized boolean isEmpty() {
        return (channels.isEmpty() && presenceChannels.isEmpty() && groups.isEmpty() && presenceGroups.isEmpty());
    }

    private synchronized List<String> prepareMembershipList(Map<String, SubscriptionItem> dataStorage, Map<String,
            SubscriptionItem> presenceStorage, boolean includePresence) {
        List<String> response = new ArrayList<>();

        for (SubscriptionItem channelGroupItem : dataStorage.values()) {
            response.add(channelGroupItem.getName());
        }

        if (includePresence) {
            for (SubscriptionItem presenceChannelGroupItem : presenceStorage.values()) {
                response.add(presenceChannelGroupItem.getName().concat("-pnpres"));
            }
        }


        return response;
    }

}
