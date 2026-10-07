package com.pubnub.api.endpoints.datasync.membership

import com.pubnub.api.models.consumer.datasync.membership.PNDataSyncUpdateMembershipResult
import com.pubnub.kmp.PNFuture

/**
 * @see [com.pubnub.api.datasync.DataSync.updateMembership]
 */
expect interface UpdateMembership : PNFuture<PNDataSyncUpdateMembershipResult>
