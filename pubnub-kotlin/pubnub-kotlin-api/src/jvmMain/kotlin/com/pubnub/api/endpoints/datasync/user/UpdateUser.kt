package com.pubnub.api.endpoints.datasync.user

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.datasync.user.PNDataSyncUpdateUserResult

/**
 * @see [com.pubnub.api.datasync.DataSync.updateUser]
 */
actual interface UpdateUser : Endpoint<PNDataSyncUpdateUserResult>
