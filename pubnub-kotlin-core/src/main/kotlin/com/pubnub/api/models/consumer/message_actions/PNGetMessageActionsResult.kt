package com.pubnub.api.models.consumer.message_actions

import com.google.gson.annotations.SerializedName
import com.pubnub.api.models.consumer.PNBoundedPage
import com.pubnub.internal.PubNubImpl

/**
 * Result for the [PubNubImpl.getMessageActions] API operation.
 *
 * @property actions List of message actions for a certain message in a certain channel.
 * @property page Information about next page. When null there's no next page.
 */
class PNGetMessageActionsResult internal constructor(
    @SerializedName("data")
    val actions: List<PNMessageAction>,
    @SerializedName("more")
    val page: PNBoundedPage?
)
