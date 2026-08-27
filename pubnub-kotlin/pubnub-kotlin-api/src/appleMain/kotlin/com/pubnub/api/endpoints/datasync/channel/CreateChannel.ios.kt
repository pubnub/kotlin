package com.pubnub.api.endpoints.datasync.channel

import com.pubnub.api.models.consumer.datasync.channel.DataSyncCreateChannelResult
import com.pubnub.kmp.PNFuture

/**
 * @see [com.pubnub.api.datasync.DataSync.createChannel]
 */
actual interface CreateChannel : PNFuture<DataSyncCreateChannelResult>
