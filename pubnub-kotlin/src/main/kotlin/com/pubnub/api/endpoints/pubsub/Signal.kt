package com.pubnub.api.endpoints.pubsub

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.mapIdentity
import com.pubnub.api.models.consumer.PNPublishResult
import com.pubnub.internal.DelegatingEndpoint
import com.pubnub.internal.endpoints.pubsub.ISignal
import com.pubnub.internal.endpoints.pubsub.Signal

/**
 * @see [PubNub.signal]
 */
class Signal internal constructor(private val signal: Signal) : DelegatingEndpoint<PNPublishResult>(), ISignal by signal {
    override fun createAction(): Endpoint<PNPublishResult> = signal.mapIdentity()
}
