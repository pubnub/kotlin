package com.pubnub.api.models.consumer.message_actions

open class PNMessageAction {
    val type: String
    val value: String
    val messageTimetoken: Long

    constructor(pnMessageAction: PNMessageAction) {
        this.type = pnMessageAction.type
        this.value = pnMessageAction.value
        this.messageTimetoken = pnMessageAction.messageTimetoken
        this.uuid = pnMessageAction.uuid
        this.actionTimetoken = pnMessageAction.actionTimetoken
    }

    var uuid: String
        private set
    var actionTimetoken: Long? = null
        private set
}