package com.pubnub.api.endpoints.datasync.entity

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.datasync.entity.DataSyncGetEntityResult

/**
 * @see [com.pubnub.api.datasync.DataSync.getEntity]
 */
actual interface GetEntity : Endpoint<DataSyncGetEntityResult>
