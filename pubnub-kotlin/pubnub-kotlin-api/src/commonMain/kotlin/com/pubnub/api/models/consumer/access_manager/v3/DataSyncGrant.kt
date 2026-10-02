package com.pubnub.api.models.consumer.access_manager.v3

/**
 * Factory for DataSync (App Context v4) PAM v3 resource grants, passed in the `grants` list of
 * [com.pubnub.api.PubNub.grantToken].
 *
 * Every DataSync resource is granted here, on an exact resource id or on a regex pattern:
 * - [entity] / [entityPattern] → `datasync:entities` bucket;
 * - [relationship] / [relationshipPattern] → `datasync:relationships` bucket;
 * - [membership] / [membershipPattern] → `datasync:memberships` bucket;
 * - [channel] / [channelPattern] → the plain `channels` bucket;
 * - [user] / [userPattern] → the plain `users` bucket.
 *
 * Only the four DataSync-relevant permission flags are exposed: `get`, `create`, `update` and `delete`.
 *
 * **Shared `channels` bucket:** DataSync channels share the `channels` bucket with pub/sub and App Context v2, so
 * `channel(id, update = true)` also authorizes App Context v2 `setChannelMetadata` for the same id (and `get` /
 * `delete` likewise). Grants on the same id are OR-merged, so combining a [ChannelGrant] and a [channel] grant on one
 * id is safe: the token carries the union of both.
 *
 * [entity], [relationship], [channel] and [user] grants (and their pattern variants) can also carry an optional
 * `projection`: when this client uses the token to access this resource, they see it through this projection. A
 * projection is a named, filtered view of a resource's fields, defined in the class schema under `projections`. When
 * set, the SDK emits the corresponding `pn-projections` entry into the token meta automatically. Omit it (or pass
 * `null`) to use the implicit `__default__` projection. Don't write `pn-projections` into the token meta yourself:
 * `grantToken` rejects a caller meta that contains it.
 *
 * Which projections a resource has depends on its class:
 * - entities and relationships: the projections declared by their custom class;
 * - channels and users: only `__default__` for the built-in `Channel` / `User` classes. A named projection only has
 *   an effect for a custom Channel / User subclass that declares it;
 * - memberships: always `__default__` (the built-in `Membership` class has no named projections), so [membership]
 *   takes no `projection` parameter.
 *
 * These grants authorize DataSync **REST CRUD** (`get`/`create`/`update`/`delete`) on the resource record only.
 * They do **not** authorize subscribing to realtime events: a realtime subscribe is a plain PubSub read of the
 * resource's ref-channel. Use [subscribe] / [subscribePattern] for that. They resolve the ref-channel names for you.
 * See the
 * `subscription(...)` methods on the DataSync handles ([com.pubnub.api.v2.entities.DataSyncEntity],
 * [com.pubnub.api.v2.entities.DataSyncChannel], [com.pubnub.api.v2.entities.DataSyncUser]).
 *
 * ```kotlin
 * pubnub.grantToken(
 *     ttl = 60,
 *     authorizedUserId = UserId("pam-debug-admin"),
 *     grants = listOf(
 *         // entities
 *         DataSyncGrant.entity("capy-001", get = true, update = true, projection = "admin"),
 *         DataSyncGrant.entityPattern("capy-.*", get = true),
 *         // relationships
 *         DataSyncGrant.relationship("user.A:channel.X", get = true, projection = "admin"),
 *         DataSyncGrant.relationshipPattern("user\\.A:.*", get = true),
 *         // memberships
 *         DataSyncGrant.membership("user-123:channel-X", get = true, delete = true),
 *         DataSyncGrant.membershipPattern("user-123:.*", get = true),
 *         // channels (plain `channels` bucket)
 *         DataSyncGrant.channel("chat-1", get = true, update = true, projection = "admin"),
 *         DataSyncGrant.channelPattern("chat-.*", get = true),
 *         // users (plain `users` bucket)
 *         DataSyncGrant.user("user-123", get = true, update = true),
 *         DataSyncGrant.userPattern("user-.*", get = true, create = true),
 *         // realtime subscribe (pub/sub `read` on the resolved ref-channel)
 *         DataSyncGrant.subscribe("capy-001"),                     // read on "capy-001"
 *         DataSyncGrant.subscribe("chat-1", projection = "admin"), // read on "__admin__chat-1"
 *         // default projection publishes on the bare id, so no prefix; `^` keeps it off the `__admin__capy-…` mirrors
 *         DataSyncGrant.subscribePattern("capy-.*"),                       // read on "^(?:capy-.*)"
 *         DataSyncGrant.subscribePattern("chat-.*", projection = "admin"), // read on "^__admin__(?:chat-.*)"
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
     * Grants DataSync REST CRUD on a membership record. There is no `projection` parameter: memberships always use
     * the built-in `Membership` class, which declares no named projections, so they are always read through
     * `__default__`. For a custom membership-like class with projections, use a relationship class and [relationship].
     */
    fun membership(
        name: String,
        get: Boolean = false,
        create: Boolean = false,
        update: Boolean = false,
        delete: Boolean = false,
    ): DataSyncGrantType =
        PNDataSyncResourceGrant(DataSyncNamespace.MEMBERSHIPS, name, get, create, update, delete, projection = null)

    /**
     * Pattern (regex) variant of [membership].
     */
    fun membershipPattern(
        pattern: String,
        get: Boolean = false,
        create: Boolean = false,
        update: Boolean = false,
        delete: Boolean = false,
    ): DataSyncGrantType =
        PNDataSyncPatternGrant(DataSyncNamespace.MEMBERSHIPS, pattern, get, create, update, delete, projection = null)

    // channels

    /**
     * Grants DataSync REST CRUD on a channel record. The permission bits land in the plain `channels` bucket, shared
     * with pub/sub and App Context v2: `update = true` also authorizes App Context v2 `setChannelMetadata` for the
     * same id.
     *
     * @param projection the single projection the token holder looks *through* for this channel's REST reads, or
     * `null` for the implicit `__default__` projection. Emitted into the token meta as the `pn-projections` entry
     * `datasync:channels:<name>`. It does not affect realtime subscribe (see [subscribe]). A named projection only
     * has an effect for channels of a custom Channel subclass that declares it: the built-in `Channel` class exposes
     * its fields through `__default__` only. `parseToken` returns it in `PNToken.projections.resources.channels`
     * (`patterns.channels` for [channelPattern]).
     */
    fun channel(
        name: String,
        get: Boolean = false,
        create: Boolean = false,
        update: Boolean = false,
        delete: Boolean = false,
        projection: String? = null,
    ): DataSyncGrantType =
        PNDataSyncResourceGrant(DataSyncNamespace.CHANNELS_PROJECTION, name, get, create, update, delete, projection)

    /**
     * Pattern (regex) variant of [channel].
     */
    fun channelPattern(
        pattern: String,
        get: Boolean = false,
        create: Boolean = false,
        update: Boolean = false,
        delete: Boolean = false,
        projection: String? = null,
    ): DataSyncGrantType =
        PNDataSyncPatternGrant(DataSyncNamespace.CHANNELS_PROJECTION, pattern, get, create, update, delete, projection)

    // users

    /**
     * Grants DataSync REST CRUD on a user record. The permission bits land in the plain `users` bucket.
     *
     * @param projection the single projection the token holder looks *through* for this user's REST reads, or `null`
     * for the implicit `__default__` projection. Emitted into the token meta as the `pn-projections` entry
     * `datasync:users:<name>`. It does not affect realtime subscribe (see [subscribe]). A named projection only has
     * an effect for users of a custom User subclass that declares it: the built-in `User` class exposes its fields
     * through `__default__` only. `parseToken` returns it in `PNToken.projections.resources.users`
     * (`patterns.users` for [userPattern]).
     */
    fun user(
        name: String,
        get: Boolean = false,
        create: Boolean = false,
        update: Boolean = false,
        delete: Boolean = false,
        projection: String? = null,
    ): DataSyncGrantType =
        PNDataSyncResourceGrant(DataSyncNamespace.USERS_PROJECTION, name, get, create, update, delete, projection)

    /**
     * Pattern (regex) variant of [user].
     */
    fun userPattern(
        pattern: String,
        get: Boolean = false,
        create: Boolean = false,
        update: Boolean = false,
        delete: Boolean = false,
        projection: String? = null,
    ): DataSyncGrantType =
        PNDataSyncPatternGrant(DataSyncNamespace.USERS_PROJECTION, pattern, get, create, update, delete, projection)

    // realtime subscribe

    /**
     * Grants a realtime subscribe on the ref-channel of a DataSync entity, user or channel: a pub/sub `read` on
     * [DataSyncNamespace.refChannel] (`id` for the default projection, `__{projection}__{id}` otherwise).
     * Relationships and memberships have no ref-channel of their own: their events are published to both endpoint
     * refs.
     *
     * The [projection] here only **selects the ref-channel name**. It adds nothing to the token meta. That differs
     * from the `projection` of [entity] / [channel] / [user], which sets the REST read view in `meta.pn-projections`.
     * To both read through and subscribe to a projection, grant the pair:
     * ```kotlin
     * DataSyncGrant.channel("chat-1", get = true, projection = "admin") // REST reads through "admin"
     * DataSyncGrant.subscribe("chat-1", projection = "admin")           // read on "__admin__chat-1"
     * ```
     *
     * Returns a plain [ChannelGrant] (not a [DataSyncGrantType], which has no `read` bit).
     *
     * @param projection `null` (or `"default"` / `"__default__"`) for the default projection.
     * @throws IllegalArgumentException if [projection] is blank.
     */
    fun subscribe(
        id: String,
        projection: String? = null,
    ): ChannelGrant = ChannelGrant.name(DataSyncNamespace.refChannel(id, projection), read = true)

    /**
     * Pattern variant of [subscribe]: a pub/sub `read` on every ref-channel whose id matches the regex [pattern], as
     * seen through [projection]. The channel regex is built by [DataSyncNamespace.refChannelPattern]: it is anchored
     * at the start and wraps [pattern] in a non-capturing group, e.g. `subscribePattern("capy-.*")` →
     * `^(?:capy-.*)` and `subscribePattern("capy-.*", "admin")` → `^__admin__(?:capy-.*)`.
     *
     * The anchor matters: a hand-written `ChannelGrant.pattern("capy-.*", read = true)` also matches the projection
     * mirrors (`__admin__capy-1`), because PAM does not anchor patterns. A leading `^` in [pattern] is moved in front
     * of the prefix. A pattern anchoring several alternatives (`^a|^b`) is not rewritten and won't match under a
     * projection; write it as `a|b`. [pattern] must be a valid regex on its own, so an unbalanced `)` can't close the
     * wrapping group and escape the anchor.
     *
     * Like [subscribe], [projection] only selects the channel names and adds nothing to the token meta.
     *
     * @throws IllegalArgumentException if [pattern] is blank, only `^` or not a valid regex, or if [projection] is
     * blank.
     */
    fun subscribePattern(
        pattern: String,
        projection: String? = null,
    ): ChannelGrant = ChannelGrant.pattern(DataSyncNamespace.refChannelPattern(pattern, projection), read = true)
}
