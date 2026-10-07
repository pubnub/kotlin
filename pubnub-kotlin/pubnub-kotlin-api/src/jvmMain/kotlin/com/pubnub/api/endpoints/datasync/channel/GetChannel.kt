package com.pubnub.api.endpoints.datasync.channel

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.datasync.channel.PNDataSyncGetChannelResult

/**
 * @see [com.pubnub.api.datasync.DataSync.getChannel]
 */
actual interface GetChannel : Endpoint<PNDataSyncGetChannelResult>
