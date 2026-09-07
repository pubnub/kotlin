package com.pubnub.api.datasync

import com.pubnub.api.endpoints.datasync.channel.CreateChannel
import com.pubnub.api.endpoints.datasync.channel.GetChannel
import com.pubnub.api.endpoints.datasync.channel.GetChannels
import com.pubnub.api.endpoints.datasync.channel.RemoveChannel
import com.pubnub.api.endpoints.datasync.channel.SetChannel
import com.pubnub.api.endpoints.datasync.channel.UpdateChannel
import com.pubnub.api.endpoints.datasync.entity.CreateEntity
import com.pubnub.api.endpoints.datasync.entity.GetEntities
import com.pubnub.api.endpoints.datasync.entity.GetEntity
import com.pubnub.api.endpoints.datasync.entity.RemoveEntity
import com.pubnub.api.endpoints.datasync.entity.SetEntity
import com.pubnub.api.endpoints.datasync.entity.UpdateEntity
import com.pubnub.api.endpoints.datasync.membership.CreateMembership
import com.pubnub.api.endpoints.datasync.membership.GetMembership
import com.pubnub.api.endpoints.datasync.membership.GetMemberships
import com.pubnub.api.endpoints.datasync.membership.RemoveMembership
import com.pubnub.api.endpoints.datasync.membership.SetMembership
import com.pubnub.api.endpoints.datasync.membership.UpdateMembership
import com.pubnub.api.endpoints.datasync.relationship.CreateRelationship
import com.pubnub.api.endpoints.datasync.relationship.GetRelationship
import com.pubnub.api.endpoints.datasync.relationship.GetRelationships
import com.pubnub.api.endpoints.datasync.relationship.RemoveRelationship
import com.pubnub.api.endpoints.datasync.relationship.SetRelationship
import com.pubnub.api.endpoints.datasync.relationship.UpdateRelationship
import com.pubnub.api.endpoints.datasync.user.CreateUser
import com.pubnub.api.endpoints.datasync.user.GetUser
import com.pubnub.api.endpoints.datasync.user.GetUsers
import com.pubnub.api.endpoints.datasync.user.RemoveUser
import com.pubnub.api.endpoints.datasync.user.SetUser
import com.pubnub.api.endpoints.datasync.user.UpdateUser
import com.pubnub.api.models.consumer.datasync.PNDataSyncClassLevel
import com.pubnub.api.models.consumer.datasync.PNDataSyncSortField
import com.pubnub.api.models.consumer.datasync.entity.PNJsonPatchOperation

/**
 * Entry point for the DataSync API, reached via `pubnub.dataSync`.
 */
interface DataSync {
    /**
     * Get a DataSync entity by its id.
     *
     * @param entityId Identifier of the entity to fetch.
     */
    fun getEntity(entityId: String): GetEntity

    /**
     * Create a DataSync entity.
     *
     * @param className Entity class identifier.
     * @param classVersion Version of the entity class.
     * @param classLevel Optional level at which the entity class is defined. Disambiguates a class defined
     *   at more than one level. Create-only — not accepted by [setEntity].
     * @param entityId Optional entity identifier. When `null` the server generates one.
     * @param status Optional entity status.
     * @param payload Optional arbitrary JSON object payload.
     */
    fun createEntity(
        className: String,
        classVersion: Int,
        classLevel: PNDataSyncClassLevel? = null,
        entityId: String? = null,
        status: String? = null,
        payload: Any? = null,
    ): CreateEntity

    /**
     * Remove a DataSync entity by its id.
     *
     * @param entityId Identifier of the entity to remove.
     * @param ifMatch Optional eTag for a conditional remove (`If-Match` header).
     */
    fun removeEntity(entityId: String, ifMatch: String? = null): RemoveEntity

    /**
     * List DataSync entities of a class.
     *
     * Results are scoped to the caller's access token: only entities the token is permitted to read (`get`)
     * are returned. Entities the token cannot read are silently omitted — the call does not error and does not
     * return a `403` for the un-readable entities. This token scoping is applied before [filterFast] / [filter] /
     * [sort]. When the PubNub instance is configured with a secretKey (a trusted server-side deployment, never a
     * client — the secretKey must not be shipped to clients), or when using a token whose grants cover the whole
     * result set, no permission-based filtering is applied and all matching entities are returned, subject only to
     * [filterFast] / [filter] / [sort] and [limit] paging.
     *
     * @param className Entity class identifier.
     * @param classVersion Optional entity class version. When `null` the server uses the latest.
     * @param classLevel Optional level at which the entity class is defined (e.g. `SubKey` / `Global`).
     * @param filterFast Optional filter expression. Filtering is only allowed on properties the entity class
     *   marks as filterable; filtering on any other property returns a server error. Each property in the class
     *   declares a `filtering` mode — `none`, `simple`, or `full` — and the mode is cumulative: a `simple`
     *   property is usable with `filterFast` (and [sort] on the `filterFast` path); a `full` property is usable
     *   with both `filterFast` and [filter]; a `none` property is not filterable at all. So a `simple` property
     *   works here but is rejected by [filter]. The filterable set has no fixed default — it is whatever the
     *   (required) [className] declares. In addition, the built-in fields `id`, `createdAt`, `updatedAt`, and
     *   `status` (case-sensitive, exactly as spelled) behave as `full` and are always filterable and sortable on
     *   any class, on both the `filterFast` and [filter] paths, regardless of its declared properties.
     *   `filterFast` is strongly consistent — it always reflects the latest writes — but accepts fewer conditions
     *   than [filter]; a limit on the number of conditions applies and can be adjusted by PubNub support
     *   (see the PubNub DataSync documentation for the current limit). For larger or more complex queries use
     *   [filter]. At most one of [filterFast] and [filter] may be supplied; sending both is
     *   rejected with an error.
     * @param filter Optional advanced filter expression. Uses the same syntax as [filterFast], but only
     *   properties whose `filtering` mode is `full` (plus the built-in fields) are usable here — a `simple`
     *   property that works with [filterFast] is rejected by `filter`. It is not subject to the same condition
     *   limit, so use it for larger or more complex queries. The trade-off is consistency: `filter` is eventually
     *   consistent (recent writes may not yet be reflected), whereas [filterFast] is strongly consistent. At most
     *   one of [filterFast] and [filter] may be supplied; sending both is rejected with an error.
     * @param sort Optional sort criteria applied in order; each [PNDataSyncSortField] sorts on a payload
     *   property either ascending (default) or descending. Sorting is governed by the same `filtering`-mode gate
     *   as filtering: a `simple` property is sortable on the strongly-consistent path ([filterFast], or a
     *   sort-only / [cursor] request with neither filter); a `full` property is sortable on that path and on the
     *   [filter] path. The built-in fields `id`, `createdAt`, `updatedAt`, and `status` are always sortable.
     * @param limit Optional page size (1–100, server default 20).
     * @param cursor Optional opaque cursor for pagination (from a previous result's `next.cursor`).
     */
    fun getEntities(
        className: String,
        classVersion: Int? = null,
        classLevel: PNDataSyncClassLevel? = null,
        filterFast: String? = null,
        filter: String? = null,
        sort: List<PNDataSyncSortField> = emptyList(),
        limit: Int? = null,
        cursor: String? = null,
    ): GetEntities

    /**
     * Partially update a DataSync entity via JSON Patch (RFC-6902).
     *
     * @param entityId Identifier of the entity to patch.
     * @param operations Non-empty list of JSON Patch operations to apply.
     * @param ifMatch Optional eTag for optimistic concurrency (`If-Match` header).
     */
    fun updateEntity(
        entityId: String,
        operations: List<PNJsonPatchOperation>,
        ifMatch: String? = null,
    ): UpdateEntity

    /**
     * Replaces an entity in full.
     *
     * Every mutable field is overwritten. Omitting [status] or [payload] clears the stored value rather than
     * preserving it, so a read-modify-write must send back every field it wants to keep. Use [updateEntity] to
     * change part of an entity.
     *
     * @param entityId Identifier of the entity to replace.
     * @param classVersion Version of the class the payload conforms to.
     * @param status Optional status to store with the entity.
     * @param payload Optional replacement entity fields (arbitrary JSON object).
     * @param ifMatch Optional eTag last read, to fail the request when the entity changed since (`If-Match` header).
     */
    fun setEntity(
        entityId: String,
        classVersion: Int,
        status: String? = null,
        payload: Any? = null,
        ifMatch: String? = null,
    ): SetEntity

    /**
     * Get a DataSync user by its id.
     *
     * A User is a specialized DataSync entity of class `User`.
     *
     * @param userId Identifier of the user to fetch.
     */
    fun getUser(userId: String): GetUser

    /**
     * Create a DataSync user.
     *
     * @param classVersion Version of the entity class.
     * @param userId Optional user identifier. When `null` the server generates one.
     * @param className Optional entity class identifier. When `null` the server defaults it to `User`.
     *   When set it must be a `User` subclass.
     * @param classLevel Optional level at which the entity class is defined. Disambiguates a class defined
     *   at more than one level. Create-only — not accepted by [setUser]. The built-in `User` class is
     *   defined at the `GLOBAL` level.
     * @param status Optional user status.
     * @param payload Optional arbitrary JSON object payload.
     */
    fun createUser(
        classVersion: Int,
        userId: String? = null,
        className: String? = null,
        classLevel: PNDataSyncClassLevel? = null,
        status: String? = null,
        payload: Any? = null,
    ): CreateUser

    /**
     * Remove a DataSync user by its id.
     *
     * @param userId Identifier of the user to remove.
     * @param ifMatch Optional eTag for a conditional remove (`If-Match` header).
     */
    fun removeUser(userId: String, ifMatch: String? = null): RemoveUser

    /**
     * List DataSync users.
     *
     * Results are scoped to the caller's access token: only users the token is permitted to read (`get`)
     * are returned. Users the token cannot read are silently omitted — the call does not error and does not
     * return a `403` for the un-readable users. This token scoping is applied before [filterFast] / [filter] /
     * [sort]. When the PubNub instance is configured with a secretKey (a trusted server-side deployment, never a
     * client — the secretKey must not be shipped to clients), or when using a token whose grants cover the whole
     * result set, no permission-based filtering is applied and all matching users are returned, subject only to
     * [filterFast] / [filter] / [sort] and [limit] paging.
     *
     * @param className Optional entity class identifier. When `null` the whole User family is returned;
     *   when set it narrows the results to that `User` subclass.
     * @param classVersion Optional entity class version. When `null` the server uses the latest.
     * @param classLevel Optional level at which the entity class is defined. The built-in `User` class is
     *   defined at the `GLOBAL` level.
     * @param filterFast Optional filter expression. Filtering is only allowed on properties the entity class
     *   marks as filterable; filtering on any other property returns a server error. Each property declares a
     *   `filtering` mode — `none`, `simple`, or `full` — and the mode is cumulative: a `simple` property is
     *   usable with `filterFast` (and [sort] on the `filterFast` path); a `full` property is usable with both
     *   `filterFast` and [filter]; a `none` property is not filterable at all. So a `simple` property works here
     *   but is rejected by [filter]. The built-in `User` class exposes `name` and `type` as its filterable
     *   properties. In addition, the built-in fields `id`, `createdAt`, `updatedAt`, and `status`
     *   (case-sensitive, exactly as spelled) behave as `full` and are always filterable and sortable on any
     *   class, on both the `filterFast` and [filter] paths, regardless of its declared properties. `filterFast`
     *   is strongly consistent — it always reflects the latest writes —
     *   but accepts fewer conditions than [filter]; a limit on the number of conditions applies and can
     *   be adjusted by PubNub support (see the PubNub DataSync documentation for the current limit). For larger
     *   or more complex queries use [filter]. At most one of [filterFast] and [filter] may be
     *   supplied; sending both is rejected with an error.
     * @param filter Optional advanced filter expression. Uses the same syntax as [filterFast], but only
     *   properties whose `filtering` mode is `full` (plus the built-in fields) are usable here — a `simple`
     *   property that works with [filterFast] is rejected by `filter`. It is not subject to the same condition
     *   limit, so use it for larger or more complex queries. The trade-off is consistency: `filter` is eventually
     *   consistent (recent writes may not yet be reflected), whereas [filterFast] is strongly consistent. At most
     *   one of [filterFast] and [filter] may be supplied; sending both is rejected with an error.
     * @param sort Optional sort criteria applied in order; each [PNDataSyncSortField] sorts on a payload
     *   property either ascending (default) or descending. Sorting is governed by the same `filtering`-mode gate
     *   as filtering: a `simple` property is sortable on the strongly-consistent path ([filterFast], or a
     *   sort-only / [cursor] request with neither filter); a `full` property is sortable on that path and on the
     *   [filter] path. The built-in fields `id`, `createdAt`, `updatedAt`, and `status` are always sortable
     *   (built-in `User` class filterable/sortable properties: `name` and `type`).
     * @param limit Optional page size (1–100, server default 20).
     * @param cursor Optional opaque cursor for pagination (from a previous result's `next.cursor`).
     */
    fun getUsers(
        className: String? = null,
        classVersion: Int? = null,
        classLevel: PNDataSyncClassLevel? = null,
        filterFast: String? = null,
        filter: String? = null,
        sort: List<PNDataSyncSortField> = emptyList(),
        limit: Int? = null,
        cursor: String? = null,
    ): GetUsers

    /**
     * Partially update a DataSync user via JSON Patch (RFC-6902).
     *
     * @param userId Identifier of the user to patch.
     * @param operations Non-empty list of JSON Patch operations to apply.
     * @param ifMatch Optional eTag for optimistic concurrency (`If-Match` header).
     */
    fun updateUser(
        userId: String,
        operations: List<PNJsonPatchOperation>,
        ifMatch: String? = null,
    ): UpdateUser

    /**
     * Replaces a user in full.
     *
     * Every mutable field is overwritten. Omitting [status] or [payload] clears the stored value rather than
     * preserving it, so a read-modify-write must send back every field it wants to keep. Use [updateUser] to
     * change part of a user.
     *
     * @param userId Identifier of the user to replace.
     * @param classVersion Version of the class the payload conforms to.
     * @param status Optional status to store with the user.
     * @param payload Optional replacement user fields (arbitrary JSON object).
     * @param ifMatch Optional eTag last read, to fail the request when the user changed since (`If-Match` header).
     */
    fun setUser(
        userId: String,
        classVersion: Int,
        status: String? = null,
        payload: Any? = null,
        ifMatch: String? = null,
    ): SetUser

    /**
     * Get a DataSync channel by its id.
     *
     * A Channel is a specialized DataSync entity of class `Channel`.
     *
     * @param channelId Identifier of the channel to fetch.
     */
    fun getChannel(channelId: String): GetChannel

    /**
     * Create a DataSync channel.
     *
     * @param classVersion Version of the entity class.
     * @param channelId Optional channel identifier. When `null` the server generates one.
     * @param className Optional entity class identifier. When `null` the server defaults it to `Channel`.
     *   When set it must be a `Channel` subclass.
     * @param classLevel Optional level at which the entity class is defined. Disambiguates a class defined
     *   at more than one level. Create-only — not accepted by [setChannel]. The built-in `Channel` class is
     *   defined at the `GLOBAL` level.
     * @param status Optional channel status.
     * @param payload Optional arbitrary JSON object payload.
     */
    fun createChannel(
        classVersion: Int,
        channelId: String? = null,
        className: String? = null,
        classLevel: PNDataSyncClassLevel? = null,
        status: String? = null,
        payload: Any? = null,
    ): CreateChannel

    /**
     * Remove a DataSync channel by its id.
     *
     * @param channelId Identifier of the channel to remove.
     * @param ifMatch Optional eTag for a conditional remove (`If-Match` header).
     */
    fun removeChannel(channelId: String, ifMatch: String? = null): RemoveChannel

    /**
     * List DataSync channels.
     *
     * Results are scoped to the caller's access token: only channels the token is permitted to read (`get`)
     * are returned. Channels the token cannot read are silently omitted — the call does not error and does not
     * return a `403` for the un-readable channels. This token scoping is applied before [filterFast] / [filter] /
     * [sort]. When the PubNub instance is configured with a secretKey (a trusted server-side deployment, never a
     * client — the secretKey must not be shipped to clients), or when using a token whose grants cover the whole
     * result set, no permission-based filtering is applied and all matching channels are returned, subject only to
     * [filterFast] / [filter] / [sort] and [limit] paging.
     *
     * @param className Optional entity class identifier. When `null` the whole Channel family is returned;
     *   when set it narrows the results to that `Channel` subclass.
     * @param classVersion Optional entity class version. When `null` the server uses the latest.
     * @param classLevel Optional level at which the entity class is defined. The built-in `Channel` class is
     *   defined at the `GLOBAL` level.
     * @param filterFast Optional filter expression. Filtering is only allowed on properties the entity class
     *   marks as filterable; filtering on any other property returns a server error. Each property declares a
     *   `filtering` mode — `none`, `simple`, or `full` — and the mode is cumulative: a `simple` property is
     *   usable with `filterFast` (and [sort] on the `filterFast` path); a `full` property is usable with both
     *   `filterFast` and [filter]; a `none` property is not filterable at all. So a `simple` property works here
     *   but is rejected by [filter]. For the default `Channel` class this set is `name` and `type`,
     *   but a custom class or subclass may declare additional filterable properties. These are filterable
     *   indexes over `/payload/name` and `/payload/type` — both nullable, not a required or exclusive payload
     *   schema; the `payload` stays arbitrary JSON. In addition, the built-in fields `id`, `createdAt`,
     *   `updatedAt`, and `status` (case-sensitive, exactly as spelled) behave as `full` and are always filterable
     *   and sortable on any class, on both the `filterFast` and [filter] paths, regardless of its declared
     *   properties. `filterFast` is strongly consistent — it always reflects the
     *   latest writes — but accepts fewer conditions than [filter]; a limit on the number of conditions
     *   applies and can be adjusted by PubNub support (see the PubNub DataSync documentation for the current
     *   limit). For larger or more complex queries use [filter]. At most one of [filterFast] and
     *   [filter] may be supplied; sending both is rejected with an error.
     * @param filter Optional advanced filter expression. Uses the same syntax as [filterFast], but only
     *   properties whose `filtering` mode is `full` (plus the built-in fields) are usable here — a `simple`
     *   property that works with [filterFast] is rejected by `filter`. It is not subject to the same condition
     *   limit, so use it for larger or more complex queries. The trade-off is consistency: `filter` is eventually
     *   consistent (recent writes may not yet be reflected), whereas [filterFast] is strongly consistent. At most
     *   one of [filterFast] and [filter] may be supplied; sending both is rejected with an error.
     * @param sort Optional sort criteria applied in order; each [PNDataSyncSortField] sorts on a payload
     *   property either ascending (default) or descending. Sorting is governed by the same `filtering`-mode gate
     *   as filtering: a `simple` property is sortable on the strongly-consistent path ([filterFast], or a
     *   sort-only / [cursor] request with neither filter); a `full` property is sortable on that path and on the
     *   [filter] path. The built-in fields `id`, `createdAt`, `updatedAt`, and `status` are always sortable
     *   (default `Channel` class filterable/sortable properties: `name` and `type`).
     * @param limit Optional page size (1–100, server default 20).
     * @param cursor Optional opaque cursor for pagination (from a previous result's `next.cursor`).
     */
    fun getChannels(
        className: String? = null,
        classVersion: Int? = null,
        classLevel: PNDataSyncClassLevel? = null,
        filterFast: String? = null,
        filter: String? = null,
        sort: List<PNDataSyncSortField> = emptyList(),
        limit: Int? = null,
        cursor: String? = null,
    ): GetChannels

    /**
     * Partially update a DataSync channel via JSON Patch (RFC-6902).
     *
     * @param channelId Identifier of the channel to patch.
     * @param operations Non-empty list of JSON Patch operations to apply.
     * @param ifMatch Optional eTag for optimistic concurrency (`If-Match` header).
     */
    fun updateChannel(
        channelId: String,
        operations: List<PNJsonPatchOperation>,
        ifMatch: String? = null,
    ): UpdateChannel

    /**
     * Replaces a channel in full.
     *
     * Every mutable field is overwritten. Omitting [status] or [payload] clears the stored value rather than
     * preserving it, so a read-modify-write must send back every field it wants to keep. Use [updateChannel] to
     * change part of a channel.
     *
     * @param channelId Identifier of the channel to replace.
     * @param classVersion Version of the class the payload conforms to.
     * @param status Optional status to store with the channel.
     * @param payload Optional replacement channel fields (arbitrary JSON object).
     * @param ifMatch Optional eTag last read, to fail the request when the channel changed since (`If-Match` header).
     */
    fun setChannel(
        channelId: String,
        classVersion: Int,
        status: String? = null,
        payload: Any? = null,
        ifMatch: String? = null,
    ): SetChannel

    /**
     * Get a DataSync membership by its id.
     *
     * A Membership is a specialized DataSync relationship linking a Channel and a User.
     *
     * @param membershipId Identifier of the membership to fetch.
     */
    fun getMembership(membershipId: String): GetMembership

    /**
     * Create a DataSync membership linking a Channel and a User.
     *
     * This API always targets the built-in `Membership` relationship class — there is no `className`
     * parameter; the SDK fixes the class to `Membership`. Creating a Membership *subclass* is not supported
     * here; that is served by the general `/relationships` API.
     *
     * @param channelId Identifier of the Channel (entity A) to link. Must reference an existing
     *   Channel entity — a missing or wrong-class entity is rejected.
     * @param userId Identifier of the User (entity B) to link. Must reference an existing User
     *   entity — a missing or wrong-class entity is rejected.
     * @param classVersion Version of the `Membership` class the payload conforms to. Currently only version
     *   `1` exists — pass `1`. New versions may be introduced in the future; when one is, opt in by passing
     *   its number.
     * @param membershipId Optional membership identifier. When `null` the server generates one.
     * @param status Optional membership status.
     * @param payload Optional arbitrary JSON object payload.
     */
    fun createMembership(
        channelId: String,
        userId: String,
        classVersion: Int,
        membershipId: String? = null,
        status: String? = null,
        payload: Any? = null,
    ): CreateMembership

    /**
     * Remove a DataSync membership by its id.
     *
     * @param membershipId Identifier of the membership to remove.
     * @param ifMatch Optional eTag for a conditional remove (`If-Match` header).
     */
    fun removeMembership(membershipId: String, ifMatch: String? = null): RemoveMembership

    /**
     * List DataSync memberships, optionally filtered by channel and/or user.
     *
     * This API always queries the built-in `Membership` relationship class — there is no class-name query,
     * only [classVersion]. Querying a Membership *subclass* is not supported here; that is served by the
     * general `/relationships` API.
     *
     * Results are scoped to the caller's access token: only memberships the token is permitted to read (`get`)
     * are returned. Memberships the token cannot read are silently omitted — the call does not error and does
     * not return a `403` for the un-readable memberships. Token scoping and the [filterFast] / [filter] /
     * [sort] criteria are applied together, so the returned page never contains a membership the token cannot
     * read regardless of the other criteria. When the PubNub instance is configured with a secretKey (a trusted server-side
     * deployment, never a client — the secretKey must not be shipped to clients), or when using a token whose
     * grants cover the whole result set, no permission-based filtering is applied and all matching memberships
     * are returned, subject only to [filterFast] / [filter] / [sort] and [limit] paging.
     *
     * @param channelId Optional Channel identifier to filter by. [channelId] and [userId] are independent,
     *   AND-ed equality filters on fixed sides of the membership: [channelId] matches the Channel (entity A)
     *   side and [userId] matches the User (entity B) side. Supplying only one narrows to memberships on that
     *   side; supplying both returns the membership(s) matching both (i.e. that specific channel-user pair);
     *   supplying neither lists all readable memberships.
     * @param userId Optional User identifier to filter by. See [channelId] for how the two combine.
     * @param classVersion Restricts results to a single version of the `Membership` class. If omitted, every
     *   version is returned.
     * @param filterFast Optional filter expression. For the built-in `Membership` class, filtering is allowed
     *   only on the built-in fields `id`, `createdAt`, `updatedAt`, and `status` (case-sensitive, exactly as
     *   spelled), which are filterable and sortable on both the `filterFast` and [filter] paths — except that
     *   `status` may be excluded when the class declares it as a projected field and the token cannot fully
     *   reach it. `filterFast` is strongly consistent — it always
     *   reflects the latest writes — but accepts fewer conditions than [filter]; a limit on the number of
     *   conditions applies and can be adjusted by PubNub support (see the PubNub DataSync documentation for the
     *   current limit). For larger or more complex queries use [filter]. At most one of [filterFast] and
     *   [filter] may be supplied; sending both is rejected with an error.
     * @param filter Optional advanced filter expression. Uses the same syntax as [filterFast] and, for the
     *   built-in `Membership` class, targets the same built-in fields. It is not subject to the same condition
     *   limit, so use it for larger or more complex queries. The trade-off is consistency: `filter` is
     *   eventually consistent (recent writes may not yet be reflected), whereas [filterFast] is strongly
     *   consistent. At most one of [filterFast] and [filter] may be supplied; sending both is rejected with an
     *   error.
     * @param sort Optional sort criteria applied in order; each [PNDataSyncSortField] sorts on a property
     *   either ascending (default) or descending. For the built-in `Membership` class, the sortable set is the
     *   built-in fields `id`, `createdAt`, `updatedAt`, and `status` (with the same `status` caveat as
     *   [filterFast]).
     * @param limit Optional page size (1–100, server default 20).
     * @param cursor Optional opaque cursor for pagination (from a previous result's `next.cursor`).
     */
    fun getMemberships(
        channelId: String? = null,
        userId: String? = null,
        classVersion: Int? = null,
        filterFast: String? = null,
        filter: String? = null,
        sort: List<PNDataSyncSortField> = emptyList(),
        limit: Int? = null,
        cursor: String? = null,
    ): GetMemberships

    /**
     * Partially update a DataSync membership via JSON Patch (RFC-6902).
     *
     * @param membershipId Identifier of the membership to patch.
     * @param operations Non-empty list of JSON Patch operations to apply.
     * @param ifMatch Optional eTag for optimistic concurrency (`If-Match` header).
     */
    fun updateMembership(
        membershipId: String,
        operations: List<PNJsonPatchOperation>,
        ifMatch: String? = null,
    ): UpdateMembership

    /**
     * Replaces a membership in full.
     *
     * Every mutable field is overwritten. Omitting [status] or [payload] clears the stored value rather than
     * preserving it, so a read-modify-write must send back every field it wants to keep. This cannot re-point
     * or reclassify a membership — the linked channel/user and the relationship class are immutable. Use
     * [updateMembership] to change part of a membership.
     *
     * @param membershipId Identifier of the membership to replace.
     * @param classVersion Version of the `Membership` class the payload conforms to. Currently only version
     *   `1` exists — pass `1`. New versions may be introduced in the future; when one is, opt in by passing
     *   its number.
     * @param status Optional status to store with the membership.
     * @param payload Optional replacement membership fields (arbitrary JSON object).
     * @param ifMatch Optional eTag last read, to fail the request when the membership changed since
     *   (`If-Match` header).
     */
    fun setMembership(
        membershipId: String,
        classVersion: Int,
        status: String? = null,
        payload: Any? = null,
        ifMatch: String? = null,
    ): SetMembership

    /**
     * Get a DataSync relationship by its id.
     *
     * A Relationship is a typed link between two entities (side A and side B) under an arbitrary relationship
     * class.
     *
     * @param relationshipId Identifier of the relationship to fetch.
     */
    fun getRelationship(relationshipId: String): GetRelationship

    /**
     * Create a DataSync relationship linking two entities under a relationship class.
     *
     * Unlike `createMembership`, [className] is **required** — this API targets the caller-supplied relationship
     * class rather than a fixed built-in one, and the two ends are the generic [entityAId] / [entityBId] rather
     * than a Channel/User pair.
     *
     * @param entityAId Identifier of entity A to link. Must reference an existing entity — a missing
     *   or wrong-class entity is rejected.
     * @param entityBId Identifier of entity B to link. Must reference an existing entity — a missing
     *   or wrong-class entity is rejected.
     * @param className Relationship class identifier.
     * @param classVersion Version of the relationship class the payload conforms to. Relationship classes are
     *   versioned (an integer `>= 1`); pass the version of the class you are targeting.
     * @param relationshipId Optional relationship identifier. When `null` the server generates one.
     * @param status Optional relationship status.
     * @param payload Optional arbitrary JSON object payload.
     */
    fun createRelationship(
        entityAId: String,
        entityBId: String,
        className: String,
        classVersion: Int,
        relationshipId: String? = null,
        status: String? = null,
        payload: Any? = null,
    ): CreateRelationship

    /**
     * Remove a DataSync relationship by its id.
     *
     * @param relationshipId Identifier of the relationship to remove.
     * @param ifMatch Optional eTag for a conditional remove (`If-Match` header).
     */
    fun removeRelationship(relationshipId: String, ifMatch: String? = null): RemoveRelationship

    /**
     * List DataSync relationships in a relationship class, optionally filtered by either end.
     *
     * Unlike `getMemberships`, [className] is **required** — the relationship class to query is caller-supplied,
     * not a fixed built-in one.
     *
     * Filtering and sorting are only allowed on properties the relationship class marks as filterable via their
     * `filtering` mode (`none` / `simple` / `full`), which also determines which of [filterFast] / [filter] /
     * [sort] a property may be used with. The filterable set has no fixed default; it is whatever the supplied
     * [className] declares as filterable payload properties (indexes over the relationship's `/payload`; the
     * `payload` otherwise stays arbitrary JSON). In addition, the built-in fields `id`, `createdAt`, `updatedAt`,
     * and `status` behave as `full` and are always filterable and sortable on any class.
     *
     * Results are scoped to the caller's access token: only relationships the token is permitted to read (`get`)
     * are returned. Relationships the token cannot read are silently omitted — the call does not error and does
     * not return a `403` for the un-readable relationships. Token scoping and the [filterFast] / [filter] /
     * [sort] criteria are applied together, so the returned page never contains a relationship the token cannot
     * read regardless of the other criteria. When the PubNub instance is configured with a secretKey (a trusted
     * server-side deployment, never a client — the secretKey must not be shipped to clients), or when using a
     * token whose grants cover the whole result set, no permission-based filtering is applied and all matching
     * relationships are returned, subject only to [filterFast] / [filter] / [sort] and [limit] paging.
     *
     * @param className Relationship class identifier to query.
     * @param entityAId Optional entity A identifier to filter by. [entityAId] and [entityBId] are independent,
     *   AND-ed equality filters on the two sides of the relationship. Supplying only one narrows to relationships
     *   on that side; supplying both returns the relationships matching both; supplying neither lists all
     *   readable relationships in the class. This is not a pair-only lookup API.
     * @param entityBId Optional entity B identifier to filter by. See [entityAId] for how the two combine.
     * @param classVersion Restricts results to a single version of the relationship class. If omitted, every
     *   version is returned.
     * @param filterFast Optional filter expression. `filterFast` is strongly consistent — it always reflects the
     *   latest writes — but accepts fewer conditions than [filter]; a limit on the number of conditions applies
     *   and can be adjusted by PubNub support (see the PubNub DataSync documentation for the current limit). For
     *   larger or more complex queries use [filter]. At most one of [filterFast] and [filter] may be supplied;
     *   sending both is rejected with an error.
     * @param filter Optional advanced filter expression. Uses the same syntax as [filterFast]. It is not subject
     *   to the same condition limit, so use it for larger or more complex queries. The trade-off is consistency:
     *   `filter` is eventually consistent (recent writes may not yet be reflected), whereas [filterFast] is
     *   strongly consistent. At most one of [filterFast] and [filter] may be supplied; sending both is rejected
     *   with an error.
     * @param sort Optional sort criteria applied in order; each [PNDataSyncSortField] sorts on a property either
     *   ascending (default) or descending.
     * @param limit Optional page size (1–100, server default 20).
     * @param cursor Optional opaque cursor for pagination (from a previous result's `next.cursor`).
     */
    fun getRelationships(
        className: String,
        entityAId: String? = null,
        entityBId: String? = null,
        classVersion: Int? = null,
        filterFast: String? = null,
        filter: String? = null,
        sort: List<PNDataSyncSortField> = emptyList(),
        limit: Int? = null,
        cursor: String? = null,
    ): GetRelationships

    /**
     * Partially update a DataSync relationship via JSON Patch (RFC-6902).
     *
     * @param relationshipId Identifier of the relationship to patch.
     * @param operations Non-empty list of JSON Patch operations to apply.
     * @param ifMatch Optional eTag for optimistic concurrency (`If-Match` header).
     */
    fun updateRelationship(
        relationshipId: String,
        operations: List<PNJsonPatchOperation>,
        ifMatch: String? = null,
    ): UpdateRelationship

    /**
     * Replaces a relationship in full.
     *
     * Every mutable field is overwritten. Omitting [status] or [payload] clears the stored value rather than
     * preserving it, so a read-modify-write must send back every field it wants to keep. This cannot re-point
     * or reclassify a relationship — the two linked entities ([entityAId] / [entityBId]) and the relationship
     * class are immutable. Use [updateRelationship] to change part of a relationship.
     *
     * @param relationshipId Identifier of the relationship to replace.
     * @param classVersion Version of the relationship class the payload conforms to. Relationship classes are
     *   versioned (an integer `>= 1`); pass the version of the class you are targeting.
     * @param status Optional status to store with the relationship.
     * @param payload Optional replacement relationship fields (arbitrary JSON object).
     * @param ifMatch Optional eTag last read, to fail the request when the relationship changed since
     *   (`If-Match` header).
     */
    fun setRelationship(
        relationshipId: String,
        classVersion: Int,
        status: String? = null,
        payload: Any? = null,
        ifMatch: String? = null,
    ): SetRelationship
}
