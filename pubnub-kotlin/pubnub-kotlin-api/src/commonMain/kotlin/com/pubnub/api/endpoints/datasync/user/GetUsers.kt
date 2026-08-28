package com.pubnub.api.endpoints.datasync.user

import com.pubnub.api.models.consumer.datasync.user.DataSyncGetUsersResult
import com.pubnub.kmp.PNFuture

/**
 * @see [com.pubnub.api.datasync.DataSync.getUsers]
 */
expect interface GetUsers : PNFuture<DataSyncGetUsersResult>
