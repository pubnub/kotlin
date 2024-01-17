package com.pubnub.internal.endpoints.message_actions

import com.pubnub.api.models.consumer.PNBoundedPage

interface IGetMessageActions {
    val channel: String
    val page: PNBoundedPage
}
