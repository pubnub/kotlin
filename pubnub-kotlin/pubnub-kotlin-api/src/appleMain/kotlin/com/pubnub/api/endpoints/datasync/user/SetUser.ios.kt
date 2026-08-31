package com.pubnub.api.endpoints.datasync.user

import com.pubnub.api.models.consumer.datasync.user.PNDataSyncSetUserResult
import com.pubnub.kmp.PNFuture

/**
 * @see [com.pubnub.api.datasync.DataSync.setUser]
 */
actual interface SetUser : PNFuture<PNDataSyncSetUserResult>
