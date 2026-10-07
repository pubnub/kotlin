package com.pubnub.api.models.consumer.access_manager.v3

/**
 * A PAM v3 grant on a **channel**: pub/sub (`read`/`write`), presence, channel management (`manage`) and App Context
 * v2 channel metadata / members / memberships (`get`/`update`/`delete`/`join`).
 *
 * For DataSync channels use [DataSyncGrant.channel] (REST CRUD, optional projection) and [DataSyncGrant.subscribe]
 * (realtime subscribe on the resolved ref-channel) instead.
 */
interface ChannelGrant : TokenGrant {
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
            )
    }
}
