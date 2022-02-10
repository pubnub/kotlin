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
        channels: List<String>,
        channelGroups: List<String>,
        callback: (result: SubscribeEnvelope?, status: PNStatus) -> Unit
    ) {
        val call = HttpCall(
            id = id,
            cancelable = pubNub.handshake(
                channels = channels,
                channelGroups = channelGroups,
                callback = { r, s ->
                    synchronized(calls) {
                        calls.remove(id)
                    }
                    callback(r, s)

                }
            )
        )

        synchronized(calls) {
            calls[id] = call
        }
    }

    fun receiveMessages(
        id: String,
        channels: List<String>,
        channelGroups: List<String>,
        timetoken: Long,
        region: String,
        callback: (result: SubscribeEnvelope?, status: PNStatus) -> Unit
    ) {
        val call = HttpCall(
            id = id,
            cancelable = pubNub.receiveMessages(
                channels = channels,
                channelGroups = channelGroups,
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

        synchronized(calls) {
            calls[id] = call
        }

    }

    fun cancel(id: String) {
        val call = synchronized(calls) {
            calls.remove(id)
        }

        call?.cancelable?.silentCancel()
    }


}