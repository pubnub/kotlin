package com.pubnub.api.endpoints.datasync.user

import com.pubnub.api.models.consumer.datasync.user.DataSyncCreateUserResult
import com.pubnub.kmp.PNFuture

/**
 * @see [com.pubnub.api.datasync.DataSync.createUser]
 */
expect interface CreateUser : PNFuture<DataSyncCreateUserResult>
