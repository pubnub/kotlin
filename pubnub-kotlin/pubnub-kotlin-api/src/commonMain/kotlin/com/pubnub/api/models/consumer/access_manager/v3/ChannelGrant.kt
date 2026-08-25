package com.pubnub.api.models.consumer.access_manager.v3

interface ChannelGrant : TokenGrant {
    /**
     * The single DataSync projection the token holder looks *through* when reading this channel's DataSync schema,
     * or `null` for the implicit `__default__` projection. Same semantics as [DataSyncGrantType.projection]: it is a
     * token-level viewing projection folded into the token `meta` (so `meta` must be `null` or a map whenever any
     * grant carries a projection). It applies only to DataSync reads through this channel's schema and is ignored by
     * the pub/sub / Presence / App Context permission bits carried on the same grant.
     */
    val projection: String? get() = null

    companion object {
        fun name(
            name: String, // this is channelId :|
            read: Boolean = false,
            write: Boolean = false,
            manage: Boolean = false,
            delete: Boolean = false,
            create: Boolean = false,
            get: Boolean = false,
            join: Boolean = false,
            update: Boolean = false,
            projection: String? = null,
        ): ChannelGrant =
            PNChannelResourceGrant(
                id = name,
                read = read,
                write = write,
                manage = manage,
                delete = delete,
                create = create,
                get = get,
                join = join,
                update = update,
                projection = projection,
            )

        fun pattern(
            pattern: String,
            read: Boolean = false,
            write: Boolean = false,
            manage: Boolean = false,
            delete: Boolean = false,
            create: Boolean = false,
            get: Boolean = false,
            join: Boolean = false,
            update: Boolean = false,
            projection: String? = null,
        ): ChannelGrant =
            PNChannelPatternGrant(
                id = pattern,
                read = read,
                write = write,
                manage = manage,
                delete = delete,
                create = create,
                get = get,
                join = join,
                update = update,
                projection = projection,
            )
    }
}
