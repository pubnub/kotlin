package com.pubnub.api.endpoints.datasync.membership

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.datasync.membership.PNDataSyncUpdateMembershipResult

/**
 * @see [com.pubnub.api.datasync.DataSync.updateMembership]
 */
actual interface UpdateMembership : Endpoint<PNDataSyncUpdateMembershipResult>
