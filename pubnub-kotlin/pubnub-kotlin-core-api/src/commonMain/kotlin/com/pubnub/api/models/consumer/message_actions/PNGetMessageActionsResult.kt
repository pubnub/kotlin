package com.pubnub.api.models.consumer.message_actions

import com.pubnub.api.models.consumer.PNBoundedPage
import com.pubnub.api.utils.SerializedName

/**
 * Result for the GetMessageActions API operation.
 *
 * @property actions List of message actions for a certain message in a certain channel.
 * @property page Information about next page. When null there's no next page.
 */

class PNGetMessageActionsResult(
    @field:SerializedName("data")
    val actions: List<PNMessageAction>,
    @field:SerializedName("more")
    val page: PNBoundedPage?,
)
