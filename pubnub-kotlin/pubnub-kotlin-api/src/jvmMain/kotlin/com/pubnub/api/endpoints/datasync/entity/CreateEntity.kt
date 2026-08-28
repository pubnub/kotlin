package com.pubnub.api.endpoints.datasync.entity

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.datasync.entity.DataSyncCreateEntityResult

/**
 * @see [com.pubnub.api.datasync.DataSync.createEntity]
 */
actual interface CreateEntity : Endpoint<DataSyncCreateEntityResult>
