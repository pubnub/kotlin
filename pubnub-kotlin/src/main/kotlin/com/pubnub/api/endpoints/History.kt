package com.pubnub.api.endpoints

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.mapIdentity
import com.pubnub.api.models.consumer.history.PNHistoryResult
import com.pubnub.internal.DelegatingEndpoint
import com.pubnub.internal.endpoints.History
import com.pubnub.internal.endpoints.IHistory

/**
 * @see [PubNub.history]
 */
class History internal constructor(private val history: History) : DelegatingEndpoint<PNHistoryResult>(), IHistory by history {
    override fun createAction(): Endpoint<PNHistoryResult> = history.mapIdentity()
}
