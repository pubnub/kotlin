package com.pubnub.internal.endpoints

import com.pubnub.api.models.consumer.PNBoundedPage
import com.pubnub.api.models.consumer.history.PNFetchMessagesResult
import com.pubnub.internal.EndpointInterface

interface FetchMessagesInterface : EndpointInterface<PNFetchMessagesResult> {
    val channels: List<String>
    val page: PNBoundedPage
    val includeUUID: Boolean
    val includeMeta: Boolean
    val includeMessageActions: Boolean
    val includeMessageType: Boolean
}
