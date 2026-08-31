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
    // Optional class level (wire string, e.g. "SubKey" / "Global") — the endpoint maps the public
    // `classLevel: PNDataSyncClassLevel?` param to `entityClassLevel = classLevel?.value`. Omitted from
    // the wire payload when null.
    val entityClassLevel: String? = null,
    val status: String? = null,
    val payload: Any? = null,
)
