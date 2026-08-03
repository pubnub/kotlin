package com.pubnub.api.endpoints.datasync.user

import com.pubnub.api.models.consumer.datasync.user.PNGetUsersResult
import com.pubnub.kmp.PNFuture

/**
 * @see [UserApi.getAll]
 */
expect interface GetUsers : PNFuture<PNGetUsersResult>
