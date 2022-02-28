package com.pubnub.api.subscribe.internal

import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.pubsub.Subscribe
import com.pubnub.api.endpoints.remoteaction.Cancelable
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.server.SubscribeEnvelope

internal fun PubNub.handshake(
    channels: List<String>,
    channelGroups: List<String>,
    callback: (result: SubscribeEnvelope?, status: PNStatus) -> Unit
): Cancelable {
    return Subscribe(this).also {
        it.channels = channels
        it.channelGroups = channelGroups
        it.timetoken = 0
        it.async(callback)
    }
}

internal fun PubNub.receiveMessages(
    channels: List<String>,
    channelGroups: List<String>,
    timetoken: Long,
    region: String,
    callback: (result: SubscribeEnvelope?, status: PNStatus) -> Unit
): Cancelable {
    return Subscribe(this).also {
        it.channels = channels
        it.channelGroups = channelGroups
        it.timetoken = timetoken
        it.region = region
        it.filterExpression = this.configuration.filterExpression.ifBlank { null }
        it.async(callback)
    }
}
