package com.pubnub.api.models.consumer.objects

open class PNMemberSortKey(
    internal val key: PNMemberKey,
    internal val dir: String = "asc"
) : SortBase() {
    class PNAsc(key: PNMemberKey) : PNMemberSortKey(key = key, dir = "asc")
    class PNDesc(key: PNMemberKey) : PNMemberSortKey(key = key, dir = "desc")

    override fun toSortParameter(): String {
        return key.fieldName + ":" + dir
    }
}

enum class PNMemberKey(internal val fieldName: String) {
    UUID_ID("uuid.id"), UUID_NAME("uuid.name"), UUID_UPDATED("uuid.updated"), UPDATED("updated");
}
