package com.pubnub.api.endpoints.datasync.entity

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.datasync.entity.PNCreateEntityResult

/**
 * @see [EntityApi.create]
 */
actual interface CreateEntity : Endpoint<PNCreateEntityResult>
