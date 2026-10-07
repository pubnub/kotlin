package com.pubnub.api.endpoints.datasync.user

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.datasync.user.PNDataSyncRemoveUserResult

/**
 * @see [com.pubnub.api.datasync.DataSync.removeUser]
 */
actual interface RemoveUser : Endpoint<PNDataSyncRemoveUserResult>
