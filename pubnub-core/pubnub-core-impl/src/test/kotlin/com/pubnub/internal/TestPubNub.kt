package com.pubnub.internal

import com.pubnub.internal.callbacks.SubscribeCallback
import com.pubnub.internal.subscribe.eventengine.configuration.EventEnginesConf
import com.pubnub.internal.v2.callbacks.InternalEventListener
import com.pubnub.internal.v2.callbacks.InternalStatusListener
import com.pubnub.internal.v2.entities.BaseChannelGroupImpl
import com.pubnub.internal.v2.entities.BaseChannelImpl
import com.pubnub.internal.v2.entities.BaseChannelMetadataImpl
import com.pubnub.internal.v2.entities.BaseUserMetadataImpl
import com.pubnub.internal.v2.subscription.BaseSubscriptionImpl
import com.pubnub.internal.v2.subscription.BaseSubscriptionSetImpl

class TestPubNub internal constructor(configuration: CorePNConfiguration, eventEnginesConf: EventEnginesConf = EventEnginesConf()) :
    BasePubNubImpl<
        InternalEventListener,
        BaseSubscriptionImpl<InternalEventListener>,
        BaseChannelImpl<InternalEventListener, BaseSubscriptionImpl<InternalEventListener>>,
        BaseChannelGroupImpl<InternalEventListener, BaseSubscriptionImpl<InternalEventListener>>,
        BaseChannelMetadataImpl<InternalEventListener, BaseSubscriptionImpl<InternalEventListener>>,
        BaseUserMetadataImpl<InternalEventListener, BaseSubscriptionImpl<InternalEventListener>>,
        BaseSubscriptionSetImpl<InternalEventListener, BaseSubscriptionImpl<InternalEventListener>>,
        InternalStatusListener,
        >(configuration, eventEnginesConf) {
        override fun channel(name: String): BaseChannelImpl<InternalEventListener, BaseSubscriptionImpl<InternalEventListener>> {
            TODO("Not yet implemented")
        }

        override fun channelGroup(name: String): BaseChannelGroupImpl<InternalEventListener, BaseSubscriptionImpl<InternalEventListener>> {
            TODO("Not yet implemented")
        }

        override fun channelMetadata(
            id: String,
        ): BaseChannelMetadataImpl<InternalEventListener, BaseSubscriptionImpl<InternalEventListener>> {
            TODO("Not yet implemented")
        }

        override fun userMetadata(id: String): BaseUserMetadataImpl<InternalEventListener, BaseSubscriptionImpl<InternalEventListener>> {
            TODO("Not yet implemented")
        }

        /**
         * Cancel any subscribe and heartbeat loops or ongoing re-connections.
         *
         * Monitor the results in [SubscribeCallback.status]
         */
        override fun disconnect() {
            TODO("Not yet implemented")
        }

        override fun subscriptionSetOf(
            subscriptions: Set<BaseSubscriptionImpl<InternalEventListener>>,
        ): BaseSubscriptionSetImpl<InternalEventListener, BaseSubscriptionImpl<InternalEventListener>> {
            TODO("Not yet implemented")
        }

        fun addListener(listener: SubscribeCallback) {
            listenerManager.addListener(listener)
        }

        /**
         * Add a listener.
         *
         * @param listener The listener to be added.
         */
        override fun addListener(listener: InternalEventListener) {
            listenerManager.addListener(listener)
        }

        /**
         * Add a listener.
         *
         * @param listener The listener to be added.
         */
        override fun addListener(listener: InternalStatusListener) {
            listenerManager.addListener(listener)
        }
    }
