package com.pubnub.api.endpoints.datasync.user

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.datasync.user.DataSyncGetUsersResult

/**
 * @see [com.pubnub.api.datasync.DataSync.getUsers]
 */
actual interface GetUsers : Endpoint<DataSyncGetUsersResult>
