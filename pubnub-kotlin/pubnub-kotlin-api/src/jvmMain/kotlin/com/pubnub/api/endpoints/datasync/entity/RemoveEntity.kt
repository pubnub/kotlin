package com.pubnub.api.endpoints.datasync.entity

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.datasync.entity.PNRemoveEntityResult

/**
 * @see [EntityApi.delete]
 */
actual interface RemoveEntity : Endpoint<PNRemoveEntityResult>
