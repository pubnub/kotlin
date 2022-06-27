package com.pubnub.api.models.consumer.access_manager.sum

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.SpaceId
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant
import com.pubnub.api.models.consumer.access_manager.v3.PNChannelPatternGrant
import com.pubnub.api.models.consumer.access_manager.v3.PNChannelResourceGrant
import com.pubnub.api.models.consumer.access_manager.v3.PNGrant
import com.pubnub.api.models.consumer.access_manager.v3.PNSpacePatternGrant
import com.pubnub.api.models.consumer.access_manager.v3.PNSpaceResourceGrant

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
        ): SpaceIdGrant = PNSpaceResourceGrant(
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
        ): SpaceIdGrant = PNSpacePatternGrant(
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

fun SpaceIdGrant.toChannelGrant(): ChannelGrant {
    return when (this) {
        is PNSpaceResourceGrant -> PNChannelResourceGrant(spaceIdGrant = this)
        is PNSpacePatternGrant -> PNChannelPatternGrant(spaceIdGrant = this)
        else -> throw PubNubException(pubnubError = PubNubError.INVALID_ARGUMENTS)
    }
}
