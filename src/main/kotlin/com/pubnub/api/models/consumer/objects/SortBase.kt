package com.pubnub.api.models.consumer.objects

abstract class SortBase() {
    abstract fun toSortParameter(): String
}
