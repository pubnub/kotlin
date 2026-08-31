package com.pubnub.api.endpoints.datasync.channel

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.datasync.channel.PNDataSyncGetChannelsResult

/**
 * @see [com.pubnub.api.datasync.DataSync.getChannels]
 */
actual interface GetChannels : Endpoint<PNDataSyncGetChannelsResult>
