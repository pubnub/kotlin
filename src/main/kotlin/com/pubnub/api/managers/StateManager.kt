package com.pubnub.api.managers

import com.pubnub.api.builder.PresenceOperation
import com.pubnub.api.builder.StateOperation
import com.pubnub.api.builder.SubscribeOperation
import com.pubnub.api.builder.UnsubscribeOperation
import com.pubnub.api.models.SubscriptionItem

class StateManager {

    /**
     * Contains a list of subscribed channels
     */
    private val channels: HashMap<String, SubscriptionItem> = hashMapOf()

    /**
     * Contains a list of subscribed presence channels.
     */
    private val presenceChannels: HashMap<String, SubscriptionItem> = hashMapOf()

    /**
     * Contains a list of subscribed channel groups.
     */
    private val groups: HashMap<String, SubscriptionItem> = hashMapOf()

    /**
     * Contains a list of subscribed presence channel groups.
     */
    private val presenceGroups: HashMap<String, SubscriptionItem> = hashMapOf()

    private val heartbeatChannels: HashMap<String, SubscriptionItem> = hashMapOf()
    private val heartbeatGroups: HashMap<String, SubscriptionItem> = hashMapOf()

    @Synchronized
    internal fun adaptSubscribeBuilder(subscribeOperation: SubscribeOperation) {
        for (channel in subscribeOperation.channels) {
            if (channel.isEmpty()) {
                continue
            }
            val subscriptionItem = SubscriptionItem(channel)

            channels[channel] = subscriptionItem

            if (subscribeOperation.presenceEnabled) {
                val presenceSubscriptionItem = SubscriptionItem(channel)
                presenceChannels[channel] = presenceSubscriptionItem
            }
        }
        for (channelGroup in subscribeOperation.channelGroups) {
            if (channelGroup.isEmpty()) {
                continue
            }
            val subscriptionItem = SubscriptionItem(channelGroup)
            groups.put(channelGroup, subscriptionItem)
            if (subscribeOperation.presenceEnabled) {
                val presenceSubscriptionItem = SubscriptionItem(channelGroup)
                presenceGroups[channelGroup] = presenceSubscriptionItem
            }
        }
    }

    @Synchronized
    internal fun adaptStateBuilder(stateOperation: StateOperation) {
        for (channel in stateOperation.channels) {
            val subscribedChannel = channels[channel]
            subscribedChannel?.state = stateOperation.state
        }
        for (channelGroup in stateOperation.channelGroups) {
            val subscribedChannelGroup = groups[channelGroup]
            subscribedChannelGroup?.state = stateOperation.state
        }
    }

    @Synchronized
    internal fun adaptUnsubscribeBuilder(unsubscribeOperation: UnsubscribeOperation) {
        for (channel in unsubscribeOperation.channels) {
            channels.remove(channel)
            presenceChannels.remove(channel)
        }
        for (channelGroup in unsubscribeOperation.channelGroups) {
            groups.remove(channelGroup)
            presenceGroups.remove(channelGroup)
        }
    }

    @Synchronized
    internal fun adaptPresenceBuilder(presenceOperation: PresenceOperation) {
        for (channel in presenceOperation.channels) {
            if (channel.isEmpty()) {
                continue
            }
            if (presenceOperation.connected) {
                val subscriptionItem = SubscriptionItem(channel)
                heartbeatChannels[channel] = subscriptionItem
            } else {
                heartbeatChannels.remove(channel)
            }
        }
        for (channelGroup in presenceOperation.channelGroups) {
            if (channelGroup.isEmpty()) {
                continue
            }
            if (presenceOperation.connected) {
                val subscriptionItem = SubscriptionItem(channelGroup)
                heartbeatGroups[channelGroup] = subscriptionItem
            } else {
                heartbeatGroups.remove(channelGroup)
            }
        }
    }

    @Synchronized
    fun createStatePayload(): Map<String, Any?> {
        val stateResponse: HashMap<String, Any?> = hashMapOf()
        for (channel in channels.values) {
            if (channel.state != null) {
                stateResponse[channel.name] = channel.state
            }
        }
        for (channelGroup in groups.values) {
            if (channelGroup.state != null) {
                stateResponse[channelGroup.name] = channelGroup.state
            }
        }
        return stateResponse
    }

    @Synchronized
    fun prepareChannelList(includePresence: Boolean): List<String> {
        return prepareMembershipList(channels, presenceChannels, includePresence)
    }

    @Synchronized
    fun prepareChannelGroupList(includePresence: Boolean): List<String> {
        return prepareMembershipList(groups, presenceGroups, includePresence)
    }

    @Synchronized
    fun prepareHeartbeatChannelList(includePresence: Boolean): List<String> {
        return prepareMembershipList(heartbeatChannels, presenceChannels, includePresence)
    }

    @Synchronized
    fun prepareHeartbeatChannelGroupList(includePresence: Boolean): List<String> {
        return prepareMembershipList(heartbeatGroups, presenceGroups, includePresence)
    }

    @Synchronized
    fun isEmpty(): Boolean {
        return channels.isEmpty() && presenceChannels.isEmpty() && groups.isEmpty() && presenceGroups.isEmpty()
    }

    @Synchronized
    private fun prepareMembershipList(
        dataStorage: Map<String, SubscriptionItem>,
        presenceStorage: Map<String, SubscriptionItem>,
        includePresence: Boolean
    ): List<String> {
        val response: MutableList<String> = ArrayList()
        for (channelGroupItem in dataStorage.values) {
            response.add(channelGroupItem.name)
        }
        if (includePresence) {
            for (presenceChannelGroupItem in presenceStorage.values) {
                response.add(presenceChannelGroupItem.name + "-pnpres")
            }
        }
        return response
    }
}