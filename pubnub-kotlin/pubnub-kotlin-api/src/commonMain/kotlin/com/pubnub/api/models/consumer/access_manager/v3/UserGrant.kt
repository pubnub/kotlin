package com.pubnub.api.models.consumer.access_manager.v3

interface UserGrant : PNGrant {
    companion object {
        fun id(
            id: String,
            get: Boolean = false,
            update: Boolean = false,
            delete: Boolean = false,
            create: Boolean = false,
        ): UserGrant =
            PNUserResourceGrant(
                id = id,
                delete = delete,
                get = get,
                update = update,
                create = create,
            )

        fun pattern(
            pattern: String,
            get: Boolean = false,
            update: Boolean = false,
            delete: Boolean = false,
            create: Boolean = false,
        ): UserGrant =
            PNUserPatternGrant(
                id = pattern,
                delete = delete,
                get = get,
                update = update,
                create = create,
            )
    }
}
