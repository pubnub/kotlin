package com.pubnub.api.endpoints.datasync.membership

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.datasync.membership.PNDataSyncRemoveMembershipResult

/**
 * @see [com.pubnub.api.datasync.DataSync.removeMembership]
 */
actual interface RemoveMembership : Endpoint<PNDataSyncRemoveMembershipResult>
