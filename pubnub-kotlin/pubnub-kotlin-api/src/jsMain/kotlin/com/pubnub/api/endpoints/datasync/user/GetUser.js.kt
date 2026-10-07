package com.pubnub.api.endpoints.datasync.user

import com.pubnub.api.models.consumer.datasync.user.PNDataSyncGetUserResult
import com.pubnub.kmp.PNFuture

/**
 * @see [com.pubnub.api.datasync.DataSync.getUser]
 */
actual interface GetUser : PNFuture<PNDataSyncGetUserResult>
