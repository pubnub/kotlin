package com.pubnub.api.models.consumer.datasync.membership

data class PNDataSyncUpdateMembershipResult(
    val status: Int,
    val data: PNDataSyncMembership,
)
