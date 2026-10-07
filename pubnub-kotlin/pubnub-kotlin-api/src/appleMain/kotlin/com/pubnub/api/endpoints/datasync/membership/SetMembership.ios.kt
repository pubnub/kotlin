package com.pubnub.api.endpoints.datasync.membership

import com.pubnub.api.models.consumer.datasync.membership.PNDataSyncSetMembershipResult
import com.pubnub.kmp.PNFuture

/**
 * @see [com.pubnub.api.datasync.DataSync.setMembership]
 */
actual interface SetMembership : PNFuture<PNDataSyncSetMembershipResult>
