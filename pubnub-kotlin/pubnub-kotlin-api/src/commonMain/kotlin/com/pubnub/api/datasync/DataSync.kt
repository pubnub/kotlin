package com.pubnub.api.datasync

import com.pubnub.api.endpoints.datasync.user.CreateUser
import com.pubnub.api.endpoints.datasync.user.GetUser
import com.pubnub.api.endpoints.datasync.user.GetUsers
import com.pubnub.api.endpoints.datasync.user.PatchUser
import com.pubnub.api.endpoints.datasync.user.RemoveUser
import com.pubnub.api.endpoints.datasync.user.UpdateUser
import com.pubnub.api.models.consumer.datasync.entity.PNJsonPatchOperation

/**
 * Entry point for the DataSync API, reached via `pubnub.dataSync`.
 */
interface DataSync {
    /**
     * Entity operations (`get` / `getAll` / `create` / `update` / `patch` / `delete`).
     */
    val entity: EntityApi

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
     * @param filter Optional filter expression.
     * @param filterAdvanced Optional advanced filter expression.
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
    fun patchUser(
        userId: String,
        operations: List<PNJsonPatchOperation>,
        ifMatch: String? = null,
    ): PatchUser

    /**
     * Fully replace a DataSync user. `entityClass` is immutable and cannot be updated.
     *
     * @param userId Identifier of the user to update.
     * @param entityClassVersion Version of the entity class.
     * @param status Optional user status.
     * @param payload Optional arbitrary JSON object payload.
     * @param ifMatch Optional eTag for optimistic concurrency (`If-Match` header).
     */
    fun updateUser(
        userId: String,
        entityClassVersion: Int,
        status: String? = null,
        payload: Any? = null,
        ifMatch: String? = null,
    ): UpdateUser
}
