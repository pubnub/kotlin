package com.pubnub.api.endpoints.datasync.membership

import com.pubnub.api.models.consumer.datasync.membership.PNDataSyncGetMembershipsResult
import com.pubnub.kmp.PNFuture

/**
 * @see [com.pubnub.api.datasync.DataSync.getMemberships]
 */
expect interface GetMemberships : PNFuture<PNDataSyncGetMembershipsResult>
