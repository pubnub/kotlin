package com.pubnub.api.endpoints.datasync.channel

import com.pubnub.api.models.consumer.datasync.channel.PNDataSyncGetChannelResult
import com.pubnub.kmp.PNFuture

/**
 * @see [com.pubnub.api.datasync.DataSync.getChannel]
 */
expect interface GetChannel : PNFuture<PNDataSyncGetChannelResult>
