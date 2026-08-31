package com.pubnub.api.endpoints.datasync.entity

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.datasync.entity.PNDataSyncSetEntityResult

/**
 * @see [com.pubnub.api.datasync.DataSync.setEntity]
 */
actual interface SetEntity : Endpoint<PNDataSyncSetEntityResult>
