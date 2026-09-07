package com.pubnub.api.endpoints.datasync.membership

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.datasync.membership.PNDataSyncGetMembershipResult

/**
 * @see [com.pubnub.api.datasync.DataSync.getMembership]
 */
actual interface GetMembership : Endpoint<PNDataSyncGetMembershipResult>
