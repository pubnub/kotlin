package com.pubnub.api.endpoints.datasync.user

import com.pubnub.api.models.consumer.datasync.user.DataSyncUpdateUserResult
import com.pubnub.kmp.PNFuture

/**
 * @see [com.pubnub.api.datasync.DataSync.updateUser]
 */
expect interface UpdateUser : PNFuture<DataSyncUpdateUserResult>
