package com.pubnub.api.endpoints.datasync.channel

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.datasync.channel.DataSyncRemoveChannelResult

/**
 * @see [com.pubnub.api.datasync.DataSync.removeChannel]
 */
actual interface RemoveChannel : Endpoint<DataSyncRemoveChannelResult>
