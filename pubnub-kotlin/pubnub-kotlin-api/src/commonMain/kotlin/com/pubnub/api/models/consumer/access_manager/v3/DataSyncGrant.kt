package com.pubnub.api.models.consumer.access_manager.v3

/**
 * Factory for DataSync (App Context v4) PAM v3 resource grants, passed to the `datasync` parameter of
 * [com.pubnub.api.PubNub.grantToken].
 *
 * DataSync introduces three resource namespaces — `entities`, `relationships` and `memberships` — each of which can
 * be granted on an exact resource id or on a regex pattern. Only the four DataSync-relevant permission flags are
 * exposed: `get`, `create`, `update` and `delete`.
 *
 * ```kotlin
 * pubnub.grantToken(
 *     ttl = 60,
 *     authorizedUUID = UserId("pam-debug-admin"),
 *     datasync = listOf(
 *         DataSyncGrant.entity("capy-001", get = true, update = true),
 *         DataSyncGrant.entityPattern(".*", get = true),
 *         DataSyncGrant.relationshipPattern(".*", get = true),
 *         DataSyncGrant.membership("user-123:channel-X", get = true),
 *     ),
 * ).sync().token
 * ```
 */
object DataSyncGrant {
    // entities
    fun entity(
        name: String,
        get: Boolean = false,
        create: Boolean = false,
        update: Boolean = false,
        delete: Boolean = false,
    ): DataSyncGrantType = PNDataSyncResourceGrant(DataSyncNamespace.ENTITIES, name, get, create, update, delete)

    fun entityPattern(
        pattern: String,
        get: Boolean = false,
        create: Boolean = false,
        update: Boolean = false,
        delete: Boolean = false,
    ): DataSyncGrantType = PNDataSyncPatternGrant(DataSyncNamespace.ENTITIES, pattern, get, create, update, delete)

    // relationships
    fun relationship(
        name: String,
        get: Boolean = false,
        create: Boolean = false,
        update: Boolean = false,
        delete: Boolean = false,
    ): DataSyncGrantType = PNDataSyncResourceGrant(DataSyncNamespace.RELATIONSHIPS, name, get, create, update, delete)

    fun relationshipPattern(
        pattern: String,
        get: Boolean = false,
        create: Boolean = false,
        update: Boolean = false,
        delete: Boolean = false,
    ): DataSyncGrantType = PNDataSyncPatternGrant(DataSyncNamespace.RELATIONSHIPS, pattern, get, create, update, delete)

    // memberships
    fun membership(
        name: String,
        get: Boolean = false,
        create: Boolean = false,
        update: Boolean = false,
        delete: Boolean = false,
    ): DataSyncGrantType = PNDataSyncResourceGrant(DataSyncNamespace.MEMBERSHIPS, name, get, create, update, delete)

    fun membershipPattern(
        pattern: String,
        get: Boolean = false,
        create: Boolean = false,
        update: Boolean = false,
        delete: Boolean = false,
    ): DataSyncGrantType = PNDataSyncPatternGrant(DataSyncNamespace.MEMBERSHIPS, pattern, get, create, update, delete)
}
