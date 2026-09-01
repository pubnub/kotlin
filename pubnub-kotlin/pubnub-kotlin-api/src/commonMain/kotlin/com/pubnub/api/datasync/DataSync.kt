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
     * @param className Entity class identifier (required).
     * @param classVersion Optional entity class version. When `null` the server uses the latest.
     * @param classLevel Optional level at which the entity class is defined (e.g. `SubKey` / `Global`).
     * @param filterFast Optional filter expression. Filtering is only allowed on the entity class's properties
     *   whose filtering mode is not disabled (i.e. those the class marks as filterable); filtering on any
     *   other property returns a server error. The filterable/sortable set has no fixed default — it is
     *   whatever the (required) [className] declares, and system fields (e.g. `status`, `createdAt`,
     *   `updatedAt`, `id`) are not filterable unless the class declares a property for them. `filterFast` is
     *   strongly consistent — it always reflects the latest writes — but accepts fewer conditions than
     *   [filter]; a limit on the number of conditions applies and can be adjusted by PubNub support
     *   (see the PubNub DataSync documentation for the current limit). For larger or more complex queries use
     *   [filter]. At most one of [filterFast] and [filter] may be supplied; sending both is
     *   rejected with an error.
     * @param filter Optional advanced filter expression. Uses the same syntax and filterable-property
     *   rules as [filterFast] and is not subject to the same condition limit, so use it for larger or more complex
     *   queries. The trade-off is consistency: `filter` is eventually consistent (recent writes may not
     *   yet be reflected), whereas [filterFast] is strongly consistent. At most one of [filterFast] and [filter]
     *   may be supplied; sending both is rejected with an error.
     * @param sort Optional sort criteria applied in order; each [PNDataSyncSortField] sorts on a payload
     *   property either ascending (default) or descending. Sorting is governed by the same filterable-property
     *   rule as [filterFast] — the properties the (required) [className] marks as filterable.
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
     * @param filterFast Optional filter expression. Filtering is only allowed on the entity class's properties
     *   whose filtering mode is not disabled (i.e. those the class marks as filterable); filtering on any
     *   other property returns a server error. The built-in `User` class exposes `name` and `type` as its
     *   filterable properties. `filterFast` is strongly consistent — it always reflects the latest writes —
     *   but accepts fewer conditions than [filter]; a limit on the number of conditions applies and can
     *   be adjusted by PubNub support (see the PubNub DataSync documentation for the current limit). For larger
     *   or more complex queries use [filter]. At most one of [filterFast] and [filter] may be
     *   supplied; sending both is rejected with an error.
     * @param filter Optional advanced filter expression. Uses the same syntax and filterable-property
     *   rules as [filterFast] and is not subject to the same condition limit, so use it for larger or more complex
     *   queries. The trade-off is consistency: `filter` is eventually consistent (recent writes may not
     *   yet be reflected), whereas [filterFast] is strongly consistent. At most one of [filterFast] and [filter]
     *   may be supplied; sending both is rejected with an error.
     * @param sort Optional sort criteria applied in order; each [PNDataSyncSortField] sorts on a payload
     *   property either ascending (default) or descending. Sorting is governed by the same filterable-property
     *   rule as [filterFast] (built-in `User` class: `name` and `type`).
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
     * @param filterFast Optional filter expression. Filtering is only allowed on the entity class's properties
     *   whose filtering mode is not disabled (i.e. those the class marks as filterable); filtering on any
     *   other property returns a server error. For the default `Channel` class this set is `name` and `type`,
     *   but a custom class or subclass may declare additional filterable properties. These are filterable
     *   indexes over `/payload/name` and `/payload/type` — both nullable, not a required or exclusive payload
     *   schema; the `payload` stays arbitrary JSON. `filterFast` is strongly consistent — it always reflects the
     *   latest writes — but accepts fewer conditions than [filter]; a limit on the number of conditions
     *   applies and can be adjusted by PubNub support (see the PubNub DataSync documentation for the current
     *   limit). For larger or more complex queries use [filter]. At most one of [filterFast] and
     *   [filter] may be supplied; sending both is rejected with an error.
     * @param filter Optional advanced filter expression. Uses the same syntax and filterable-property
     *   rules as [filterFast] and is not subject to the same condition limit, so use it for larger or more complex
     *   queries. The trade-off is consistency: `filter` is eventually consistent (recent writes may not
     *   yet be reflected), whereas [filterFast] is strongly consistent. At most one of [filterFast] and [filter]
     *   may be supplied; sending both is rejected with an error.
     * @param sort Optional sort criteria applied in order; each [PNDataSyncSortField] sorts on a payload
     *   property either ascending (default) or descending. Sorting is governed by the same filterable-property
     *   rule as [filterFast] (default `Channel` class: `name` and `type`).
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
}
