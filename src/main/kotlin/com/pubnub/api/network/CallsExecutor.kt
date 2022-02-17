package com.pubnub.api.network

import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.Cancelable
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.server.SubscribeEnvelope

class HttpCall(
    val id: String,
    val cancelable: Cancelable
)

internal class CallsExecutor(
    val pubNub: PubNub,
    private val calls: MutableMap<String, HttpCall> = mutableMapOf()
) {

    fun handshake(
        id: String,
        channels: Collection<String>,
        channelGroups: Collection<String>,
        callback: (result: SubscribeEnvelope?, status: PNStatus) -> Unit
    ): HttpCall {
        return HttpCall(
            id = id,
            cancelable = pubNub.handshake(
                channels = channels.toList(),
                channelGroups = channelGroups.toList(),
                callback = { r, s ->
                    synchronized(calls) {
                        calls.remove(id)
                    }
                    callback(r, s)

                }
            )
        )
    }

    fun receiveMessages(
        id: String,
        channels: Collection<String>,
        channelGroups: Collection<String>,
        timetoken: Long,
        region: String,
        callback: (result: SubscribeEnvelope?, status: PNStatus) -> Unit
    ): HttpCall {
        return HttpCall(
            id = id,
            cancelable = pubNub.receiveMessages(
                channels = channels.toList(),
                channelGroups = channelGroups.toList(),
                timetoken = timetoken,
                region = region,
                callback = { r, s ->
                    synchronized(calls) {
                        calls.remove(id)
                    }
                    callback(r, s)
                }
            )
        )
    }
}