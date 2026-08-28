package com.pubnub.api.endpoints.datasync.entity

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.datasync.entity.DataSyncUpdateEntityResult

/**
 * @see [com.pubnub.api.datasync.DataSync.updateEntity]
 */
actual interface UpdateEntity : Endpoint<DataSyncUpdateEntityResult>
