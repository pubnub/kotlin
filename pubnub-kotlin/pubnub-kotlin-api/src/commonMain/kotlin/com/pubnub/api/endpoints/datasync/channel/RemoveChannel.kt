package com.pubnub.api.endpoints.datasync.channel

import com.pubnub.api.models.consumer.datasync.channel.DataSyncRemoveChannelResult
import com.pubnub.kmp.PNFuture

/**
 * @see [com.pubnub.api.datasync.DataSync.removeChannel]
 */
expect interface RemoveChannel : PNFuture<DataSyncRemoveChannelResult>
