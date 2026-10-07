package com.pubnub.api.endpoints.datasync.channel

import com.pubnub.api.models.consumer.datasync.channel.PNDataSyncSetChannelResult
import com.pubnub.kmp.PNFuture

/**
 * @see [com.pubnub.api.datasync.DataSync.setChannel]
 */
expect interface SetChannel : PNFuture<PNDataSyncSetChannelResult>
