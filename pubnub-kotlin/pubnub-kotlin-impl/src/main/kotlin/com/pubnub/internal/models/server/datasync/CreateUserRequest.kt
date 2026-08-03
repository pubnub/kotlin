package com.pubnub.internal.models.server.datasync

internal data class CreateUserRequest(
    val data: CreateUserRequestData,
)

internal data class CreateUserRequestData(
    // Omitted from the wire payload when null (server generates the id) — the SDK's Gson instance
    // does not serialize nulls.
    val id: String? = null,
    // Omitted from the wire payload when null — the server defaults it to "User". When set it must be
    // a `User` subclass.
    val entityClass: String? = null,
    val entityClassVersion: Int,
    val status: String? = null,
    val payload: Any? = null,
)
