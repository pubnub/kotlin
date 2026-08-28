package com.pubnub.api.endpoints.datasync.user

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.datasync.user.DataSyncUpdateUserResult

/**
 * @see [com.pubnub.api.datasync.DataSync.updateUser]
 */
actual interface UpdateUser : Endpoint<DataSyncUpdateUserResult>
