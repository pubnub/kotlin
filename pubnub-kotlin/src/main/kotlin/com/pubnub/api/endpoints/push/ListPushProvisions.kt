package com.pubnub.api.endpoints.push

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.mapIdentity
import com.pubnub.api.models.consumer.push.PNPushListProvisionsResult
import com.pubnub.internal.DelegatingEndpoint
import com.pubnub.internal.endpoints.push.IListPushProvisions
import com.pubnub.internal.endpoints.push.ListPushProvisions

/**
 * @see [PubNub.auditPushChannelProvisions]
 */
class ListPushProvisions internal constructor(private val listPushProvisions: ListPushProvisions) :
    DelegatingEndpoint<PNPushListProvisionsResult>(), IListPushProvisions by listPushProvisions {
    override fun createAction(): Endpoint<PNPushListProvisionsResult> = listPushProvisions.mapIdentity()
}
