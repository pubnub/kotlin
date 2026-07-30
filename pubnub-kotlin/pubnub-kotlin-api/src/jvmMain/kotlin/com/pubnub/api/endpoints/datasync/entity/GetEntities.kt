package com.pubnub.api.endpoints.datasync.entity

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.datasync.entity.PNGetEntitiesResult

/**
 * @see [EntityApi.getAll]
 */
actual interface GetEntities : Endpoint<PNGetEntitiesResult>
