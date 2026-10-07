package com.pubnub.api.endpoints.datasync.user

import com.pubnub.api.models.consumer.datasync.user.PNDataSyncGetUsersResult
import com.pubnub.kmp.PNFuture

/**
 * @see [com.pubnub.api.datasync.DataSync.getUsers]
 */
actual interface GetUsers : PNFuture<PNDataSyncGetUsersResult>
