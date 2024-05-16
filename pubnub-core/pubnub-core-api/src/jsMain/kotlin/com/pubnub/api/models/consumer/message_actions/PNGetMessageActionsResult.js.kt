package com.pubnub.api.models.consumer.message_actions

import com.google.gson.annotations.SerializedName
import com.pubnub.api.models.consumer.PNBoundedPage

actual class PNGetMessageActionsResult actual constructor(
    actual val actions: List<PNMessageAction>,
    actual val page: PNBoundedPage?,
)
