package com.pubnub.api.models.consumer.access_manager.sum

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.SpaceId
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant
import com.pubnub.api.models.consumer.access_manager.v3.PNChannelPatternGrant
import com.pubnub.api.models.consumer.access_manager.v3.PNChannelResourceGrant
import com.pubnub.api.models.consumer.access_manager.v3.PNGrant

interface SpaceIdGrant : PNGrant {

    companion object {
        fun name(
            spaceId: SpaceId,
            read: Boolean = false,
            write: Boolean = false,
            manage: Boolean = false,
            delete: Boolean = false,
            create: Boolean = false,
            get: Boolean = false,
            join: Boolean = false,
            update: Boolean = false
        ): SpaceIdGrant = PNChannelResourceGrant(
            id = spaceId.value,
            read = read,
            write = write,
            manage = manage,
            delete = delete,
            create = create,
            get = get,
            join = join,
            update = update
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
            update: Boolean = false
        ): SpaceIdGrant = PNChannelPatternGrant(
            id = pattern,
            read = read,
            write = write,
            manage = manage,
            delete = delete,
            create = create,
            get = get,
            join = join,
            update = update
        )
    }
}

fun SpaceIdGrant.toChannelGrants(): ChannelGrant {
    return when (this) {
        is PNChannelResourceGrant -> PNChannelResourceGrant(spaceIdGrant = this)
        is PNChannelPatternGrant -> PNChannelPatternGrant(spaceIdGrant = this)
        else -> throw PubNubException(pubnubError = PubNubError.INVALID_ARGUMENTS) // to be changed
    }
}
