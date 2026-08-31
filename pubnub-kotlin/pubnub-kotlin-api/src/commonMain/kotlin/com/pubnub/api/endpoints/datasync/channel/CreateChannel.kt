package com.pubnub.api.endpoints.datasync.channel

import com.pubnub.api.models.consumer.datasync.channel.PNDataSyncCreateChannelResult
import com.pubnub.kmp.PNFuture

/**
 * @see [com.pubnub.api.datasync.DataSync.createChannel]
 */
expect interface CreateChannel : PNFuture<PNDataSyncCreateChannelResult>
