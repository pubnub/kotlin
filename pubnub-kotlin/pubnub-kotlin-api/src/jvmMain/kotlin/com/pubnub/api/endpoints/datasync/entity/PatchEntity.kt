package com.pubnub.api.endpoints.datasync.entity

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.datasync.entity.PNPatchEntityResult

/**
 * @see [EntityApi.patch]
 */
actual interface PatchEntity : Endpoint<PNPatchEntityResult>
