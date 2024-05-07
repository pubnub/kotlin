package com.pubnub.api.models.consumer.message_actions

import com.pubnub.api.SerializedName
import com.pubnub.api.models.consumer.PNBoundedPage

/**
 * Result for the GetMessageActions API operation.
 *
 * @property actions List of message actions for a certain message in a certain channel.
 * @property page Information about next page. When null there's no next page.
 */
class PNGetMessageActionsResult(
    @SerializedName("data")
    val actions: List<PNMessageAction>,
    @SerializedName("more")
    val page: PNBoundedPage?,
)
