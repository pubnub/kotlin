package com.pubnub.api.endpoints.datasync.entity

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.datasync.entity.PNUpdateEntityResult

/**
 * @see [com.pubnub.api.datasync.DataSync.updateEntity]
 */
actual interface UpdateEntity : Endpoint<PNUpdateEntityResult>
