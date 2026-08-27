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
     * @param entityClass Entity class identifier.
     * @param entityClassVersion Version of the entity class.
     * @param entityId Optional entity identifier. When `null` the server generates one.
     * @param status Optional entity status.
     * @param payload Optional arbitrary JSON object payload.
     */
    fun createEntity(
        entityClass: String,
        entityClassVersion: Int,
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
     * @param entityClass Entity class identifier (required).
     * @param entityClassVersion Optional entity class version. When `null` the server uses the latest.
     * @param entityClassLevel Optional level at which the entity class is defined (e.g. `SubKey` / `Global`).
     * @param filter Optional filter expression. Strongly consistent (always reflects the latest writes) but
     *   limited in the number of conditionals per request — currently at most 10, which support may raise via
     *   keyset configuration. A `filter` with more conditionals than allowed is rejected with an error; use
     *   [filterAdvanced] for larger or more complex queries.
     * @param filterAdvanced Optional advanced filter expression. Same syntax as [filter] but without the
     *   conditional-count limit, at the cost of consistency: `filterAdvanced` is eventually consistent (recent
     *   writes may not yet be reflected), whereas [filter] is strongly consistent.
     * @param sort Optional comma-separated sort fields, each optionally suffixed with a direction
     *   (`:asc` or `:desc`, default `:asc`), e.g. `username:desc,email:asc`.
     * @param limit Optional page size (1–100, server default 20).
     * @param cursor Optional opaque cursor for pagination (from a previous result's `next`).
     */
    fun getEntities(
        entityClass: String,
        entityClassVersion: Int? = null,
        entityClassLevel: String? = null,
        filter: String? = null,
        filterAdvanced: String? = null,
        sort: String? = null,
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
     * Fully replace a DataSync entity. `entityClass` is immutable and cannot be updated.
     *
     * @param entityId Identifier of the entity to update.
     * @param entityClassVersion Version of the entity class.
     * @param status Optional entity status.
     * @param payload Optional arbitrary JSON object payload.
     * @param ifMatch Optional eTag for optimistic concurrency (`If-Match` header).
     */
    fun setEntity(
        entityId: String,
        entityClassVersion: Int,
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
     * @param entityClassVersion Version of the entity class.
     * @param userId Optional user identifier. When `null` the server generates one.
     * @param entityClass Optional entity class identifier. When `null` the server defaults it to `User`.
     *   When set it must be a `User` subclass.
     * @param status Optional user status.
     * @param payload Optional arbitrary JSON object payload.
     */
    fun createUser(
        entityClassVersion: Int,
        userId: String? = null,
        entityClass: String? = null,
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
     * @param entityClass Optional entity class identifier. When `null` the whole User family is returned;
     *   when set it narrows the results to that `User` subclass.
     * @param entityClassVersion Optional entity class version. When `null` the server uses the latest.
     * @param entityClassLevel Optional level at which the entity class is defined (e.g. `SubKey` / `Global`).
     * @param filter Optional filter expression. Strongly consistent (always reflects the latest writes) but
     *   limited in the number of conditionals per request — currently at most 10, which support may raise via
     *   keyset configuration. A `filter` with more conditionals than allowed is rejected with an error; use
     *   [filterAdvanced] for larger or more complex queries.
     * @param filterAdvanced Optional advanced filter expression. Same syntax as [filter] but without the
     *   conditional-count limit, at the cost of consistency: `filterAdvanced` is eventually consistent (recent
     *   writes may not yet be reflected), whereas [filter] is strongly consistent.
     * @param sort Optional comma-separated sort fields, each optionally suffixed with a direction
     *   (`:asc` or `:desc`, default `:asc`), e.g. `username:desc,email:asc`.
     * @param limit Optional page size (1–100, server default 20).
     * @param cursor Optional opaque cursor for pagination (from a previous result's `next`).
     */
    fun getUsers(
        entityClass: String? = null,
        entityClassVersion: Int? = null,
        entityClassLevel: String? = null,
        filter: String? = null,
        filterAdvanced: String? = null,
        sort: String? = null,
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
     * Fully replace a DataSync user. `entityClass` is immutable and cannot be updated.
     *
     * @param userId Identifier of the user to update.
     * @param entityClassVersion Version of the entity class.
     * @param status Optional user status.
     * @param payload Optional arbitrary JSON object payload.
     * @param ifMatch Optional eTag for optimistic concurrency (`If-Match` header).
     */
    fun setUser(
        userId: String,
        entityClassVersion: Int,
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
     * @param className Optional entity class identifier. When `null` the whole Channel family is returned;
     *   when set it narrows the results to that `Channel` subclass.
     * @param classVersion Optional entity class version. When `null` the server uses the latest.
     * @param classLevel Optional level at which the entity class is defined. The built-in `Channel` class is
     *   defined at the `GLOBAL` level.
     * @param filter Optional filter expression. Filtering is only allowed on the entity class's properties
     *   whose filtering mode is not disabled (i.e. those the class marks as filterable); filtering on any
     *   other property returns a server error. For the default `Channel` class this set is `name` and `type`,
     *   but a custom class or subclass may declare additional filterable properties. These are filterable
     *   indexes over `/payload/name` and `/payload/type` — both nullable, not a required or exclusive payload
     *   schema; the `payload` stays arbitrary JSON. `filter` is strongly consistent (it always reflects the
     *   latest writes) but limited in the number of conditionals per request — currently at most 10, which
     *   support may raise via keyset configuration. A `filter` with more conditionals than allowed is
     *   rejected with an error; use [filterAdvanced] for larger or more complex queries.
     * @param filterAdvanced Optional advanced filter expression. Uses the same syntax and filterable-property
     *   rules as [filter] but is not subject to the conditional-count limit, so use it for larger or more
     *   complex queries. The trade-off is consistency: `filterAdvanced` is eventually consistent (recent writes
     *   may not yet be reflected), whereas [filter] is strongly consistent.
     * @param sort Optional sort criteria applied in order; each [PNDataSyncSortField] sorts on a payload
     *   property either ascending (default) or descending. Sorting is governed by the same filterable-property
     *   rule as [filter] (default `Channel` class: `name` and `type`).
     * @param limit Optional page size (1–100, server default 20).
     * @param cursor Optional opaque cursor for pagination (from a previous result's `next.cursor`).
     */
    fun getChannels(
        className: String? = null,
        classVersion: Int? = null,
        classLevel: PNDataSyncClassLevel? = null,
        filter: String? = null,
        filterAdvanced: String? = null,
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
     * Fully replace a DataSync channel. The entity class is immutable and cannot be updated.
     *
     * @param channelId Identifier of the channel to update.
     * @param classVersion Version of the entity class.
     * @param status Optional channel status.
     * @param payload Optional arbitrary JSON object payload.
     * @param ifMatch Optional eTag for optimistic concurrency (`If-Match` header).
     */
    fun setChannel(
        channelId: String,
        classVersion: Int,
        status: String? = null,
        payload: Any? = null,
        ifMatch: String? = null,
    ): SetChannel
}
