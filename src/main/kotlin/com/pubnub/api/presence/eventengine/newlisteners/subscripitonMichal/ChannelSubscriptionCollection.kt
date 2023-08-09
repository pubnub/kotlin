package com.pubnub.api.presence.eventengine.newlisteners.subscripitonMichal

import java.util.function.Consumer

class ChannelSubscriptionCollection {
    internal val subscriptions: MutableMap<String, ChannelSubscription_M> = mutableMapOf()

    var onMessage: Consumer<String> = Consumer { }  //todo is this ok that consumer is empty.
        set(value) {
            subscriptions.forEach { (_, subscription) ->
                subscription.onMessage = value
            }
            field = value
        }

    internal fun add(channelSubscription: ChannelSubscription_M) {
        subscriptions[channelSubscription.channel] = channelSubscription
    }

    internal fun remove(channelSubscription: ChannelSubscription_M){
        subscriptions.remove(channelSubscription.channel)
    }

    internal fun remove(channel: String){
        subscriptions.remove(channel)
    }

    operator fun get(channel01: String): ChannelSubscription_M? {  //check this out "operator" allows subscriptions[channel01] instead of subscriptions.get(channel01)
        return subscriptions[channel01]
    }

    fun cancel() {  //unsubscribe
//        subscriptions.forEach { (_, subscription) ->
//            subscription.cancel()
//        }
        val subscriptionsToCancel = subscriptions.values.toList() // Create a copy of the values to avoid concurrent modification exception
        subscriptionsToCancel.forEach { subscription ->
            subscription.cancel()
        }
    }

    fun pauseOnMessage() {
        subscriptions.forEach { (_, subscription) ->
            subscription.pauseOnMessage()
        }
    }

    fun resumeOnMessage() {
        subscriptions.forEach { (_, subscription) ->
            subscription.resumeOnMessage()
        }
    }
}
