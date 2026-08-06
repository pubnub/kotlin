package com.pubnub.api.endpoints.datasync.user

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.datasync.user.PNPatchUserResult

/**
 * @see [com.pubnub.api.datasync.DataSync.patchUser]
 */
actual interface PatchUser : Endpoint<PNPatchUserResult>
