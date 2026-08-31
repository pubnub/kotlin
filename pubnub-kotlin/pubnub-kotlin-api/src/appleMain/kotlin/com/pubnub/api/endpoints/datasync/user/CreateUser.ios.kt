package com.pubnub.api.endpoints.datasync.user

import com.pubnub.api.models.consumer.datasync.user.PNDataSyncCreateUserResult
import com.pubnub.kmp.PNFuture

/**
 * @see [com.pubnub.api.datasync.DataSync.createUser]
 */
actual interface CreateUser : PNFuture<PNDataSyncCreateUserResult>
