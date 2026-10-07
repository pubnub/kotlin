package com.pubnub.api.endpoints.datasync.user

import com.pubnub.api.models.consumer.datasync.user.PNDataSyncRemoveUserResult
import com.pubnub.kmp.PNFuture

/**
 * @see [com.pubnub.api.datasync.DataSync.removeUser]
 */
expect interface RemoveUser : PNFuture<PNDataSyncRemoveUserResult>
