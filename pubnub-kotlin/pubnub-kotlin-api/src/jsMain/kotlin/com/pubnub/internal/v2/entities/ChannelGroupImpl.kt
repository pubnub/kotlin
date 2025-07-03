package com.pubnub.internal.v2.entities

import com.pubnub.api.v2.entities.ChannelGroup
import com.pubnub.api.v2.subscriptions.ReceivePresenceEventsImpl
import com.pubnub.api.v2.subscriptions.Subscription
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.internal.v2.subscriptions.SubscriptionImpl
import com.pubnub.kmp.createJsObject

class ChannelGroupImpl(private val jsChannelGroup: dynamic) : ChannelGroup {
    override val name: String
        get() = jsChannelGroup.name

    override fun subscription(options: SubscriptionOptions): Subscription { // TODO: Handle missing filter options
        return SubscriptionImpl(
            jsChannelGroup.subscription(
                createJsObject<PubNub.SubscriptionOptions> {
                    if (options.allOptions.filterIsInstance<ReceivePresenceEventsImpl>().isNotEmpty()) {
                        receivePresenceEvents = true
                    }
                }
            )
        )
    }
}
