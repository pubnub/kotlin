package com.pubnub.api.endpoints.datasync.entity

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.datasync.entity.PNGetEntityResult

/**
 * @see [EntityApi.get]
 */
actual interface GetEntity : Endpoint<PNGetEntityResult>
