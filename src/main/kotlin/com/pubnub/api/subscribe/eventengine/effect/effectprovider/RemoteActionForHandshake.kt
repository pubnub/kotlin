package com.pubnub.api.subscribe.eventengine.effect.effectprovider

import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.server.SubscribeEnvelope
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor

internal class RemoteActionForHandshake(
    val remoteAction: RemoteAction<SubscribeEnvelope>
) : RemoteAction<SubscriptionCursor> {
    override fun sync(): SubscriptionCursor? {
        val result: SubscribeEnvelope? = remoteAction.sync()
        return SubscriptionCursor(result?.metadata?.timetoken!!, result.metadata.region)
    }

    override fun silentCancel() {
        remoteAction.silentCancel()
    }

    override fun async(callback: (result: SubscriptionCursor?, status: PNStatus) -> Unit) {
        remoteAction.async { result: SubscribeEnvelope?, status: PNStatus -> // todo name of result so that it not overlap with result from line above
            callback(SubscriptionCursor(result?.metadata?.timetoken!!, result.metadata.region), status)
        }
    }
}
