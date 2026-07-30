package com.pubnub.api.models.consumer.access_manager.v3

/**
 * Factory for DataSync (App Context v4) PAM v3 resource grants, passed to the `dataSync` parameter of
 * [com.pubnub.api.PubNub.grantToken].
 *
 * DataSync introduces three resource namespaces — `datasync:entities`, `datasync:relationships` and
 * `datasync:memberships` (the keys emitted into the token's `res`/`pat` maps) — each of which can be granted on an
 * exact resource id or on a regex pattern. Only the four DataSync-relevant permission flags are exposed: `get`,
 * `create`, `update` and `delete`.
 *
 * Each grant can also carry an optional `projection`: when this client uses the token to access this resource, they
 * see it through this projection. A projection is a named, filtered view of a resource's fields, defined in the
 * entity/relationship class schema under `projections`. When set, the SDK emits the corresponding `pn-projections`
 * entry into the token meta automatically. Omit it (or pass `null`) to use the implicit `__default__` projection.
 *
 * ```kotlin
 * pubnub.grantToken(
 *     ttl = 60,
 *     authorizedUUID = "pam-debug-admin",
 *     dataSync = listOf(
 *         DataSyncGrant.entity("capy-001", get = true, update = true, projection = "admin"),
 *         DataSyncGrant.entityPattern(".*", get = true),
 *         DataSyncGrant.relationship("user.A:channel.X", get = true, projection = "admin"),
 *         DataSyncGrant.membership("user-123:channel-X", get = true),
 *     ),
 * ).sync().token
 * ```
 */
object DataSyncGrant {
    // entities

    /**
     * @param projection the single projection the token holder looks *through* for this resource, or `null` for the
     * implicit `__default__` projection. When set, the SDK emits the matching `pn-projections` entry into the token
     * meta automatically. Pass [DataSyncNamespace.DEFAULT_PROJECTION] to write `__default__` out explicitly.
     */
    fun entity(
        name: String,
        get: Boolean = false,
        create: Boolean = false,
        update: Boolean = false,
        delete: Boolean = false,
        projection: String? = null,
    ): DataSyncGrantType =
        PNDataSyncResourceGrant(DataSyncNamespace.ENTITIES, name, get, create, update, delete, projection)

    /**
     * @param projection the single projection the token holder looks *through* for this resource, or `null` for the
     * implicit `__default__` projection. When set, the SDK emits the matching `pn-projections` entry into the token
     * meta automatically. Pass [DataSyncNamespace.DEFAULT_PROJECTION] to write `__default__` out explicitly.
     */
    fun entityPattern(
        pattern: String,
        get: Boolean = false,
        create: Boolean = false,
        update: Boolean = false,
        delete: Boolean = false,
        projection: String? = null,
    ): DataSyncGrantType =
        PNDataSyncPatternGrant(DataSyncNamespace.ENTITIES, pattern, get, create, update, delete, projection)

    // relationships

    /**
     * @param projection the single projection the token holder looks *through* for this resource, or `null` for the
     * implicit `__default__` projection. When set, the SDK emits the matching `pn-projections` entry into the token
     * meta automatically. Pass [DataSyncNamespace.DEFAULT_PROJECTION] to write `__default__` out explicitly.
     */
    fun relationship(
        name: String,
        get: Boolean = false,
        create: Boolean = false,
        update: Boolean = false,
        delete: Boolean = false,
        projection: String? = null,
    ): DataSyncGrantType =
        PNDataSyncResourceGrant(DataSyncNamespace.RELATIONSHIPS, name, get, create, update, delete, projection)

    /**
     * @param projection the single projection the token holder looks *through* for this resource, or `null` for the
     * implicit `__default__` projection. When set, the SDK emits the matching `pn-projections` entry into the token
     * meta automatically. Pass [DataSyncNamespace.DEFAULT_PROJECTION] to write `__default__` out explicitly.
     */
    fun relationshipPattern(
        pattern: String,
        get: Boolean = false,
        create: Boolean = false,
        update: Boolean = false,
        delete: Boolean = false,
        projection: String? = null,
    ): DataSyncGrantType =
        PNDataSyncPatternGrant(DataSyncNamespace.RELATIONSHIPS, pattern, get, create, update, delete, projection)

    // memberships

    /**
     * @param projection the single projection the token holder looks *through* for this resource, or `null` for the
     * implicit `__default__` projection. When set, the SDK emits the matching `pn-projections` entry into the token
     * meta automatically. Pass [DataSyncNamespace.DEFAULT_PROJECTION] to write `__default__` out explicitly.
     */
    fun membership(
        name: String,
        get: Boolean = false,
        create: Boolean = false,
        update: Boolean = false,
        delete: Boolean = false,
        projection: String? = null,
    ): DataSyncGrantType =
        PNDataSyncResourceGrant(DataSyncNamespace.MEMBERSHIPS, name, get, create, update, delete, projection)

    /**
     * @param projection the single projection the token holder looks *through* for this resource, or `null` for the
     * implicit `__default__` projection. When set, the SDK emits the matching `pn-projections` entry into the token
     * meta automatically. Pass [DataSyncNamespace.DEFAULT_PROJECTION] to write `__default__` out explicitly.
     */
    fun membershipPattern(
        pattern: String,
        get: Boolean = false,
        create: Boolean = false,
        update: Boolean = false,
        delete: Boolean = false,
        projection: String? = null,
    ): DataSyncGrantType =
        PNDataSyncPatternGrant(DataSyncNamespace.MEMBERSHIPS, pattern, get, create, update, delete, projection)
}
