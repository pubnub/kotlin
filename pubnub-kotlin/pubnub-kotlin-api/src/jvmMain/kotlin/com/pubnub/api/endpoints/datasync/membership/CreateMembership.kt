package com.pubnub.api.endpoints.datasync.membership

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.datasync.membership.PNDataSyncCreateMembershipResult

/**
 * @see [com.pubnub.api.datasync.DataSync.createMembership]
 */
actual interface CreateMembership : Endpoint<PNDataSyncCreateMembershipResult>
