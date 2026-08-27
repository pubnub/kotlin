package com.pubnub.api.endpoints.datasync.channel

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.datasync.channel.DataSyncCreateChannelResult

/**
 * @see [com.pubnub.api.datasync.DataSync.createChannel]
 */
actual interface CreateChannel : Endpoint<DataSyncCreateChannelResult>
