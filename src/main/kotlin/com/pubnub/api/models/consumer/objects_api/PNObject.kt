package com.pubnub.api.models.consumer.objects_api

open class PNObject(val id: String) {

    val created: String? = null
    val updated: String? = null
    val eTag: String? = null

    // todo custom interceptor
    open var custom: Any? = null

    override fun toString(): String {
        return "PNObject(" +
                "id='$id', " +
                "created=$created, " +
                "updated=$updated, " +
                "eTag=$eTag, " +
                "custom=$custom" +
                ")"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PNObject) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }


}