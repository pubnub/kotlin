package com.pubnub.api.endpoints.datasync.entity

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.datasync.entity.DataSyncGetEntitiesResult

/**
 * @see [com.pubnub.api.datasync.DataSync.getEntities]
 */
actual interface GetEntities : Endpoint<DataSyncGetEntitiesResult>
