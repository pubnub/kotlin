package com.pubnub.api.endpoints.datasync.channel

import com.pubnub.api.models.consumer.datasync.channel.DataSyncSetChannelResult
import com.pubnub.kmp.PNFuture

/**
 * @see [com.pubnub.api.datasync.DataSync.setChannel]
 */
actual interface SetChannel : PNFuture<DataSyncSetChannelResult>
