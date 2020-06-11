package com.pubnub.api.models.consumer.message_actions

open class PNMessageAction(
    val type: String,
    val value: String,
    val messageTimetoken: Long
) {

    internal constructor(pnMessageAction: PNMessageAction) : this(
        pnMessageAction.type,
        pnMessageAction.value,
        pnMessageAction.messageTimetoken
    ) {
        this.uuid = pnMessageAction.uuid
        this.actionTimetoken = pnMessageAction.actionTimetoken
    }

    var uuid: String? = null
        private set
    var actionTimetoken: Long? = null
        private set
}