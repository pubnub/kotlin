package com.pubnub.internal.endpoints.access

interface GrantInterface {
    val read: Boolean
    val write: Boolean
    val manage: Boolean
    val delete: Boolean
    val get: Boolean
    val update: Boolean
    val join: Boolean
    val ttl: Int
    val authKeys: List<String>
    val channels: List<String>
    val channelGroups: List<String>
    val uuids: List<String>
}
