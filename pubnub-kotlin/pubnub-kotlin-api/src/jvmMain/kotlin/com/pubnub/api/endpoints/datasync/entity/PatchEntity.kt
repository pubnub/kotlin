package com.pubnub.api.endpoints.datasync.entity

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.datasync.entity.PNPatchEntityResult

/**
 * @see [com.pubnub.api.datasync.DataSync.patchEntity]
 */
actual interface PatchEntity : Endpoint<PNPatchEntityResult>
