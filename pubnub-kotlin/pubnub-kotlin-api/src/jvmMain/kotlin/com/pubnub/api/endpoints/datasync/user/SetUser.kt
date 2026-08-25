package com.pubnub.api.endpoints.datasync.user

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.datasync.user.PNSetUserResult

/**
 * @see [com.pubnub.api.datasync.DataSync.setUser]
 */
actual interface SetUser : Endpoint<PNSetUserResult>
