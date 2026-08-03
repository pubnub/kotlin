package com.pubnub.api.endpoints.datasync.user

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.datasync.user.PNCreateUserResult

/**
 * @see [UserApi.create]
 */
actual interface CreateUser : Endpoint<PNCreateUserResult>
