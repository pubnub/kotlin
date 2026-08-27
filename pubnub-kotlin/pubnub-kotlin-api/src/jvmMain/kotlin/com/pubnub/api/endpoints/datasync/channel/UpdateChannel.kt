package com.pubnub.api.endpoints.datasync.channel

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.datasync.channel.DataSyncUpdateChannelResult

/**
 * @see [com.pubnub.api.datasync.DataSync.updateChannel]
 */
actual interface UpdateChannel : Endpoint<DataSyncUpdateChannelResult>
