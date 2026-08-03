package com.pubnub.api.endpoints.datasync.user

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.datasync.user.PNPatchUserResult

/**
 * @see [UserApi.patch]
 */
actual interface PatchUser : Endpoint<PNPatchUserResult>
