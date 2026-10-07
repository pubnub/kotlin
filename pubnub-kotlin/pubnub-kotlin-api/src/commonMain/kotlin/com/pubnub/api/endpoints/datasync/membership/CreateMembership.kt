package com.pubnub.api.endpoints.datasync.membership

import com.pubnub.api.models.consumer.datasync.membership.PNDataSyncCreateMembershipResult
import com.pubnub.kmp.PNFuture

/**
 * @see [com.pubnub.api.datasync.DataSync.createMembership]
 */
expect interface CreateMembership : PNFuture<PNDataSyncCreateMembershipResult>
