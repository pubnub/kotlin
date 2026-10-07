package com.pubnub.api.endpoints.datasync.membership

import com.pubnub.api.models.consumer.datasync.membership.PNDataSyncRemoveMembershipResult
import com.pubnub.kmp.PNFuture

/**
 * @see [com.pubnub.api.datasync.DataSync.removeMembership]
 */
expect interface RemoveMembership : PNFuture<PNDataSyncRemoveMembershipResult>
