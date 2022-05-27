package com.pubnub.api.models.consumer.objects

enum class PNKey(internal val fieldName: String) {
    ID("id"),
    NAME("name"),
    UPDATED("updated"),
    TYPE("type"),
    STATUS("status");
}

sealed class PNSortKey(
    private val key: PNKey,
    private val dir: String = "asc"
) {

    class PNAsc(key: PNKey) : PNSortKey(key = key, dir = "asc")
    class PNDesc(key: PNKey) : PNSortKey(key = key, dir = "desc")

    internal fun toSortParameter(): String {
        return key.fieldName + ":" + dir
    }

    companion object {
        fun asc(key: PNKey): PNSortKey {
            return PNAsc(key)
        }

        fun desc(key: PNKey): PNSortKey {
            return PNDesc(key)
        }
    }
}
