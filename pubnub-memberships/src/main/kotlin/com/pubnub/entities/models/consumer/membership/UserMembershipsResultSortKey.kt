package com.pubnub.entities.models.consumer.membership

import com.pubnub.api.models.consumer.objects.PNMembershipKey

sealed class UserMembershipsResultSortKey(
    internal val key: UserMembershipsResultKey,
    internal val dir: String = "asc"
) {
    class Asc(key: UserMembershipsResultKey) : UserMembershipsResultSortKey(key = key, dir = "asc")
    class Desc(key: UserMembershipsResultKey) : UserMembershipsResultSortKey(key = key, dir = "desc")

    fun toSortParameter(): String {
        return key.fieldName + ":" + dir
    }
}

enum class UserMembershipsResultKey(internal val fieldName: String) {
    SPACE_ID("space.id"), SPACE_NAME("space.name"), SPACE_UPDATED("space.updated"), UPDATED("updated");

    fun toPNMembershipKey(): PNMembershipKey {
        return when (this) {
            SPACE_ID -> PNMembershipKey.CHANNEL_ID
            SPACE_NAME -> PNMembershipKey.CHANNEL_NAME
            SPACE_UPDATED -> PNMembershipKey.CHANNEL_UPDATED
            UPDATED -> PNMembershipKey.UPDATED
        }
    }
}
