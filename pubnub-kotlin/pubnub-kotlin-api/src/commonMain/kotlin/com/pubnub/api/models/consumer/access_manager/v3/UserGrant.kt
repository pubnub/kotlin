package com.pubnub.api.models.consumer.access_manager.v3

interface UserGrant : TokenGrant {
    /**
     * The single DataSync projection the token holder looks *through* when reading this user's DataSync schema, or
     * `null` for the implicit `__default__` projection. Same semantics as [DataSyncGrantType.projection]: it is a
     * token-level viewing projection folded into the token `meta` (so `meta` must be `null` or a map whenever any
     * grant carries a projection). The user's *permission* stays in the plain `users` bucket; only the projection
     * entry uses the `datasync:users:<id>` composite key.
     *
     * Defaulted so external implementers of this interface are not broken by its addition.
     */
    val projection: String? get() = null

    companion object {
        fun id(
            id: String,
            get: Boolean = false,
            update: Boolean = false,
            delete: Boolean = false,
            create: Boolean = false,
            projection: String? = null,
        ): UserGrant =
            PNUserResourceGrant(
                id = id,
                delete = delete,
                get = get,
                update = update,
                create = create,
                projection = projection,
            )

        fun pattern(
            pattern: String,
            get: Boolean = false,
            update: Boolean = false,
            delete: Boolean = false,
            create: Boolean = false,
            projection: String? = null,
        ): UserGrant =
            PNUserPatternGrant(
                id = pattern,
                delete = delete,
                get = get,
                update = update,
                create = create,
                projection = projection,
            )
    }
}
