package com.pubnub.api.endpoints.datasync.membership

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.datasync.membership.PNDataSyncGetMembershipsResult

/**
 * @see [com.pubnub.api.datasync.DataSync.getMemberships]
 */
actual interface GetMemberships : Endpoint<PNDataSyncGetMembershipsResult>
