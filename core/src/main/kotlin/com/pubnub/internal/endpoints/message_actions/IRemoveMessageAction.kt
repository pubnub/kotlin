package com.pubnub.internal.endpoints.message_actions

interface IRemoveMessageAction {
    val channel: String
    val messageTimetoken: Long
    val actionTimetoken: Long
}
