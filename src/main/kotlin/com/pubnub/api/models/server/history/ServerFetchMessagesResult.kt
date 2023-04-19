package com.pubnub.api.models.server.history

import com.pubnub.api.models.consumer.PNBoundedPage

data class ServerFetchMessagesResult(
    val channels: Map<String, List<ServerFetchMessageItem>>,
    val page: PNBoundedPage?
)
