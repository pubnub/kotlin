package com.pubnub.api.subscribe

import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.enums.PNReconnectionPolicy
import com.pubnub.api.managers.ListenerManager
import com.pubnub.api.models.server.SubscribeEnvelope
import com.pubnub.api.subscribe.internal.Config
import com.pubnub.api.subscribe.internal.IncomingPayloadProcessor
import com.pubnub.api.subscribe.internal.InternalSubscribeModule
import com.pubnub.api.subscribe.internal.PN
import com.pubnub.api.subscribe.internal.handshake
import com.pubnub.api.subscribe.internal.receiveEvents

interface NewSubscribeModule {
    companion object {
        internal fun create(
            pubnub: PubNub,
            listenerManager: ListenerManager,
            incomingPayloadProcessor: IncomingPayloadProcessor
        ): NewSubscribeModule {

            val pn = object : PN {
                override fun handshake(
                    channels: List<String>,
                    channelGroups: List<String>
                ): RemoteAction<SubscribeEnvelope> = pubnub.handshake(channels, channelGroups)

                override fun receiveEvents(
                    channels: List<String>,
                    channelGroups: List<String>,
                    timetoken: Long,
                    region: String
                ): RemoteAction<SubscribeEnvelope> = pubnub.receiveEvents(
                    channels = channels,
                    channelGroups = channelGroups,
                    timetoken = timetoken,
                    region = region
                )

                override val configuration: Config = object : Config {
                    override val reconnectionPolicy: PNReconnectionPolicy = pubnub.configuration.reconnectionPolicy
                    override val maximumReconnectionRetries: Int = pubnub.configuration.maximumReconnectionRetries
                }
            }

            return InternalSubscribeModule.create(
                pubnub = pn,
                listenerManager = listenerManager,
                incomingPayloadProcessor = incomingPayloadProcessor
            )
        }
    }

    fun subscribe(
        channels: List<String>,
        channelGroups: List<String>,
        withPresence: Boolean,
        withTimetoken: Long
    )

    fun unsubscribe(
        channels: List<String> = emptyList(),
        channelGroups: List<String> = emptyList()
    )

    fun unsubscribeAll()
    fun getSubscribedChannels(): List<String>
    fun getSubscribedChannelGroups(): List<String>
    fun cancel()
}
