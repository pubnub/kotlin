package com.pubnub.api.endpoints.datasync.channel

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.datasync.channel.PNDataSyncSetChannelResult

/**
 * @see [com.pubnub.api.datasync.DataSync.setChannel]
 */
actual interface SetChannel : Endpoint<PNDataSyncSetChannelResult>
