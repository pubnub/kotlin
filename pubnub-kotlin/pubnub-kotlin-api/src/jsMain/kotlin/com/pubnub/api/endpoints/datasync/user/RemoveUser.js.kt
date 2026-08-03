package com.pubnub.api.endpoints.datasync.user

import com.pubnub.api.models.consumer.datasync.user.PNRemoveUserResult
import com.pubnub.kmp.PNFuture

/**
 * @see [UserApi.delete]
 */
actual interface RemoveUser : PNFuture<PNRemoveUserResult>
