package com.pubnub.api.endpoints.datasync.channel

import com.pubnub.api.models.consumer.datasync.channel.PNDataSyncGetChannelsResult
import com.pubnub.kmp.PNFuture

/**
 * @see [com.pubnub.api.datasync.DataSync.getChannels]
 */
expect interface GetChannels : PNFuture<PNDataSyncGetChannelsResult>
