package com.pubnub.entities.models.consumer.space

import com.pubnub.api.models.consumer.objects.PNKey
import com.pubnub.api.models.consumer.objects.SortableKey

enum class SpaceKey() : SortableKey {
    ID(), NAME(), UPDATED();

    fun toPNSortKey(): PNKey {
        return when (this) {
            ID -> PNKey.ID
            NAME -> PNKey.NAME
            UPDATED -> PNKey.UPDATED
        }
    }
}
