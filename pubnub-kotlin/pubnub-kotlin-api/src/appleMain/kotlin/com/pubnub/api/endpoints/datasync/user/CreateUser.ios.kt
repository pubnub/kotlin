package com.pubnub.api.endpoints.datasync.user

import com.pubnub.api.models.consumer.datasync.user.PNCreateUserResult
import com.pubnub.kmp.PNFuture

/**
 * @see [UserApi.create]
 */
actual interface CreateUser : PNFuture<PNCreateUserResult>
