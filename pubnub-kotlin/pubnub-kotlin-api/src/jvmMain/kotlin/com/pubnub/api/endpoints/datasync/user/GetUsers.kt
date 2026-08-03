package com.pubnub.api.endpoints.datasync.user

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.datasync.user.PNGetUsersResult

/**
 * @see [UserApi.getAll]
 */
actual interface GetUsers : Endpoint<PNGetUsersResult>
