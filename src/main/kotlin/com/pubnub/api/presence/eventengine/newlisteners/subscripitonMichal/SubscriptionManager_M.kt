package com.pubnub.api.presence.eventengine.newlisteners.subscripitonMichal

import java.util.function.Consumer

class SubscriptionManager_M {
    val allChannelSubscriptions: MutableList<ChannelSubscriptionCollection> =
        mutableListOf() //todo do we allow duplicate ChannelSubscription i.e. subscription that have the same channel?

    fun addSubscriptionToGlobalistAndReturnIt(
        channels: Set<String>,
        onMessage: Consumer<String>
    ): ChannelSubscriptionCollection {
        val channelSubscriptionCollection = ChannelSubscriptionCollection()
        channels.forEach { channel ->
            val channelSubscription = ChannelSubscription_M(
                channel = channel,
                onMessage = onMessage,
                onCancel = { channelSubscription -> channelSubscriptionCollection.remove(channelSubscription) },
            )
            channelSubscriptionCollection.add(channelSubscription)
        }
        allChannelSubscriptions.add(channelSubscriptionCollection)
        return channelSubscriptionCollection
    }


    fun addOnMessageToAllSubscriptions(onMessage: Consumer<String>) {
        allChannelSubscriptions.forEach { subscription ->
            subscription.onMessage = onMessage
        }
    }

    fun removeAllChannelSubscriptions() {
        allChannelSubscriptions.clear()
    }

    fun removeChannelFromAllSubscriptions(channels: Set<String>) {
        channels.forEach { channel ->
            val subscriptionCollectionContainingChannel: List<ChannelSubscriptionCollection> =
                allChannelSubscriptions.filter { subscriptionCollection ->
                    subscriptionCollection.subscriptions.keys.contains(channel)
                }
            subscriptionCollectionContainingChannel.forEach { subscriptionCollectionContainingChannel ->
                subscriptionCollectionContainingChannel.remove(
                    channel
                )
            }
        }
    }

    fun pauseAllChannelSubscriptions() {
        allChannelSubscriptions.forEach { channelSubscriptionCollection ->
            channelSubscriptionCollection.pauseOnMessage()
        }
    }

    fun resumeAllChannelSubscriptions() {
        allChannelSubscriptions.forEach { channelSubscriptionCollection ->
            channelSubscriptionCollection.resumeOnMessage()
        }
    }


    //    ----------
    fun simulateReceiveMessage(channels: Set<String>) {
        val channelsAsString = channels.joinToString(separator = ", ")
        channels.forEach { channel ->
            val subscriptionCollectionContainingChannel: List<ChannelSubscriptionCollection> =
                allChannelSubscriptions.filter { subscriptionCollection ->
                    subscriptionCollection.subscriptions.keys.contains(channel)
                }
            subscriptionCollectionContainingChannel.forEach { subscriptionCollection ->
                subscriptionCollection.subscriptions.values.forEach { subscription ->
                    if (subscription.channel.equals(channel) && subscription.shouldHandleMessage) {
                        subscription.onMessage.accept("This is receive message for channels: $channelsAsString ")
                    }
                }
            }
        }
    }
}
