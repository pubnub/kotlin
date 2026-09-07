package com.pubnub.api.models.consumer.datasync.membership

data class PNDataSyncCreateMembershipResult(
    val status: Int,
    val data: PNDataSyncMembership,
)
