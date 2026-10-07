package com.pubnub.api.endpoints.datasync.membership

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.datasync.membership.PNDataSyncSetMembershipResult

/**
 * @see [com.pubnub.api.datasync.DataSync.setMembership]
 */
actual interface SetMembership : Endpoint<PNDataSyncSetMembershipResult>
