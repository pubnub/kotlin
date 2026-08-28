package com.pubnub.api.endpoints.datasync.user

import com.pubnub.api.models.consumer.datasync.user.DataSyncSetUserResult
import com.pubnub.kmp.PNFuture

/**
 * @see [com.pubnub.api.datasync.DataSync.setUser]
 */
expect interface SetUser : PNFuture<DataSyncSetUserResult>
