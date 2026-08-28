package com.pubnub.api.endpoints.datasync.user

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.datasync.user.DataSyncSetUserResult

/**
 * @see [com.pubnub.api.datasync.DataSync.setUser]
 */
actual interface SetUser : Endpoint<DataSyncSetUserResult>
