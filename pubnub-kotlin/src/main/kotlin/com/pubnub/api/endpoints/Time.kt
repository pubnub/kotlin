package com.pubnub.api.endpoints

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.mapIdentity
import com.pubnub.api.models.consumer.PNTimeResult
import com.pubnub.internal.DelegatingEndpoint
import com.pubnub.internal.endpoints.ITime
import com.pubnub.internal.endpoints.Time

/**
 * @see [PubNub.time]
 */
class Time internal constructor(private val time: Time) : DelegatingEndpoint<PNTimeResult>(), ITime by time {
    override fun createAction(): Endpoint<PNTimeResult> = time.mapIdentity()
}
