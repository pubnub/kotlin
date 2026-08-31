package com.pubnub.api.endpoints.datasync.user

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.datasync.user.PNDataSyncGetUserResult

/**
 * @see [com.pubnub.api.datasync.DataSync.getUser]
 */
actual interface GetUser : Endpoint<PNDataSyncGetUserResult>
