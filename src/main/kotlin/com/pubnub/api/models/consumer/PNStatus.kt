package com.pubnub.api.models.consumer

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNStatusCategory
import okhttp3.Request

data class PNStatus(
    var category: PNStatusCategory,
    var error: Boolean,
    val operation: PNOperationType,

    val exception: PubNubException? = null,

    var statusCode: Int? = null,
    var tlsEnabled: Boolean? = null,
    var origin: String? = null,
    var uuid: String? = null,
    var authKey: String? = null,

    var affectedChannels: List<String?> = emptyList(),
    var affectedChannelGroups: List<String?> = emptyList()

) {

    var executedEndpoint: Endpoint<*, *>? = null
    var clientRequest: Request? = null

    fun retry() {
        executedEndpoint?.retry()
    }

}