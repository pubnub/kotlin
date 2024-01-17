package com.pubnub.internal.endpoints

import com.pubnub.api.models.consumer.PNBoundedPage

interface IFetchMessages {
    val channels: List<String>
    val page: PNBoundedPage
    val includeUUID: Boolean
    val includeMeta: Boolean
    val includeMessageActions: Boolean
    val includeMessageType: Boolean
}
