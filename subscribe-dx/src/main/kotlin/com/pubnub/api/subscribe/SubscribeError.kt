package com.pubnub.api.subscribe

import com.pubnub.core.CoreError

enum class SubscribeError(override val code: Int, override val message: String) : CoreError {
    CHANNEL_OR_CHANNEL_GROUP_MISSING(
        171,
        "Please, provide channel or channelGroup"
    );
}