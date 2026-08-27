package com.pubnub.api.endpoints.datasync.channel

import com.pubnub.api.models.consumer.datasync.channel.DataSyncGetChannelResult
import com.pubnub.kmp.PNFuture

/**
 * @see [com.pubnub.api.datasync.DataSync.getChannel]
 */
actual interface GetChannel : PNFuture<DataSyncGetChannelResult>
