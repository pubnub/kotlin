package com.pubnub.api.models.consumer.objects

sealed class ResultSortKey<T : SortField>(
    val key: T,
    internal val dir: String = "asc"
) {
    class Asc<T : SortField>(key: T) : ResultSortKey<T>(key = key, dir = "asc")
    class Desc<T : SortField>(key: T) : ResultSortKey<T>(key = key, dir = "desc")

    fun toSortParameter(): String {
        return key.fieldName + ":" + dir
    }
}
enum class ResultKey(override val fieldName: String) : SortField {
    ID("id"), NAME("name"), UPDATED("updated");

    fun toPNSortKey(): PNKey {
        return when (this) {
            ID -> PNKey.ID
            NAME -> PNKey.NAME
            UPDATED -> PNKey.UPDATED
        }
    }
}

enum class SpaceMembershipResultKey(override val fieldName: String) : SortField {
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

enum class UserMembershipsResultKey(override val fieldName: String) : SortField {
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
