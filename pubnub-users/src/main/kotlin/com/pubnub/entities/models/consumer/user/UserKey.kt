package com.pubnub.entities.models.consumer.user

import com.pubnub.api.models.consumer.objects.PNKey
import com.pubnub.api.models.consumer.objects.SortableKey

enum class UserKey : SortableKey {
    ID, NAME, UPDATED;

    fun toPNSortKey(): PNKey {
        return when (this) {
            ID -> PNKey.ID
            NAME -> PNKey.NAME
            UPDATED -> PNKey.UPDATED
        }
    }
}
