package com.pubnub.api.endpoints.datasync.user

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.datasync.user.PNUpdateUserResult

/**
 * @see [UserApi.update]
 */
actual interface UpdateUser : Endpoint<PNUpdateUserResult>
