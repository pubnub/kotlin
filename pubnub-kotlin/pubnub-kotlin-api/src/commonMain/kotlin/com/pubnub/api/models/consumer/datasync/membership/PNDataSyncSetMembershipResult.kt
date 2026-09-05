package com.pubnub.api.models.consumer.datasync.membership

data class PNDataSyncSetMembershipResult(
    val status: Int,
    val data: PNDataSyncMembership,
)
