package com.pubnub.api.models.server

import com.pubnub.api.models.consumer.history.PNFetchMessageItem

class FetchMessagesEnvelope(
    val channels: HashMap<String, List<PNFetchMessageItem>>
)
