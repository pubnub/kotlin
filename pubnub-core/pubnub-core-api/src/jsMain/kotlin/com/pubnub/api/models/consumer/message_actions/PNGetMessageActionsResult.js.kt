package com.pubnub.api.models.consumer.message_actions

import com.pubnub.api.models.consumer.PNBoundedPage

actual class PNGetMessageActionsResult actual constructor(
    actual val actions: List<PNSavedMessageAction>,
    actual val page: PNBoundedPage?,
)
