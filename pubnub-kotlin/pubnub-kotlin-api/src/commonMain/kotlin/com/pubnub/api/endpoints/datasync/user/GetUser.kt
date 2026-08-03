package com.pubnub.api.endpoints.datasync.user

import com.pubnub.api.models.consumer.datasync.user.PNGetUserResult
import com.pubnub.kmp.PNFuture

/**
 * @see [UserApi.get]
 */
expect interface GetUser : PNFuture<PNGetUserResult>
