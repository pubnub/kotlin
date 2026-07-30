package com.pubnub.api.endpoints.datasync.entity

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.datasync.entity.PNUpdateEntityResult

/**
 * @see [EntityApi.update]
 */
actual interface UpdateEntity : Endpoint<PNUpdateEntityResult>
