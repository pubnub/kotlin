package com.pubnub.internal.endpoints.access

interface IGrant {
    val read: Boolean
    val write: Boolean
    val manage: Boolean
    val delete: Boolean
    val ttl: Int
    val authKeys: List<String>
    val channels: List<String>
    val channelGroups: List<String>
}
