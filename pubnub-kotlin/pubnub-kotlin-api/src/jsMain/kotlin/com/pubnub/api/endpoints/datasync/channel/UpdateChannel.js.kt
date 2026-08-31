package com.pubnub.api.endpoints.datasync.channel

import com.pubnub.api.models.consumer.datasync.channel.PNDataSyncUpdateChannelResult
import com.pubnub.kmp.PNFuture

/**
 * @see [com.pubnub.api.datasync.DataSync.updateChannel]
 */
actual interface UpdateChannel : PNFuture<PNDataSyncUpdateChannelResult>
