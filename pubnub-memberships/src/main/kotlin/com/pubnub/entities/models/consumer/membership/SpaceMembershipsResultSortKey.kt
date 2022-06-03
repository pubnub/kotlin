package com.pubnub.entities.models.consumer.membership

import com.pubnub.api.models.consumer.objects.PNMemberKey

sealed class SpaceMembershipsResultSortKey(
    internal val key: SpaceMembershipResultKey,
    internal val dir: String = "asc"
) {
    class Asc(key: SpaceMembershipResultKey) : SpaceMembershipsResultSortKey(key = key, dir = "asc")
    class Desc(key: SpaceMembershipResultKey) : SpaceMembershipsResultSortKey(key = key, dir = "desc")

    fun toSortParameter(): String {
        return key.fieldName + ":" + dir
    }
}

enum class SpaceMembershipResultKey(internal val fieldName: String) {
    USER_ID("user.id"), USER_NAME("user.name"), USER_UPDATED("user.updated"), UPDATED("updated");

    fun toPNMemberKey(): PNMemberKey {
        return when (this) {
            USER_ID -> PNMemberKey.UUID_ID
            USER_NAME -> PNMemberKey.UUID_NAME
            USER_UPDATED -> PNMemberKey.UUID_UPDATED
            UPDATED -> PNMemberKey.UPDATED
        }
    }
}
