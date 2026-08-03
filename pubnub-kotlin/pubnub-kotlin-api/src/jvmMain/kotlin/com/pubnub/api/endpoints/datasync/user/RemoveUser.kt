package com.pubnub.api.endpoints.datasync.user

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.datasync.user.PNRemoveUserResult

/**
 * @see [UserApi.delete]
 */
actual interface RemoveUser : Endpoint<PNRemoveUserResult>
