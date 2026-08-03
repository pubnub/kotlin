package com.pubnub.api.endpoints.datasync.user

import com.pubnub.api.models.consumer.datasync.user.PNUpdateUserResult
import com.pubnub.kmp.PNFuture

/**
 * @see [UserApi.update]
 */
actual interface UpdateUser : PNFuture<PNUpdateUserResult>
