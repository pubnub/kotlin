package com.pubnub.api.endpoints.datasync.user

import com.pubnub.api.models.consumer.datasync.user.DataSyncGetUserResult
import com.pubnub.kmp.PNFuture

/**
 * @see [com.pubnub.api.datasync.DataSync.getUser]
 */
actual interface GetUser : PNFuture<DataSyncGetUserResult>
