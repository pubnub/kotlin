package com.pubnub.api.endpoints.push

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.mapIdentity
import com.pubnub.api.models.consumer.push.PNPushRemoveChannelResult
import com.pubnub.internal.DelegatingEndpoint
import com.pubnub.internal.endpoints.push.IRemoveChannelsFromPush
import com.pubnub.internal.endpoints.push.RemoveChannelsFromPush

/**
 * @see [PubNub.removePushNotificationsFromChannels]
 */
class RemoveChannelsFromPush internal constructor(
    private val removeChannelsFromPush: RemoveChannelsFromPush
) : DelegatingEndpoint<PNPushRemoveChannelResult>(), IRemoveChannelsFromPush by removeChannelsFromPush {
    override fun createAction(): Endpoint<PNPushRemoveChannelResult> = removeChannelsFromPush.mapIdentity()
}
