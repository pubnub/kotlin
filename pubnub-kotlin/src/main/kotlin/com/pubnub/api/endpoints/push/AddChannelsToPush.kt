package com.pubnub.api.endpoints.push

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.mapIdentity
import com.pubnub.api.models.consumer.push.PNPushAddChannelResult
import com.pubnub.internal.DelegatingEndpoint
import com.pubnub.internal.endpoints.push.AddChannelsToPush
import com.pubnub.internal.endpoints.push.IAddChannelsToPush

/**
 * @see [PubNub.addPushNotificationsOnChannels]
 */
class AddChannelsToPush internal constructor(private val addChannelsToPush: AddChannelsToPush) :
    DelegatingEndpoint<PNPushAddChannelResult>(),
    IAddChannelsToPush by addChannelsToPush {
    override fun createAction(): Endpoint<PNPushAddChannelResult> = addChannelsToPush.mapIdentity()
}
