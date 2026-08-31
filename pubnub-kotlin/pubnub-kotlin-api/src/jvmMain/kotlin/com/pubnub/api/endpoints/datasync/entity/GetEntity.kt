package com.pubnub.api.endpoints.datasync.entity

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.datasync.entity.PNDataSyncGetEntityResult

/**
 * @see [com.pubnub.api.datasync.DataSync.getEntity]
 */
actual interface GetEntity : Endpoint<PNDataSyncGetEntityResult>
