package com.pubnub.api.models.consumer.message_actions

import com.pubnub.api.models.consumer.PNBoundedPage

/**
 * Result for the GetMessageActions API operation.
 *
 * @property actions List of message actions for a certain message in a certain channel.
 * @property page Information about next page. When null there's no next page.
 */
expect class PNGetMessageActionsResult(actions: List<PNMessageAction>, page: PNBoundedPage?) {
    val actions: List<PNMessageAction>
    val page: PNBoundedPage?
}
