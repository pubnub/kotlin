package com.pubnub.api.models.consumer.access_manager.v3

import com.pubnub.api.models.TokenBitmask

data class PNToken(
    val version: Int = 0,
    val timestamp: Long = 0,
    val ttl: Long = 0,
    val authorizedUUID: String? = null,
    val resources: PNTokenResources,
    val patterns: PNTokenResources,
    val meta: Any? = null
) {

    data class PNTokenResources(
        val channels: Map<String, PNResourcePermissions> = emptyMap(),
        val channelGroups: Map<String, PNResourcePermissions> = emptyMap(),
        val uuids: Map<String, PNResourcePermissions> = emptyMap()
    )

    data class PNResourcePermissions(
        val read: Boolean = false,
        val write: Boolean = false,
        val manage: Boolean = false,
        val delete: Boolean = false,
        val get: Boolean = false,
        val update: Boolean = false,
        val join: Boolean = false
    ) {

        constructor(grant: Int) : this(
            grant and TokenBitmask.READ != 0,
            grant and TokenBitmask.WRITE != 0,
            grant and TokenBitmask.MANAGE != 0,
            grant and TokenBitmask.DELETE != 0,
            grant and TokenBitmask.GET != 0,
            grant and TokenBitmask.UPDATE != 0,
            grant and TokenBitmask.JOIN != 0
        )
    }
}
