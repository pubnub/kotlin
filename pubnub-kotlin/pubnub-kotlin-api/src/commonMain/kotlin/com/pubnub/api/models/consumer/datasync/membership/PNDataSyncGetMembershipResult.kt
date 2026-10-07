package com.pubnub.api.models.consumer.datasync.membership

data class PNDataSyncGetMembershipResult(
    val status: Int,
    val data: PNDataSyncMembership,
)
