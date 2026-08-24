package com.pubnub.api.endpoints.datasync.entity

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.datasync.entity.PNCreateEntityResult

/**
 * @see [com.pubnub.api.datasync.DataSync.createEntity]
 */
actual interface CreateEntity : Endpoint<PNCreateEntityResult>
