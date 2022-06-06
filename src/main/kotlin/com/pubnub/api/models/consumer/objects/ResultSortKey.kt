package com.pubnub.api.models.consumer.objects

interface SortableKey

sealed class ResultSortKey<T : SortableKey>(
    val key: T,
    internal val dir: String = "asc"
) {
    class Asc<T : SortableKey>(key: T) : ResultSortKey<T>(key = key, dir = "asc")
    class Desc<T : SortableKey>(key: T) : ResultSortKey<T>(key = key, dir = "desc")

    fun toSortParameter(): String {
        return dir
    }
}
enum class ResultKey() : SortableKey {
    ID(), NAME(), UPDATED();

    fun toPNSortKey(): PNKey {
        return when (this) {
            ID -> PNKey.ID
            NAME -> PNKey.NAME
            UPDATED -> PNKey.UPDATED
        }
    }
}

enum class SpaceMembershipResultKey() : SortableKey {
    USER_ID(), USER_NAME(), USER_UPDATED(), UPDATED();

    fun toPNMemberKey(): PNMemberKey {
        return when (this) {
            USER_ID -> PNMemberKey.UUID_ID
            USER_NAME -> PNMemberKey.UUID_NAME
            USER_UPDATED -> PNMemberKey.UUID_UPDATED
            UPDATED -> PNMemberKey.UPDATED
        }
    }
}

enum class UserMembershipsResultKey() : SortableKey {
    SPACE_ID(), SPACE_NAME(), SPACE_UPDATED(), UPDATED();

    fun toPNMembershipKey(): PNMembershipKey {
        return when (this) {
            SPACE_ID -> PNMembershipKey.CHANNEL_ID
            SPACE_NAME -> PNMembershipKey.CHANNEL_NAME
            SPACE_UPDATED -> PNMembershipKey.CHANNEL_UPDATED
            UPDATED -> PNMembershipKey.UPDATED
        }
    }
}
