package com.pubnub.api.models.consumer.objects

open class PNMembershipSortKey(
    val key: PNMembershipKey,
    val dir: String = "asc"
) : SortBase() {
    class PNAsc(key: PNMembershipKey) : PNMembershipSortKey(key = key, dir = "asc")
    class PNDesc(key: PNMembershipKey) : PNMembershipSortKey(key = key, dir = "desc")

    override fun toSortParameter(): String {
        return key.fieldName + ":" + dir
    }
}

enum class PNMembershipKey(internal val fieldName: String) {
    CHANNEL_ID("channel.id"), CHANNEL_NAME("channel.name"), CHANNEL_UPDATED("channel.updated"), UPDATED("updated");
}
