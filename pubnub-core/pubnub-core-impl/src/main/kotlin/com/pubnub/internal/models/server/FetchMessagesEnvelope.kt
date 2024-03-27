package com.pubnub.internal.models.server

import com.pubnub.internal.models.server.history.ServerFetchMessageItem

data class FetchMessagesEnvelope(
    val channels: Map<String, List<ServerFetchMessageItem>>,
    val more: FetchMessagesPage?,
)

data class FetchMessagesPage(
    val start: Long? = null,
    val end: Long? = null,
    val max: Int? = null,
)
