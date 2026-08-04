package com.pubnub.api.models.consumer.access_manager.v3

import com.pubnub.api.models.TokenBitmask
import kotlin.jvm.JvmOverloads

data class PNToken(
    val version: Int = 0,
    val timestamp: Long = 0,
    val ttl: Long = 0,
    val authorizedUUID: String? = null,
    val resources: PNTokenResources,
    val patterns: PNTokenResources,
    val meta: Any? = null,
    /**
     * The DataSync projections carried by this token, split by namespace, or `null` when the token has no
     * `pn-projections` block. Each grant's projection is the single named view the token holder looks *through* for
     * that resource. The same data also remains available in raw form under [meta] (key `pn-projections`).
     */
    val projections: PNDataSyncProjections? = null,
) {
    data class PNTokenResources(
        val channels: Map<String, PNResourcePermissions> = emptyMap(),
        val channelGroups: Map<String, PNResourcePermissions> = emptyMap(),
        val uuids: Map<String, PNResourcePermissions> = emptyMap(),
        val users: Map<String, PNResourcePermissions> = emptyMap(),
        val datasyncEntities: Map<String, PNResourcePermissions> = emptyMap(),
        val datasyncRelationships: Map<String, PNResourcePermissions> = emptyMap(),
        val datasyncMemberships: Map<String, PNResourcePermissions> = emptyMap(),
    )

    data class PNResourcePermissions
        @JvmOverloads
        constructor(
            val read: Boolean = false,
            val write: Boolean = false,
            val manage: Boolean = false,
            val delete: Boolean = false,
            val get: Boolean = false,
            val update: Boolean = false,
            val join: Boolean = false,
            val create: Boolean = false,
        ) {
            constructor(grant: Int) : this(
                grant and TokenBitmask.READ != 0,
                grant and TokenBitmask.WRITE != 0,
                grant and TokenBitmask.MANAGE != 0,
                grant and TokenBitmask.DELETE != 0,
                grant and TokenBitmask.GET != 0,
                grant and TokenBitmask.UPDATE != 0,
                grant and TokenBitmask.JOIN != 0,
                grant and TokenBitmask.CREATE != 0,
            )
        }
}

/**
 * DataSync projections decoded from a token's `pn-projections` meta block, split into exact-resource and pattern
 * grants. Each value is the single projection name the token holder looks *through* for that resource; `__default__`
 * denotes the implicit base projection.
 */
data class PNDataSyncProjections(
    val resources: PNDataSyncProjectionScope = PNDataSyncProjectionScope(),
    val patterns: PNDataSyncProjectionScope = PNDataSyncProjectionScope(),
)

/**
 * Per-namespace projection assignments (resource id -> projection name) for one side (`res` or `pat`) of a token's
 * `pn-projections` block. Keys are the bare resource ids (e.g. `user.A`, `user.A:channel.X`), not the composite
 * `datasync:<type>:<id>` wire keys.
 */
data class PNDataSyncProjectionScope(
    val entities: Map<String, String> = emptyMap(),
    val relationships: Map<String, String> = emptyMap(),
    val memberships: Map<String, String> = emptyMap(),
)
