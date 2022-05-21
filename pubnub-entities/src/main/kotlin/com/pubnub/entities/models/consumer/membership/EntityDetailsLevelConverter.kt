package com.pubnub.entities.models.consumer.membership

import com.pubnub.api.models.consumer.objects.member.PNUUIDDetailsLevel
import com.pubnub.api.models.consumer.objects.membership.PNChannelDetailsLevel

class EntityDetailsLevelConverter {

    companion object {
        internal fun convertSpaceDetailsLevelToPNChannelDetailsLevel(spaceDetailsLevel: SpaceDetailsLevel?): PNChannelDetailsLevel {
            return this.let {
                if (spaceDetailsLevel == SpaceDetailsLevel.SPACE) {
                    PNChannelDetailsLevel.CHANNEL
                } else if (spaceDetailsLevel == SpaceDetailsLevel.SPACE_WITH_CUSTOM) {
                    PNChannelDetailsLevel.CHANNEL_WITH_CUSTOM
                } else {
                    throw Exception("Invalid PNChannelDetailsLevel")
                }
            }
        }

        internal fun convertUserDetailsLevelToPNUUIDDetailsLevel(userDetailsLevel: UserDetailsLevel?): PNUUIDDetailsLevel {

            return if (userDetailsLevel == UserDetailsLevel.USER) {
                PNUUIDDetailsLevel.UUID
            } else if (userDetailsLevel == UserDetailsLevel.USER_WITH_CUSTOM) {
                PNUUIDDetailsLevel.UUID_WITH_CUSTOM
            } else {
                throw Exception("Invalid PNChannelDetailsLevel")
            }
        }
    }
}
