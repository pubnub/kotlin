package com.pubnub.api.subscribe.internal

import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.pubsub.Subscribe
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.enums.PNReconnectionPolicy
import com.pubnub.api.models.server.SubscribeEnvelope

interface PN {
    fun handshake(
        channels: List<String>,
        channelGroups: List<String>
    ): RemoteAction<SubscribeEnvelope>

    fun receiveEvents(
        channels: List<String>,
        channelGroups: List<String>,
        timetoken: Long,
        region: String
    ): RemoteAction<SubscribeEnvelope>

    val configuration: Config
}

interface Config {
    val reconnectionPolicy: PNReconnectionPolicy

    val maximumReconnectionRetries: Int
}

internal fun PubNub.handshake(
    channels: List<String>,
    channelGroups: List<String>
): RemoteAction<SubscribeEnvelope> {
    return Subscribe(this).also {
        it.channels = channels
        it.channelGroups = channelGroups
        it.timetoken = 0
    }
}

internal fun PubNub.receiveEvents(
    channels: List<String>,
    channelGroups: List<String>,
    timetoken: Long,
    region: String
): RemoteAction<SubscribeEnvelope> {
    return Subscribe(this).also {
        it.channels = channels
        it.channelGroups = channelGroups
        it.timetoken = timetoken
        it.region = region
        it.filterExpression = this.configuration.filterExpression.ifBlank { null }
    }
}
