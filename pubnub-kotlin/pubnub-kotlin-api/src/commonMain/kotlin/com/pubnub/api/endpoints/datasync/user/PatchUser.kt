package com.pubnub.api.endpoints.datasync.user

import com.pubnub.api.models.consumer.datasync.user.PNPatchUserResult
import com.pubnub.kmp.PNFuture

/**
 * @see [UserApi.patch]
 */
expect interface PatchUser : PNFuture<PNPatchUserResult>
